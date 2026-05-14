package cz.cvut.fel.pjv.spacemechanic;

import cz.cvut.fel.pjv.spacemechanic.level.LevelManager;
import cz.cvut.fel.pjv.spacemechanic.model.Engine;
import cz.cvut.fel.pjv.spacemechanic.model.Generator;
import cz.cvut.fel.pjv.spacemechanic.model.Inventory;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.SparePart;
import cz.cvut.fel.pjv.spacemechanic.model.ToolItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpaceMechanicTests {

    /**
     * Testuje pridani a odebrani predmetu z inventare.
     */
    @Test
    void inventoryShouldAddAndRemoveItem() {
        Inventory inventory = new Inventory();
        SparePart wire = new SparePart(0, 0, 10, 10, "Wire");

        inventory.addItem(wire);

        assertTrue(inventory.containsItem("Wire"));

        inventory.removeItem("Wire");

        assertFalse(inventory.containsItem("Wire"));
    }

    /**
     * Testuje sebrani soucastky hracem.
     */
    @Test
    void sparePartShouldBeCollectedByPlayer() {
        Player player = new Player(0, 0, 32, 32);
        SparePart battery = new SparePart(10, 10, 16, 16, "Battery");

        battery.applyEffect(player);

        assertTrue(player.getInventory().containsItem("Battery"));
        assertFalse(battery.isActive());
    }

    /**
     * Testuje sebrani nastroje a jeho nastaveni jako aktivniho.
     */
    @Test
    void toolShouldBeCollectedAndUsedByPlayer() {
        Player player = new Player(0, 0, 32, 32);
        ToolItem wrench = new ToolItem(10, 10, 16, 16, "Wrench");

        wrench.applyEffect(player);

        assertTrue(player.getInventory().containsItem("Wrench"));
        assertTrue(player.hasTool("Wrench"));
        assertFalse(wrench.isActive());
    }

    /**
     * Testuje, ze objekt bez potrebne soucastky nejde opravit.
     */
    @Test
    void repairShouldNotWorkWithoutRequiredPart() {
        Player player = new Player(0, 0, 32, 32);
        Engine engine = new Engine(0, 0, 40, 40, "Engine Core");

        engine.repair(player);

        assertEquals(0, engine.getRepairProgress());
        assertFalse(engine.isRepaired());
    }

    /**
     * Testuje postupnou opravu objektu se spravnou soucastkou.
     */
    @Test
    void repairShouldReachOneHundredPercent() {
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
     * Testuje nacteni prvniho a druheho levelu.
     */
    @Test
    void levelManagerShouldLoadLevels() {
        LevelManager levelManager = new LevelManager();

        assertEquals(1, levelManager.getCurrentLevel());
        assertFalse(levelManager.getObjects().isEmpty());

        levelManager.loadLevel(2);

        assertEquals(2, levelManager.getCurrentLevel());
        assertFalse(levelManager.getObjects().isEmpty());

        boolean hasGenerator = levelManager.getObjects().stream()
                .anyMatch(object -> object instanceof Generator);

        assertTrue(hasGenerator);
    }

    /**
     * Testuje crafting receptu v prvnim levelu.
     */
    @Test
    void craftingShouldCreatePowerModuleInLevelOne() {
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
     * Testuje crafting receptu ve druhem levelu.
     */
    @Test
    void craftingShouldCreateEngineCoreInLevelTwo() {
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
}