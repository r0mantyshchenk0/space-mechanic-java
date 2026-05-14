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
 * Vykresluje menu, HUD, inventar, crafting a koncove obrazovky.
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
     * Vykresli UI podle aktualniho stavu hry.
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
     * Prepne zobrazeni inventare.
     */
    public void toggleInventory() {
        inventoryVisible = !inventoryVisible;

        // Inventar a crafting menu se nezobrazuji zaroven
        if (inventoryVisible) {
            craftingMenuVisible = false;
        }
    }

    /**
     * Prepne zobrazeni crafting menu.
     */
    public void toggleCraftingMenu() {
        craftingMenuVisible = !craftingMenuVisible;

        if (craftingMenuVisible) {
            inventoryVisible = false;
        }
    }

    public boolean isCraftingMenuVisible() {
        return craftingMenuVisible;
    }

    /**
     * Vykresli uvodni menu hry.
     */
    public void drawMenu(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("SPACE MECHANIC", 330, 200);
        g.drawString("Press any key to start", 330, 240);
    }

    /**
     * Vykresli hlavni herni panel s informacemi.
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
     * Vykresli stav opravitelnych objektu v aktualnim levelu.
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
     * Vykresli obsah inventare hrace.
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
     * Vykresli crafting menu s receptem aktualniho levelu.
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

        // Recept se meni podle aktualniho levelu
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
     * Vykresli obrazovku pauzy.
     */
    public void drawPause(Graphics g) {
        g.setColor(new Color(20, 24, 32, 220));
        g.fillRoundRect(250, 170, 300, 120, 20, 20);

        g.setColor(Color.WHITE);
        g.drawString("PAUSED", 370, 220);
        g.drawString("Press ESC to continue", 320, 250);
    }

    /**
     * Vykresli obrazovku prohry.
     */
    public void drawGameOver(Graphics g) {
        g.setColor(new Color(40, 20, 20, 220));
        g.fillRoundRect(250, 170, 300, 120, 20, 20);

        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", 350, 220);
    }

    /**
     * Vykresli obrazovku vyhry.
     */
    public void drawWin(Graphics g) {
        g.setColor(new Color(20, 40, 25, 220));
        g.fillRoundRect(240, 160, 330, 140, 20, 20);

        g.setColor(Color.WHITE);
        g.drawString("YOU WIN", 365, 215);
        g.drawString("All ship systems are repaired.", 300, 245);
    }
}