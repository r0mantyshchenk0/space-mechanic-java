package cz.cvut.fel.pjv.spacemechanic.ui;

import cz.cvut.fel.pjv.spacemechanic.main.Game;
import cz.cvut.fel.pjv.spacemechanic.main.GameState;
import cz.cvut.fel.pjv.spacemechanic.model.GameObject;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.RepairableObject;

import java.awt.Graphics;

public class UIManager {

    private final Game game;

    public UIManager(Game game) {
        this.game = game;
    }
    // Draw object name and current repair status
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
        g.drawString("SPACE MECHANIC", 330, 200);
        g.drawString("Press any key to start", 330, 240);
    }

    public void drawHUD(Graphics g) {
        Player player = game.getLevelManager().getPlayer();
        g.drawString("Health: " + player.getHealth(), 20, 20);
        g.drawString("Items: " + player.getInventory().getItems().size(), 20, 40);
        g.drawString("ESC - pause", 20, 80);
        g.drawString("Press E to repair", 20, 60);

        int y = 120;// Show repair progress of all repairable objects


        g.drawString("Repair status:", 20, y);
        y += 20;

        for (GameObject object : game.getLevelManager().getObjects()) {
            if (object instanceof RepairableObject repairable) {
                String name = object.getClass().getSimpleName();
                String status;

                if (repairable.isRepaired()) {
                    status = "repaired";
                } else {
                    status = repairable.getRepairProgress() + "%";
                }

                g.drawString(name + " - " + status, 20, y);
                y += 20;
            }
        }
    }

    public void drawPause(Graphics g) {
        g.drawString("PAUSED", 370, 220);
    }

    public void drawGameOver(Graphics g) {
        g.drawString("GAME OVER", 350, 220);
    }

    public void drawWin(Graphics g) {
        g.drawString("YOU WIN", 360, 220);
    }


}
