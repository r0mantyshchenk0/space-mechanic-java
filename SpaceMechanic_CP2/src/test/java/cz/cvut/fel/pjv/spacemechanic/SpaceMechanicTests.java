package cz.cvut.fel.pjv.spacemechanic;

import cz.cvut.fel.pjv.spacemechanic.level.LevelManager;
import cz.cvut.fel.pjv.spacemechanic.model.Engine;
import cz.cvut.fel.pjv.spacemechanic.model.Generator;
import cz.cvut.fel.pjv.spacemechanic.model.Inventory;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.SparePart;
import cz.cvut.fel.pjv.spacemechanic.model.ToolItem;
import cz.cvut.fel.pjv.spacemechanic.save.SaveManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the main non-graphical logic of Space Mechanic.
 */
class SpaceMechanicTests {

    @AfterEach
    void cleanGeneratedSaveFile() throws Exception {
        Files.deleteIfExists(Path.of("savegame.txt"));
    }

    /**
     * Checks that an item can be added to the inventory and removed later.
     */
    @Test
    void inventoryCanAddAndRemoveWire() {
        Inventory inventory = new Inventory();
        SparePart wire = new SparePart(0, 0, 10, 10, "Wire");

        inventory.addItem(wire);

        assertTrue(inventory.containsItem("Wire"));

        inventory.removeItem("Wire");

        assertFalse(inventory.containsItem("Wire"));
    }

    /**
     * Checks that a collected spare part is stored in the player's inventory.
     */
    @Test
    void collectedSparePartGoesToPlayerInventory() {
        Player player = new Player(0, 0, 32, 32);
        SparePart battery = new SparePart(10, 10, 16, 16, "Battery");

        battery.applyEffect(player);

        assertTrue(player.getInventory().containsItem("Battery"));
        assertFalse(battery.isActive());
    }

    /**
     * Checks that a collected tool is available for later use.
     */
    @Test
    void collectedToolIsAvailableForPlayer() {
        Player player = new Player(0, 0, 32, 32);
        ToolItem wrench = new ToolItem(10, 10, 16, 16, "Wrench");

        wrench.applyEffect(player);

        assertTrue(player.getInventory().containsItem("Wrench"));
        assertTrue(player.hasTool("Wrench"));
        assertFalse(wrench.isActive());
    }

    /**
     * The engine must not be repaired when the required part is missing.
     */
    @Test
    void engineStaysBrokenWithoutEngineCore() {
        Player player = new Player(0, 0, 32, 32);
        Engine engine = new Engine(0, 0, 40, 40, "Engine Core");

        engine.repair(player);

        assertEquals(0, engine.getRepairProgress());
        assertFalse(engine.isRepaired());
    }

    /**
     * The engine should become repaired after several repair actions
     * with the correct part in the inventory.
     */
    @Test
    void engineCanBeFullyRepairedWithEngineCore() {
        Player player = new Player(0, 0, 32, 32);
        Engine engine = new Engine(0, 0, 40, 40, "Engine Core");

        player.getInventory().addItem(
                new SparePart(0, 0, 0, 0, "Engine Core")
        );

        engine.repair(player);
        engine.repair(player);
        engine.repair(player);
        engine.repair(player);

        assertEquals(100, engine.getRepairProgress());
        assertTrue(engine.isRepaired());
    }

    /**
     * Checks that the level manager starts on level one with objects loaded.
     */
    @Test
    void levelManagerStartsWithFirstLevelLoaded() {
        LevelManager levelManager = new LevelManager();

        assertEquals(1, levelManager.getCurrentLevel());
        assertNotNull(levelManager.getPlayer());
        assertFalse(levelManager.getObjects().isEmpty());
        assertFalse(levelManager.isLevelCompleted());
    }

    /**
     * Checks that the second level can be loaded and contains a generator.
     */
    @Test
    void secondLevelContainsGeneratorAfterLoading() {
        LevelManager levelManager = new LevelManager();

        levelManager.loadLevel(2);

        assertEquals(2, levelManager.getCurrentLevel());
        assertFalse(levelManager.getObjects().isEmpty());

        boolean generatorFound = levelManager.getObjects().stream()
                .anyMatch(object -> object instanceof Generator);

        assertTrue(generatorFound);
    }

    /**
     * Level one recipe should create a Power Module from Wire and Battery.
     */
    @Test
    void levelOneCraftingCreatesPowerModule() {
        LevelManager levelManager = new LevelManager();
        Player player = levelManager.getPlayer();

        player.getInventory().addItem(
                new SparePart(0, 0, 0, 0, "Wire")
        );
        player.getInventory().addItem(
                new SparePart(0, 0, 0, 0, "Battery")
        );

        levelManager.craftCurrentLevelRecipe();

        assertTrue(player.getInventory().containsItem("Power Module"));
        assertFalse(player.getInventory().containsItem("Wire"));
        assertFalse(player.getInventory().containsItem("Battery"));
    }

    /**
     * Level two recipe should create an Engine Core from Metal Plate and Fuel Cell.
     */
    @Test
    void levelTwoCraftingCreatesEngineCore() {
        LevelManager levelManager = new LevelManager();
        levelManager.loadLevel(2);

        Player player = levelManager.getPlayer();

        player.getInventory().addItem(
                new SparePart(0, 0, 0, 0, "Metal Plate")
        );
        player.getInventory().addItem(
                new SparePart(0, 0, 0, 0, "Fuel Cell")
        );

        levelManager.craftCurrentLevelRecipe();

        assertTrue(player.getInventory().containsItem("Engine Core"));
        assertFalse(player.getInventory().containsItem("Metal Plate"));
        assertFalse(player.getInventory().containsItem("Fuel Cell"));
    }

    /**
     * Crafting must not create a module when the required parts are missing.
     */
    @Test
    void craftingDoesNothingWithoutRequiredParts() {
        LevelManager levelManager = new LevelManager();
        Player player = levelManager.getPlayer();

        levelManager.craftCurrentLevelRecipe();

        assertFalse(player.getInventory().containsItem("Power Module"));
    }

    /**
     * The same recipe should not create the result twice after the parts are consumed.
     */
    @Test
    void craftingCannotUseTheSamePartsTwice() {
        LevelManager levelManager = new LevelManager();
        Player player = levelManager.getPlayer();

        player.getInventory().addItem(
                new SparePart(0, 0, 0, 0, "Wire")
        );
        player.getInventory().addItem(
                new SparePart(0, 0, 0, 0, "Battery")
        );

        levelManager.craftCurrentLevelRecipe();
        levelManager.craftCurrentLevelRecipe();

        long powerModuleCount = player.getInventory().getItems().stream()
                .filter(item -> item.getName().equals("Power Module"))
                .count();

        assertEquals(1, powerModuleCount);
    }

    /**
     * SaveManager should store and load the basic game state values.
     */
    @Test
    void saveManagerStoresBasicGameState() {
        SaveManager saveManager = new SaveManager();

        Set<String> inventoryItems = new LinkedHashSet<>();
        inventoryItems.add("Wire");
        inventoryItems.add("Battery");

        Set<String> repairedObjects = new LinkedHashSet<>();
        repairedObjects.add("Engine:100:200");

        saveManager.saveGame(
                2,
                345,
                210,
                inventoryItems,
                repairedObjects
        );

        SaveManager.SaveData saveData = saveManager.loadGame();

        assertEquals(2, saveData.getCurrentLevel());
        assertEquals(345, saveData.getPlayerX());
        assertEquals(210, saveData.getPlayerY());
        assertTrue(saveData.getInventoryItems().contains("Wire"));
        assertTrue(saveData.getInventoryItems().contains("Battery"));
        assertTrue(saveData.getRepairedObjects().contains("Engine:100:200"));
    }
}