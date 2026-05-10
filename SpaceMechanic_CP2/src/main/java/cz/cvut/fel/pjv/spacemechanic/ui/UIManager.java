package cz.cvut.fel.pjv.spacemechanic.ui;

import cz.cvut.fel.pjv.spacemechanic.main.Game;
import cz.cvut.fel.pjv.spacemechanic.main.GameState;
import cz.cvut.fel.pjv.spacemechanic.model.GameObject;
import cz.cvut.fel.pjv.spacemechanic.model.Item;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.RepairableObject;

import java.awt.Color;
import java.awt.Graphics;

public class UIManager {

    private final Game game;

    public UIManager(Game game) {
        this.game = game;
    }

    public void render(Graphics g) {
        GameState state = game.getCurrentState();

        if (state == GameState.MENU) {
            drawMenu(g);
        } else if (state == GameState.PLAYING) {
            drawHUD(g);
        } else if (state == GameState.PAUSED) {
            drawPause(g);
        } else if (state == GameState.GAME_OVER) {
            drawGameOver(g);
        } else if (state == GameState.WIN) {
            drawWin(g);
        }
    }

    public void drawMenu(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("SPACE MECHANIC", 330, 200);
        g.drawString("Press any key to start", 330, 240);
    }

    public void drawHUD(Graphics g) {
        Player player = game.getLevelManager().getPlayer();

        int panelX = 15;
        int panelY = 15;
        int panelWidth = 330;
        int panelHeight = 280;

        // Draw dark HUD panel background
        g.setColor(new Color(20, 24, 32, 210));
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

        // Draw HUD panel border
        g.setColor(new Color(90, 110, 140));
        g.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

        int x = panelX + 18;
        int y = panelY + 25;

        g.setColor(Color.WHITE);
        g.drawString("SPACE MECHANIC", x, y);

        y += 25;
        g.drawString("Health: " + player.getHealth(), x, y);

        y += 20;
        g.drawString("Items: " + player.getInventory().getItems().size(), x, y);

        y += 25;
        g.drawString("Inventory:", x, y);

        if (player.getInventory().getItems().isEmpty()) {
            y += 20;
            g.drawString("- empty", x + 10, y);
        } else {
            for (Item item : player.getInventory().getItems()) {
                y += 20;
                g.drawString("- " + item.getName(), x + 10, y);
            }
        }

        y += 30;
        g.drawString("Controls:", x, y);

        y += 20;
        g.drawString("E - interact / repair", x + 10, y);

        y += 20;
        g.drawString("P / ESC - pause", x + 10, y);

        y += 30;
        g.drawString("Message:", x, y);

        y += 20;
        g.drawString(game.getLevelManager().getLastMessage(), x + 10, y);

        drawRepairStatus(g, panelX, panelY + panelHeight + 15);
    }

    private void drawRepairStatus(Graphics g, int panelX, int panelY) {
        int panelWidth = 330;
        int panelHeight = 170;

        // Draw repair status panel background
        g.setColor(new Color(20, 24, 32, 210));
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 18, 18);

        // Draw panel border
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

    public void drawPause(Graphics g) {
        g.setColor(new Color(20, 24, 32, 220));
        g.fillRoundRect(250, 170, 300, 120, 20, 20);

        g.setColor(Color.WHITE);
        g.drawString("PAUSED", 370, 220);
        g.drawString("Press P or ESC to continue", 310, 250);
    }

    public void drawGameOver(Graphics g) {
        g.setColor(new Color(40, 20, 20, 220));
        g.fillRoundRect(250, 170, 300, 120, 20, 20);

        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", 350, 220);
    }

    public void drawWin(Graphics g) {
        g.setColor(new Color(20, 40, 25, 220));
        g.fillRoundRect(240, 160, 330, 140, 20, 20);

        g.setColor(Color.WHITE);
        g.drawString("YOU WIN", 365, 215);
        g.drawString("All ship systems are repaired.", 300, 245);
    }
}