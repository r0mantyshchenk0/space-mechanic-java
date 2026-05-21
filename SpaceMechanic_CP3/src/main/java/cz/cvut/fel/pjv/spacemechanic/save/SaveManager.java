package cz.cvut.fel.pjv.spacemechanic.save;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Handles saving and loading of the basic game state.
 */
public class SaveManager {

    private static final Logger LOGGER =
            Logger.getLogger(SaveManager.class.getName());

    private static final Path SAVE_FILE = Path.of("savegame.txt");

    /**
     * Saves the current game state into a text file.
     *
     * @param currentLevel current level number
     * @param playerX player x position
     * @param playerY player y position
     * @param inventoryItems names of items in inventory
     * @param repairedObjects names of repaired objects
     */
    public void saveGame(
            int currentLevel,
            int playerX,
            int playerY,
            Set<String> inventoryItems,
            Set<String> repairedObjects
    ) {
        try (BufferedWriter writer = Files.newBufferedWriter(SAVE_FILE)) {
            writer.write("level=" + currentLevel);
            writer.newLine();

            writer.write("playerX=" + playerX);
            writer.newLine();

            writer.write("playerY=" + playerY);
            writer.newLine();

            writer.write("inventory=" + String.join(",", inventoryItems));
            writer.newLine();

            writer.write("repaired=" + String.join(",", repairedObjects));
            writer.newLine();

            LOGGER.info("Game state saved to " + SAVE_FILE);
        } catch (IOException e) {
            LOGGER.warning("Could not save game state: " + e.getMessage());
        }
    }

    /**
     * Loads the saved game state from a text file.
     *
     * @return loaded game state
     */
    public SaveData loadGame() {
        SaveData saveData = new SaveData();

        if (!Files.exists(SAVE_FILE)) {
            LOGGER.info("Save file does not exist.");
            return saveData;
        }

        try (BufferedReader reader = Files.newBufferedReader(SAVE_FILE)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);

                if (parts.length != 2) {
                    continue;
                }

                String key = parts[0];
                String value = parts[1];

                switch (key) {
                    case "level" -> saveData.currentLevel = parseInt(value, 1);
                    case "playerX" -> saveData.playerX = parseInt(value, 0);
                    case "playerY" -> saveData.playerY = parseInt(value, 0);
                    case "inventory" -> saveData.inventoryItems = parseSet(value);
                    case "repaired" -> saveData.repairedObjects = parseSet(value);
                    default -> LOGGER.info("Unknown save key: " + key);
                }
            }

            LOGGER.info("Game state loaded from " + SAVE_FILE);
        } catch (IOException e) {
            LOGGER.warning("Could not load game state: " + e.getMessage());
        }

        return saveData;
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid number in save file: " + value + ". Using default: " + defaultValue);
            return defaultValue;
        }
    }

    private Set<String> parseSet(String value) {
        Set<String> result = new LinkedHashSet<>();

        if (value == null || value.isBlank()) {
            return result;
        }

        String[] items = value.split(",");

        for (String item : items) {
            String trimmed = item.trim();

            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }

        return result;
    }

    /**
     * Data object for the loaded game state.
     */
    public static class SaveData {

        private int currentLevel = 1;
        private int playerX = 0;
        private int playerY = 0;
        private Set<String> inventoryItems = new LinkedHashSet<>();
        private Set<String> repairedObjects = new LinkedHashSet<>();

        public int getCurrentLevel() {
            return currentLevel;
        }

        public int getPlayerX() {
            return playerX;
        }

        public int getPlayerY() {
            return playerY;
        }

        public Set<String> getInventoryItems() {
            return inventoryItems;
        }

        public Set<String> getRepairedObjects() {
            return repairedObjects;
        }
    }
}