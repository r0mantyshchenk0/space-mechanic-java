package cz.cvut.fel.pjv.spacemechanic.level;

import cz.cvut.fel.pjv.spacemechanic.collision.CollisionManager;
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

    public LevelManager() {
        this.currentLevel = 1;

        this.objects = new ArrayList<>();
        this.levelOneObjects = new ArrayList<>();
        this.levelTwoObjects = new ArrayList<>();

        this.player = new Player(100, 100, 32, 32);
        this.collisionManager = new CollisionManager();
        this.lastMessage = "Find parts and repair ship systems.";

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

        throw new IllegalArgumentException("Unknown object type: " + type);
    }

    public void loadLevel(int level) {
        objects.clear();
        currentLevel = level;

        if (level == 1) {
            objects.addAll(levelOneObjects);
            player.setX(510);
            player.setY(260);
            lastMessage = "Level 1: main deck.";
        } else if (level == 2) {
            objects.addAll(levelTwoObjects);
            player.setX(510);
            player.setY(260);
            lastMessage = "Level 2: upper deck.";
        }
    }

    public void update() {
        player.update();

        for (GameObject object : objects) {
            if (object.isActive()) {
                object.update();
            }
        }

        collisionManager.checkCollisions(player, objects);
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

        // Small stars in the background
        g.setColor(new Color(70, 80, 110));
        for (int i = 0; i < 35; i++) {
            int starX = 390 + (i * 73) % 760;
            int starY = 35 + (i * 41) % 690;
            g.fillOval(starX, starY, 2, 2);
        }

        // Main dark ship interior
        g.setColor(new Color(32, 38, 50));
        g.fillRoundRect(shipX, shipY, shipWidth, shipHeight, 28, 28);

        // Outer ship border
        g.setColor(new Color(115, 135, 160));
        g.drawRoundRect(shipX, shipY, shipWidth, shipHeight, 28, 28);

        // Wall tiles - top and bottom
        drawTileRow(g, shipX + 25, shipY + 15, shipWidth - 50);
        drawTileRow(g, shipX + 25, shipY + shipHeight - 45, shipWidth - 50);

        // Wall tiles - left and right
        drawTileColumn(g, shipX + 18, shipY + 50, shipHeight - 100);
        drawTileColumn(g, shipX + shipWidth - 48, shipY + 50, shipHeight - 100);

        // Floor grid
        g.setColor(new Color(55, 64, 78));

        for (int x = shipX + 40; x < shipX + shipWidth - 40; x += 50) {
            g.drawLine(x, shipY + 60, x, shipY + shipHeight - 70);
        }

        for (int y = shipY + 70; y < shipY + shipHeight - 60; y += 50) {
            g.drawLine(shipX + 45, y, shipX + shipWidth - 45, y);
        }

        // Large technical wall panels
        g.setColor(new Color(74, 86, 104));
        g.fillRoundRect(410, 110, 170, 70, 14, 14);
        g.fillRoundRect(980, 110, 170, 70, 14, 14);
        g.fillRoundRect(410, 500, 170, 70, 14, 14);
        g.fillRoundRect(980, 500, 170, 70, 14, 14);

        // Panel highlights
        g.setColor(new Color(95, 110, 132));
        g.drawLine(425, 128, 560, 128);
        g.drawLine(995, 128, 1130, 128);
        g.drawLine(425, 518, 560, 518);
        g.drawLine(995, 518, 1130, 518);

        // Windows
        g.setColor(new Color(18, 28, 55));
        g.fillRoundRect(610, 120, 50, 30, 10, 10);
        g.fillRoundRect(690, 120, 50, 30, 10, 10);

        g.setColor(new Color(90, 150, 220));
        g.drawRoundRect(610, 120, 50, 30, 10, 10);
        g.drawRoundRect(690, 120, 50, 30, 10, 10);

        // Small light strips
        g.setColor(new Color(80, 140, 210));
        g.drawLine(615, 154, 655, 154);
        g.drawLine(695, 154, 735, 154);

        // Decorative pipes
        g.setColor(new Color(85, 95, 110));
        g.drawLine(430, 235, 570, 235);
        g.drawLine(570, 235, 570, 280);
        g.drawLine(1040, 235, 1120, 235);
        g.drawLine(1040, 235, 1040, 300);

        // Level title
        g.setColor(Color.WHITE);

        if (currentLevel == 1) {
            g.drawString("MAIN DECK", 740, 110);
        } else {
            g.drawString("UPPER DECK", 735, 110);
        }
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

        // Repairable objects are handled after elevator and terminal
        for (GameObject object : objects) {
            if (object.isActive() && object instanceof RepairableObject repairable && isPlayerNear(object)) {
                repairObject(repairable, object);
                return;
            }
        }

        lastMessage = "No object nearby.";
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
        if (currentLevel == 1) {
            loadLevel(2);
        } else {
            loadLevel(1);
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
        return areObjectsRepaired(levelOneObjects) && areObjectsRepaired(levelTwoObjects);
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
}