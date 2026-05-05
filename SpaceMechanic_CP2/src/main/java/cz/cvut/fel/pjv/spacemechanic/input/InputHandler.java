package cz.cvut.fel.pjv.spacemechanic.input;

import cz.cvut.fel.pjv.spacemechanic.main.Game;
import cz.cvut.fel.pjv.spacemechanic.main.GameState;
import cz.cvut.fel.pjv.spacemechanic.model.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

        private final Game game;

    public InputHandler(Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Player player = game.getLevelManager().getPlayer();

        if (game.getCurrentState() == GameState.MENU) {
            game.changeState(GameState.PLAYING);
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (game.getCurrentState() == GameState.PLAYING) {
                game.changeState(GameState.PAUSED);
            } else if (game.getCurrentState() == GameState.PAUSED) {
                game.changeState(GameState.PLAYING);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (game.getCurrentState() == GameState.PLAYING) {
                game.changeState(GameState.PAUSED);
            } else if (game.getCurrentState() == GameState.PAUSED) {
                game.changeState(GameState.PLAYING);
            }
        }

        if (game.getCurrentState() != GameState.PLAYING || player == null) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setMovingUp(true);
            case KeyEvent.VK_S -> player.setMovingDown(true);
            case KeyEvent.VK_A -> player.setMovingLeft(true);
            case KeyEvent.VK_D -> player.setMovingRight(true);
            // Try to repair objects when player presses E
            case KeyEvent.VK_E -> game.getLevelManager().interactWithNearbyObject();
            default -> {
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Player player = game.getLevelManager().getPlayer();

        if (player == null) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setMovingUp(false);
            case KeyEvent.VK_S -> player.setMovingDown(false);
            case KeyEvent.VK_A -> player.setMovingLeft(false);
            case KeyEvent.VK_D -> player.setMovingRight(false);
            default -> {
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // nic
    }
}
