package cz.cvut.fel.pjv.spacemechanic.level;

import cz.cvut.fel.pjv.spacemechanic.collision.CollisionManager;
import cz.cvut.fel.pjv.spacemechanic.model.Chest;
import cz.cvut.fel.pjv.spacemechanic.model.DoorSystem;
import cz.cvut.fel.pjv.spacemechanic.model.Elevator;
import cz.cvut.fel.pjv.spacemechanic.model.Engine;
import cz.cvut.fel.pjv.spacemechanic.model.GameObject;
import cz.cvut.fel.pjv.spacemechanic.model.Generator;
import cz.cvut.fel.pjv.spacemechanic.model.Item;
import cz.cvut.fel.pjv.spacemechanic.model.LockedDoor;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.RepairableObject;
import cz.cvut.fel.pjv.spacemechanic.model.ShipTerminal;
import cz.cvut.fel.pjv.spacemechanic.model.SparePart;
import cz.cvut.fel.pjv.spacemechanic.model.ToolItem;
import cz.cvut.fel.pjv.spacemechanic.model.Wall;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private int currentLevel;

    private final List<GameObject> objects;
    private final List<GameObject> levelOneObjects;
    private final List<GameObject> levelTwoObjects;

    private final Player player;
    private final CollisionManager collisionManager;
    private String lastMessage;

    private boolean levelOneObjectiveCompleted;
    private boolean levelTwoObjectiveCompleted;

    public LevelManager() {
        this.currentLevel = 1;

        this.objects = new ArrayList<>();
        this.levelOneObjects = new ArrayList<>();
        this.levelTwoObjects = new ArrayList<>();

        this.player = new Player(100, 100, 32, 32);
        this.collisionManager = new CollisionManager();
        this.lastMessage = "Find parts and repair ship systems.";

        this.levelOneObjectiveCompleted = false;
        this.levelTwoObjectiveCompleted = false;

        createLevels();
        loadLevel(1);
    }

    private void createLevels() {
        levelOneObjects.clear();
        levelTwoObjects.clear();

        levelOneObjects.addAll(loadObjectsFromFile("levels/level1.txt"));
        levelTwoObjects.addAll(loadObjectsFromFile("levels/level2.txt"));
    }

    private List<GameObject> loadObjectsFromFile(String fileName) {
        List<GameObject> loadedObjects = new ArrayList<>();

        try {
            BufferedReader reader = openLevelFile(fileName);
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                GameObject object = createObjectFromLine(line);
                loadedObjects.add(object);
            }

            reader.close();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load level file: " + fileName, e);
        }

        return loadedObjects;
    }

    private BufferedReader openLevelFile(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        if (inputStream != null) {
            return new BufferedReader(new InputStreamReader(inputStream));
        }

        String[] possiblePaths = {
                fileName,
                "SpaceMechanic_CP2/" + fileName,
                "src/main/resources/" + fileName,
                "SpaceMechanic_CP2/src/main/resources/" + fileName,
                "./" + fileName,
                "./SpaceMechanic_CP2/" + fileName,
                "./src/main/resources/" + fileName,
                "./SpaceMechanic_CP2/src/main/resources/" + fileName
        };

        for (String path : possiblePaths) {
            File file = new File(path);

            if (file.exists()) {
                return new BufferedReader(new FileReader(file));
            }
        }

        throw new IOException("Level file not found: " + fileName);
    }

    private GameObject createObjectFromLine(String line) {
        String[] parts = line.split(",");

        String type = parts[0];
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int width = Integer.parseInt(parts[3]);
        int height = Integer.parseInt(parts[4]);

        if (type.equals("SPARE_PART")) {
            String name = parts[5];
            return new SparePart(x, y, width, height, name);
        }

        if (type.equals("TOOL")) {
            String name = parts[5];
            return new ToolItem(x, y, width, height, name);
        }

        if (type.equals("ENGINE")) {
            String requiredPart = parts[5];
            return new Engine(x, y, width, height, requiredPart);
        }

        if (type.equals("GENERATOR")) {
            String requiredPart = parts[5];
            return new Generator(x, y, width, height, requiredPart);
        }

        if (type.equals("DOOR")) {
            String requiredPart = parts[5];
            return new DoorSystem(x, y, width, height, requiredPart);
        }

        if (type.equals("TERMINAL")) {
            String hint = parts[5];
            return new ShipTerminal(x, y, width, height, hint);
        }

        if (type.equals("ELEVATOR")) {
            return new Elevator(x, y, width, height);
        }

        if (type.equals("WALL")) {
            return new Wall(x, y, width, height);
        }

        if (type.equals("LOCKED_DOOR")) {
            String requiredModule = parts[5];
            return new LockedDoor(x, y, width, height, requiredModule);
        }

        if (type.equals("CHEST")) {
            String itemName = parts[5];
            return new Chest(x, y, width, height, itemName);
        }

        throw new IllegalArgumentException("Unknown object type: " + type);
    }

    public void loadLevel(int level) {
        objects.clear();
        currentLevel = level;

        if (level == 1) {
            objects.addAll(levelOneObjects);
            player.setX(500);
            player.setY(220);
            lastMessage = "Level 1: Engineering Deck.";
        } else if (level == 2) {
            objects.addAll(levelTwoObjects);
            player.setX(500);
            player.setY(420);
            lastMessage = "Level 2: Control Deck.";
        }
    }

    public void update() {
        int oldX = player.getBounds().x;
        int oldY = player.getBounds().y;

        player.update();

        for (GameObject object : objects) {
            if (object.isActive()) {
                object.update();
            }
        }

        if (isPlayerBlocked()) {
            player.setX(oldX);
            player.setY(oldY);
        }

        collisionManager.checkCollisions(player, objects);
    }

    private boolean isPlayerBlocked() {
        for (GameObject object : objects) {
            if (!object.isActive()) {
                continue;
            }

            if (isSolidObject(object) && player.getBounds().intersects(object.getBounds())) {
                return true;
            }
        }

        return false;
    }

    private boolean isSolidObject(GameObject object) {
        return object instanceof Wall || object instanceof LockedDoor;
    }

    public void render(Graphics g) {
        drawShipBackground(g);

        player.render(g);

        for (GameObject object : objects) {
            if (object.isActive()) {
                object.render(g);
            }
        }
    }

    private void drawShipBackground(Graphics g) {
        int shipX = 370;
        int shipY = 80;
        int shipWidth = 830;
        int shipHeight = 560;

        drawSpaceBackground(g);

        g.setColor(new Color(20, 26, 38));
        g.fillRoundRect(shipX - 18, shipY - 18, shipWidth + 36, shipHeight + 36, 34, 34);

        g.setColor(new Color(105, 130, 165));
        g.drawRoundRect(shipX - 18, shipY - 18, shipWidth + 36, shipHeight + 36, 34, 34);

        if (currentLevel == 1) {
            g.setColor(new Color(31, 38, 52));
        } else {
            g.setColor(new Color(29, 34, 52));
        }

        g.fillRoundRect(shipX, shipY, shipWidth, shipHeight, 26, 26);

        g.setColor(new Color(75, 92, 120));
        g.drawRoundRect(shipX, shipY, shipWidth, shipHeight, 26, 26);

        if (currentLevel == 1) {
            g.setColor(new Color(38, 46, 61));
        } else {
            g.setColor(new Color(35, 42, 64));
        }

        g.fillRect(shipX + 35, shipY + 55, shipWidth - 70, shipHeight - 95);

        if (currentLevel == 1) {
            drawRoom(g, 410, 135, 230, 190, "STORAGE");
            drawRoom(g, 670, 135, 210, 190, "LIFT AREA");
            drawRoom(g, 910, 135, 230, 190, "POWER PANEL");
            drawRoom(g, 410, 355, 330, 190, "REPAIR BAY");
            drawRoom(g, 780, 355, 360, 190, "ENGINE ROOM");
        } else {
            drawRoom(g, 410, 135, 260, 190, "CONTROL ROOM");
            drawRoom(g, 700, 135, 180, 190, "LIFT CORE");
            drawRoom(g, 910, 135, 230, 190, "DATA STORAGE");
            drawRoom(g, 410, 355, 260, 190, "FUEL STORAGE");
            drawRoom(g, 700, 355, 440, 190, "MAIN ENGINE");
        }

        drawCorridors(g);
        drawShipDecorations(g);
        drawRoomDetails(g);

        g.setColor(Color.WHITE);
        if (currentLevel == 1) {
            g.drawString("ENGINEERING DECK", 720, 110);
        } else {
            g.drawString("CONTROL DECK", 735, 110);
        }
    }

    private void drawSpaceBackground(Graphics g) {
        g.setColor(new Color(7, 11, 20));
        g.fillRect(350, 20, 900, 720);

        g.setColor(new Color(95, 120, 170));
        for (int i = 0; i < 65; i++) {
            int starX = 360 + (i * 83) % 870;
            int starY = 25 + (i * 47) % 690;
            int size = (i % 7 == 0) ? 3 : 2;
            g.fillOval(starX, starY, size, size);
        }

        g.setColor(new Color(35, 48, 80));
        g.fillOval(1120, 55, 90, 90);

        g.setColor(new Color(60, 85, 130));
        g.drawOval(1120, 55, 90, 90);

        g.setColor(new Color(25, 35, 60));
        g.fillOval(1145, 75, 18, 10);
        g.fillOval(1170, 105, 24, 12);

        g.setColor(new Color(35, 45, 80));
        g.drawLine(390, 690, 560, 650);
        g.drawLine(700, 700, 900, 660);
        g.drawLine(940, 680, 1180, 620);
    }

    private void drawCorridors(Graphics g) {
        g.setColor(new Color(58, 68, 84));

        if (currentLevel == 1) {
            g.fillRect(640, 220, 30, 35);
            g.fillRect(880, 220, 30, 35);
            g.fillRect(740, 420, 40, 35);
        } else {
            g.fillRect(670, 220, 30, 35);
            g.fillRect(880, 220, 30, 35);
            g.fillRect(670, 420, 30, 35);
        }

        g.setColor(new Color(90, 105, 130));

        if (currentLevel == 1) {
            g.drawLine(640, 220, 670, 220);
            g.drawLine(880, 220, 910, 220);
            g.drawLine(740, 420, 780, 420);
        } else {
            g.drawLine(670, 220, 700, 220);
            g.drawLine(880, 220, 910, 220);
            g.drawLine(670, 420, 700, 420);
        }
    }

    private void drawShipDecorations(Graphics g) {
        g.setColor(new Color(88, 98, 115));
        g.drawLine(430, 585, 1120, 585);
        g.drawLine(430, 595, 1120, 595);

        if (currentLevel == 1) {
            g.setColor(new Color(80, 150, 230));
        } else {
            g.setColor(new Color(165, 110, 235));
        }

        g.drawLine(460, 590, 570, 590);
        g.drawLine(690, 590, 830, 590);
        g.drawLine(950, 590, 1080, 590);

        g.setColor(new Color(130, 150, 175));
        for (int x = 400; x <= 1160; x += 80) {
            g.fillOval(x, 92, 3, 3);
            g.fillOval(x, 625, 3, 3);
        }
    }

    private void drawRoomDetails(Graphics g) {
        if (currentLevel == 1) {
            drawToolBench(g, 470, 465);
            drawControlScreens(g, 970, 220);
            drawEnginePipes(g, 850, 430);
            drawEngineCoreBase(g, 980, 455);
        } else {
            drawControlScreens(g, 470, 205);
            drawControlScreens(g, 540, 205);

            drawServerRack(g, 950, 205);
            drawServerRack(g, 1015, 205);

            drawFuelTanks(g, 470, 430);

            drawEnginePipes(g, 790, 430);
            drawEngineCoreBase(g, 920, 445);
        }
    }

    private void drawToolBench(Graphics g, int x, int y) {
        g.setColor(new Color(85, 90, 105));
        g.fillRoundRect(x, y, 90, 20, 6, 6);

        g.setColor(new Color(45, 50, 65));
        g.fillRect(x + 8, y + 20, 8, 30);
        g.fillRect(x + 74, y + 20, 8, 30);

        g.setColor(new Color(150, 170, 190));
        g.drawLine(x + 15, y + 8, x + 35, y + 8);
        g.drawLine(x + 50, y + 8, x + 75, y + 8);
    }

    private void drawControlScreens(Graphics g, int x, int y) {
        g.setColor(new Color(25, 35, 60));
        g.fillRoundRect(x, y, 45, 28, 6, 6);

        g.setColor(new Color(80, 160, 230));
        g.drawRoundRect(x, y, 45, 28, 6, 6);

        g.setColor(new Color(100, 220, 255));
        g.drawLine(x + 8, y + 10, x + 25, y + 10);
        g.drawLine(x + 8, y + 17, x + 35, y + 17);
    }

    private void drawServerRack(Graphics g, int x, int y) {
        g.setColor(new Color(42, 48, 62));
        g.fillRoundRect(x, y, 42, 80, 6, 6);

        g.setColor(new Color(95, 110, 135));
        g.drawRoundRect(x, y, 42, 80, 6, 6);

        g.setColor(new Color(80, 190, 130));
        for (int i = 0; i < 5; i++) {
            g.fillOval(x + 8, y + 10 + i * 13, 5, 5);
        }

        g.setColor(new Color(80, 140, 220));
        for (int i = 0; i < 4; i++) {
            g.drawLine(x + 20, y + 12 + i * 15, x + 34, y + 12 + i * 15);
        }
    }

    private void drawFuelTanks(Graphics g, int x, int y) {
        g.setColor(new Color(70, 95, 105));
        g.fillRoundRect(x, y, 38, 75, 16, 16);
        g.fillRoundRect(x + 48, y, 38, 75, 16, 16);

        g.setColor(new Color(130, 170, 185));
        g.drawRoundRect(x, y, 38, 75, 16, 16);
        g.drawRoundRect(x + 48, y, 38, 75, 16, 16);

        g.setColor(new Color(90, 220, 180));
        g.drawLine(x + 8, y + 55, x + 30, y + 55);
        g.drawLine(x + 56, y + 45, x + 78, y + 45);
    }

    private void drawEnginePipes(Graphics g, int x, int y) {
        g.setColor(new Color(90, 100, 115));
        g.drawLine(x, y, x + 180, y);
        g.drawLine(x, y + 18, x + 180, y + 18);
        g.drawLine(x + 60, y, x + 60, y + 55);
        g.drawLine(x + 130, y + 18, x + 130, y + 65);

        g.setColor(new Color(55, 65, 80));
        g.fillOval(x + 55, y - 4, 10, 10);
        g.fillOval(x + 125, y + 14, 10, 10);
    }

    private void drawEngineCoreBase(Graphics g, int x, int y) {
        g.setColor(new Color(45, 55, 70));
        g.fillRoundRect(x, y, 80, 42, 8, 8);

        g.setColor(new Color(90, 110, 135));
        g.drawRoundRect(x, y, 80, 42, 8, 8);

        g.setColor(new Color(80, 150, 230));
        g.drawLine(x + 12, y + 12, x + 68, y + 12);
        g.drawLine(x + 12, y + 28, x + 68, y + 28);
    }

    private void drawRoom(Graphics g, int x, int y, int width, int height, String name) {
        g.setColor(new Color(47, 56, 72));
        g.fillRoundRect(x, y, width, height, 16, 16);

        g.setColor(new Color(95, 112, 138));
        g.drawRoundRect(x, y, width, height, 16, 16);

        g.setColor(new Color(30, 38, 52));
        g.fillRoundRect(x + 8, y + 8, width - 16, 24, 10, 10);

        g.setColor(new Color(160, 190, 220));
        g.drawString(name, x + 16, y + 25);

        g.setColor(new Color(60, 70, 88));

        for (int tileX = x + 15; tileX < x + width - 15; tileX += 40) {
            g.drawLine(tileX, y + 45, tileX, y + height - 15);
        }

        for (int tileY = y + 50; tileY < y + height - 15; tileY += 40) {
            g.drawLine(x + 15, tileY, x + width - 15, tileY);
        }

        g.setColor(new Color(35, 42, 55));
        g.drawLine(x + 4, y + 36, x + 4, y + height - 8);
        g.drawLine(x + width - 5, y + 36, x + width - 5, y + height - 8);

        g.setColor(new Color(130, 145, 165));
        g.fillOval(x + 10, y + 10, 4, 4);
        g.fillOval(x + width - 15, y + 10, 4, 4);
        g.fillOval(x + 10, y + height - 15, 4, 4);
        g.fillOval(x + width - 15, y + height - 15, 4, 4);
    }

    public void interactWithNearbyObject() {
        for (GameObject object : objects) {
            if (object.isActive() && object instanceof Elevator && isPlayerNear(object)) {
                switchLevel();
                return;
            }
        }

        for (GameObject object : objects) {
            if (object.isActive() && object instanceof ShipTerminal terminal && isPlayerNear(object)) {
                lastMessage = terminal.getHint();
                return;
            }
        }

        for (GameObject object : objects) {
            if (object.isActive() && object instanceof LockedDoor door && isPlayerNear(object)) {
                unlockDoor(door);
                return;
            }
        }

        for (GameObject object : objects) {
            if (object.isActive() && object instanceof Chest chest
                    && !chest.isOpened()
                    && isPlayerNear(object)) {
                openChest(chest);
                return;
            }
        }

        for (GameObject object : objects) {
            if (object.isActive() && object instanceof RepairableObject repairable && isPlayerNear(object)) {
                repairObject(repairable, object);
                return;
            }
        }

        lastMessage = "No object nearby.";
    }

    private void unlockDoor(LockedDoor door) {
        if (!player.getInventory().containsItem(door.getRequiredModule())) {
            lastMessage = "Required module missing: " + door.getRequiredModule();
            return;
        }

        door.setActive(false);

        if (currentLevel == 1 && door.getRequiredModule().equals("Power Module")) {
            levelOneObjectiveCompleted = true;
            lastMessage = "Power Module used. Elevator activated.";
            return;
        }

        if (currentLevel == 2 && door.getRequiredModule().equals("Engine Core")) {
            lastMessage = "Engine Core used. Main engine room unlocked.";
            return;
        }

        lastMessage = door.getRequiredModule() + " used. Door unlocked.";
    }

    private boolean isPlayerNear(GameObject object) {
        Rectangle playerBounds = player.getBounds();

        Rectangle interactionArea = new Rectangle(
                playerBounds.x - 10,
                playerBounds.y - 10,
                playerBounds.width + 20,
                playerBounds.height + 20
        );

        return interactionArea.intersects(object.getBounds());
    }

    private void switchLevel() {
        if (currentLevel == 1 && !levelOneObjectiveCompleted) {
            lastMessage = "Elevator locked. Complete Engineering Deck objective first.";
            return;
        }

        if (currentLevel == 1) {
            loadLevel(2);
        } else {
            lastMessage = "All objectives completed. Finish the station repair.";
        }
    }

    private void repairObject(RepairableObject repairable, GameObject object) {
        String objectName = object.getClass().getSimpleName();

        if (repairable.isRepaired()) {
            lastMessage = objectName + " is already repaired.";
            return;
        }

        if (!player.getInventory().containsItem(repairable.getRequiredPart())) {
            lastMessage = "Missing part: " + repairable.getRequiredPart();
            return;
        }

        repairable.repair(player);

        if (repairable.isRepaired()) {
            if (currentLevel == 2 && object instanceof Generator) {
                levelTwoObjectiveCompleted = true;
                lastMessage = "Main generator repaired. Station fixed.";
                return;
            }

            lastMessage = objectName + " repaired.";
        } else {
            lastMessage = "Repairing " + objectName + ": "
                    + repairable.getRepairProgress() + "%";
        }
    }

    public boolean isLevelCompleted() {
        return levelOneObjectiveCompleted && levelTwoObjectiveCompleted;
    }

    public Player getPlayer() {
        return player;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void saveInventoryToFile() {
        String fileName = "inventory_save.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Item item : player.getInventory().getItems()) {
                writer.println(item.getName());
            }

            lastMessage = "Inventory saved to " + fileName;
        } catch (IOException e) {
            lastMessage = "Could not save inventory.";
            System.err.println("Cannot save inventory: " + e.getMessage());
        }
    }

    public void craftCurrentLevelRecipe() {
        if (currentLevel == 1) {
            craftItem("Wire", "Battery", "Power Module");
        } else if (currentLevel == 2) {
            craftItem("Metal Plate", "Fuel Cell", "Engine Core");
        }
    }

    private void craftItem(String firstItem, String secondItem, String resultItem) {
        boolean hasFirstItem = player.getInventory().containsItem(firstItem);
        boolean hasSecondItem = player.getInventory().containsItem(secondItem);

        if (!hasFirstItem || !hasSecondItem) {
            lastMessage = "Missing parts for crafting: " + firstItem + " + " + secondItem;
            return;
        }

        if (player.getInventory().containsItem(resultItem)) {
            lastMessage = resultItem + " is already crafted.";
            return;
        }

        player.getInventory().removeItem(firstItem);
        player.getInventory().removeItem(secondItem);
        player.getInventory().addItem(new SparePart(0, 0, 0, 0, resultItem));

        lastMessage = "Crafted: " + resultItem;
    }

    private void openChest(Chest chest) {
        if (chest.isOpened()) {
            lastMessage = "Chest is empty.";
            return;
        }

        if (chest.getItemName().equals("Empty")) {
            chest.open();
            lastMessage = "Chest is empty.";
            return;
        }

        Item item = createInventoryItemForChest(chest.getItemName());
        player.getInventory().addItem(item);
        chest.open();

        lastMessage = chest.getItemName() + " collected from chest.";
    }

    private Item createInventoryItemForChest(String itemName) {
        if (itemName.equals("Wrench")) {
            return new ToolItem(0, 0, 0, 0, itemName);
        }

        return new SparePart(0, 0, 0, 0, itemName);
    }
}