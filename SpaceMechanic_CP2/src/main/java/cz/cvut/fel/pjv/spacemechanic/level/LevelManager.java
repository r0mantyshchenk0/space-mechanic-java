package cz.cvut.fel.pjv.spacemechanic.level;

import cz.cvut.fel.pjv.spacemechanic.collision.CollisionManager;
import cz.cvut.fel.pjv.spacemechanic.model.DoorSystem;
import cz.cvut.fel.pjv.spacemechanic.model.Elevator;
import cz.cvut.fel.pjv.spacemechanic.model.Engine;
import cz.cvut.fel.pjv.spacemechanic.model.GameObject;
import cz.cvut.fel.pjv.spacemechanic.model.Generator;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.RepairableObject;
import cz.cvut.fel.pjv.spacemechanic.model.SparePart;
import cz.cvut.fel.pjv.spacemechanic.model.ToolItem;
import cz.cvut.fel.pjv.spacemechanic.model.Item;
import cz.cvut.fel.pjv.spacemechanic.model.ShipTerminal;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        player.render(g);

        for (GameObject object : objects) {
            if (object.isActive()) {
                object.render(g);
            }
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