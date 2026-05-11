package cz.cvut.fel.pjv.spacemechanic.level;

import cz.cvut.fel.pjv.spacemechanic.collision.CollisionManager;
import cz.cvut.fel.pjv.spacemechanic.model.LockedDoor;
import cz.cvut.fel.pjv.spacemechanic.model.Wall;
import cz.cvut.fel.pjv.spacemechanic.model.DoorSystem;
import cz.cvut.fel.pjv.spacemechanic.model.Elevator;
import cz.cvut.fel.pjv.spacemechanic.model.Engine;
import cz.cvut.fel.pjv.spacemechanic.model.GameObject;
import cz.cvut.fel.pjv.spacemechanic.model.Generator;
import cz.cvut.fel.pjv.spacemechanic.model.Item;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.RepairableObject;
import cz.cvut.fel.pjv.spacemechanic.model.ShipTerminal;
import cz.cvut.fel.pjv.spacemechanic.model.SparePart;
import cz.cvut.fel.pjv.spacemechanic.model.ToolItem;

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

        // Solid objects block player movement
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

        drawStars(g);

        // Main ship body
        if (currentLevel == 1) {
            g.setColor(new Color(30, 36, 48));
        } else {
            g.setColor(new Color(28, 32, 46));
        }

        g.fillRoundRect(shipX, shipY, shipWidth, shipHeight, 28, 28);

        g.setColor(new Color(115, 135, 160));
        g.drawRoundRect(shipX, shipY, shipWidth, shipHeight, 28, 28);

        // Inner floor
        if (currentLevel == 1) {
            g.setColor(new Color(39, 46, 60));
        } else {
            g.setColor(new Color(36, 42, 62));
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

        // Corridors
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

        // Main title
        g.setColor(Color.WHITE);

        if (currentLevel == 1) {
            g.drawString("ENGINEERING DECK", 720, 110);
        } else {
            g.drawString("CONTROL DECK", 735, 110);
        }

        // Decorative pipes
        g.setColor(new Color(88, 98, 115));
        g.drawLine(430, 585, 1120, 585);
        g.drawLine(430, 595, 1120, 595);

        // Energy line color differs by level
        if (currentLevel == 1) {
            g.setColor(new Color(80, 150, 230));
        } else {
            g.setColor(new Color(160, 110, 230));
        }

        g.drawLine(460, 590, 570, 590);
        g.drawLine(690, 590, 830, 590);
        g.drawLine(950, 590, 1080, 590);
    }


    private void drawStars(Graphics g) {
        g.setColor(new Color(75, 85, 115));

        for (int i = 0; i < 45; i++) {
            int starX = 385 + (i * 79) % 790;
            int starY = 25 + (i * 43) % 680;

            if (starX < 350 || starX > 1220 || starY > 700) {
                continue;
            }

            g.fillOval(starX, starY, 2, 2);
        }
    }

    private void drawRoom(Graphics g, int x, int y, int width, int height, String name) {
        // Room floor
        g.setColor(new Color(47, 56, 72));
        g.fillRoundRect(x, y, width, height, 16, 16);

        // Room border
        g.setColor(new Color(90, 105, 128));
        g.drawRoundRect(x, y, width, height, 16, 16);

        // Top label bar
        g.setColor(new Color(34, 42, 56));
        g.fillRoundRect(x + 8, y + 8, width - 16, 24, 10, 10);

        g.setColor(new Color(150, 170, 195));
        g.drawString(name, x + 16, y + 25);

        // Tile-like floor details
        g.setColor(new Color(60, 70, 88));

        for (int tileX = x + 15; tileX < x + width - 15; tileX += 40) {
            g.drawLine(tileX, y + 45, tileX, y + height - 15);
        }

        for (int tileY = y + 50; tileY < y + height - 15; tileY += 40) {
            g.drawLine(x + 15, tileY, x + width - 15, tileY);
        }

        // Small corner bolts
        g.setColor(new Color(120, 135, 155));
        g.fillOval(x + 10, y + 10, 4, 4);
        g.fillOval(x + width - 15, y + 10, 4, 4);
        g.fillOval(x + 10, y + height - 15, 4, 4);
        g.fillOval(x + width - 15, y + height - 15, 4, 4);
    }


    private void drawTileRow(Graphics g, int startX, int y, int width) {
        int tileSize = 32;

        for (int x = startX; x < startX + width; x += tileSize) {
            g.setColor(new Color(48, 57, 72));
            g.fillRect(x, y, tileSize, tileSize);

            g.setColor(new Color(70, 82, 100));
            g.drawRect(x, y, tileSize, tileSize);

            g.setColor(new Color(35, 42, 55));
            g.drawLine(x + 4, y + tileSize - 5, x + tileSize - 5, y + tileSize - 5);
        }
    }

    private void drawTileColumn(Graphics g, int x, int startY, int height) {
        int tileSize = 32;

        for (int y = startY; y < startY + height; y += tileSize) {
            g.setColor(new Color(48, 57, 72));
            g.fillRect(x, y, tileSize, tileSize);

            g.setColor(new Color(70, 82, 100));
            g.drawRect(x, y, tileSize, tileSize);

            g.setColor(new Color(35, 42, 55));
            g.drawLine(x + 5, y + 4, x + 5, y + tileSize - 5);
        }
    }

    public void interactWithNearbyObject() {
        // Elevator has the highest priority
        for (GameObject object : objects) {
            if (object.isActive() && object instanceof Elevator && isPlayerNear(object)) {
                switchLevel();
                return;
            }
        }

        // Terminal has priority over repairable objects
        for (GameObject object : objects) {
            if (object.isActive() && object instanceof ShipTerminal terminal && isPlayerNear(object)) {
                lastMessage = terminal.getHint();
                return;
            }
        }

        // Locked doors are handled before normal repair objects
        for (GameObject object : objects) {
            if (object.isActive() && object instanceof LockedDoor door && isPlayerNear(object)) {
                unlockDoor(door);
                return;
            }
        }

        // Repairable objects are handled after elevator, terminal and locked doors
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
            levelTwoObjectiveCompleted = true;
            lastMessage = "Engine Core installed. Station repaired.";
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
            lastMessage = objectName + " repaired.";
        } else {
            lastMessage = "Repairing " + objectName + ": "
                    + repairable.getRepairProgress() + "%";
        }
    }

    public boolean isLevelCompleted() {
        return levelOneObjectiveCompleted && levelTwoObjectiveCompleted;
    }

    private boolean areObjectsRepaired(List<GameObject> levelObjects) {
        for (GameObject object : levelObjects) {
            if (object instanceof RepairableObject repairable && !repairable.isRepaired()) {
                return false;
            }
        }

        return true;
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
}