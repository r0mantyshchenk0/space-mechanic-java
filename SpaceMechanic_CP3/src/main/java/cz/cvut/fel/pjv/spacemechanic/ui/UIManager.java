package cz.cvut.fel.pjv.spacemechanic.ui;

import cz.cvut.fel.pjv.spacemechanic.main.Game;
import cz.cvut.fel.pjv.spacemechanic.main.GameState;
import cz.cvut.fel.pjv.spacemechanic.model.GameObject;
import cz.cvut.fel.pjv.spacemechanic.model.Item;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.RepairableObject;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Renders the game user interface.
 * Handles HUD, inventory, crafting menu, pause and win screen.
 */
public class UIManager {

    private final Game game;

    private boolean inventoryVisible;
    private boolean craftingMenuVisible;

    public UIManager(Game game) {
        this.game = game;
        this.inventoryVisible = false;
        this.craftingMenuVisible = false;
    }

    /**
     * Renders the UI according to the current game state.
     *
     * @param g graphics context
     */
    public void render(Graphics g) {
        GameState state = game.getCurrentState();

        if (state == GameState.MENU) {
            drawMenu(g);
        } else if (state == GameState.PLAYING) {
            drawHUD(g);

            if (inventoryVisible) {
                drawInventory(g);
            }

            if (craftingMenuVisible) {
                drawCraftingMenu(g);
            }
        } else if (state == GameState.PAUSED) {
            drawPause(g);
        } else if (state == GameState.GAME_OVER) {
            drawGameOver(g);
        } else if (state == GameState.WIN) {
            drawWin(g);
        }
    }

    /**
     * Toggles inventory visibility.
     */
    public void toggleInventory() {
        inventoryVisible = !inventoryVisible;

        // Inventory and crafting menu are not shown at the same time
        if (inventoryVisible) {
            craftingMenuVisible = false;
        }
    }

    /**
     * Toggles crafting menu visibility.
     */
    public void toggleCraftingMenu() {
        craftingMenuVisible = !craftingMenuVisible;

        if (craftingMenuVisible) {
            inventoryVisible = false;
        }
    }
    /**
     * Returns whether the crafting menu is open.
     *
     * @return true if the crafting menu is visible
     */
    public boolean isCraftingMenuVisible() {
        return craftingMenuVisible;
    }

    /**
     * Renders the main menu screen.
     */
    public void drawMenu(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("SPACE MECHANIC", 330, 200);
        g.drawString("Press any key to start", 330, 240);
    }

    /**
     * Renders the main in-game HUD.
     *
     * @param g graphics context
     */
    public void drawHUD(Graphics g) {
        Player player = game.getLevelManager().getPlayer();

        int panelX = 15;
        int panelY = 15;
        int panelWidth = 330;
        int panelHeight = 245;

        g.setColor(new Color(20, 24, 32, 210));
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

        g.setColor(new Color(90, 110, 140));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

        int x = panelX + 18;
        int y = panelY + 25;

        g.setColor(Color.WHITE);
        g.drawString("SPACE MECHANIC", x, y);

        y += 25;
        g.drawString("Health: " + player.getHealth(), x, y);

        y += 20;
        g.drawString("Level: " + game.getLevelManager().getCurrentLevel(), x, y);

        y += 20;
        g.drawString("Items: " + player.getInventory().getItems().size(), x, y);

        y += 30;
        g.drawString("Controls:", x, y);

        y += 20;
        g.drawString("WASD - move", x + 10, y);

        y += 20;
        g.drawString("E - interact", x + 10, y);

        y += 20;
        g.drawString("I - inventory, C - crafting", x + 10, y);

        y += 20;
        g.drawString("ESC - pause", x + 10, y);

        y += 30;
        g.setColor(new Color(180, 200, 230));
        g.drawString("Message:", x, y);

        y += 20;
        g.setColor(Color.WHITE);
        g.drawString(game.getLevelManager().getLastMessage(), x + 10, y);

        drawRepairStatus(g, panelX, panelY + panelHeight + 15);
    }

    /**
     * Renders the repair status of objects in the current level.
     */
    private void drawRepairStatus(Graphics g, int panelX, int panelY) {
        int panelWidth = 330;
        int panelHeight = 155;

        g.setColor(new Color(20, 24, 32, 210));
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

        g.setColor(new Color(90, 110, 140));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

        int x = panelX + 18;
        int y = panelY + 25;

        g.setColor(Color.WHITE);
        g.drawString("REPAIR STATUS", x, y);

        y += 25;

        for (GameObject object : game.getLevelManager().getObjects()) {
            if (object instanceof RepairableObject repairable) {
                String name = object.getClass().getSimpleName();
                String status;

                if (repairable.isRepaired()) {
                    status = "repaired";
                    g.setColor(new Color(120, 220, 140));
                } else {
                    status = repairable.getRepairProgress() + "%, needs "
                            + repairable.getRequiredPart();
                    g.setColor(new Color(230, 210, 120));
                }

                g.drawString(name + " - " + status, x + 10, y);
                y += 22;
            }
        }
    }

    /**
     * Renders the player inventory content.
     *
     * @param g graphics context
     */
    private void drawInventory(Graphics g) {
        Player player = game.getLevelManager().getPlayer();

        int panelX = 930;
        int panelY = 80;
        int panelWidth = 300;
        int panelHeight = 360;

        g.setColor(new Color(20, 24, 32, 225));
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

        g.setColor(new Color(90, 110, 140));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

        int x = panelX + 18;
        int y = panelY + 30;

        g.setColor(Color.WHITE);
        g.drawString("INVENTORY", x, y);

        y += 30;

        if (player.getInventory().getItems().isEmpty()) {
            g.drawString("- empty", x + 10, y);
            return;
        }

        for (Item item : player.getInventory().getItems()) {
            g.drawString("- " + item.getName(), x + 10, y);
            y += 22;
        }
    }

    /**
     * Renders the crafting menu with the recipe for the current level.
     *
     * @param g graphics context
     */
    private void drawCraftingMenu(Graphics g) {
        int panelX = 930;
        int panelY = 460;
        int panelWidth = 300;
        int panelHeight = 180;

        g.setColor(new Color(20, 24, 32, 225));
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

        g.setColor(new Color(90, 110, 140));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

        int x = panelX + 18;
        int y = panelY + 30;

        g.setColor(Color.WHITE);
        g.drawString("CRAFTING", x, y);

        y += 30;

        // The recipe changes according to the current level
        if (game.getLevelManager().getCurrentLevel() == 1) {
            g.drawString("Recipe:", x, y);
            y += 25;
            g.drawString("Wire + Battery", x + 10, y);
            y += 25;
            g.drawString("=> Power Module", x + 10, y);
        } else {
            g.drawString("Recipe:", x, y);
            y += 25;
            g.drawString("Metal Plate + Fuel Cell", x + 10, y);
            y += 25;
            g.drawString("=> Engine Core", x + 10, y);
        }

        y += 35;
        g.drawString("ENTER - craft", x, y);
    }

    /**
     * Renders the pause screen.
     */
    public void drawPause(Graphics g) {
        g.setColor(new Color(20, 24, 32, 220));
        g.fillRoundRect(250, 170, 300, 120, 20, 20);

        g.setColor(Color.WHITE);
        g.drawString("PAUSED", 370, 220);
        g.drawString("Press ESC to continue", 320, 250);
    }

    /**
     * Renders the game over screen.
     */
    public void drawGameOver(Graphics g) {
        g.setColor(new Color(40, 20, 20, 220));
        g.fillRoundRect(250, 170, 300, 120, 20, 20);

        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", 350, 220);
    }

    /**
     * Renders the win screen.
     */
    public void drawWin(Graphics g) {
        g.setColor(new Color(20, 40, 25, 220));
        g.fillRoundRect(240, 160, 330, 140, 20, 20);

        g.setColor(Color.WHITE);
        g.drawString("YOU WIN", 365, 215);
        g.drawString("All ship systems are repaired.", 300, 245);
    }
}