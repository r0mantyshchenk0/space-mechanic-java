package cz.cvut.fel.pjv.spacemechanic.input;

import cz.cvut.fel.pjv.spacemechanic.main.Game;
import cz.cvut.fel.pjv.spacemechanic.main.GameState;
import cz.cvut.fel.pjv.spacemechanic.model.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Zpracovava vstup z klavesnice a predava akce do hry.
 */
public class InputHandler implements KeyListener {

    private final Game game;

    public InputHandler(Game game) {
        this.game = game;
    }

    /**
     * Reaguje na stisk klavesy.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // V menu libovolna klavesa spusti hru
        if (game.getCurrentState() == GameState.MENU) {
            game.changeState(GameState.PLAYING);
            return;
        }

        // Escape prepina pauzu
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (game.getCurrentState() == GameState.PLAYING) {
                game.changeState(GameState.PAUSED);
            } else if (game.getCurrentState() == GameState.PAUSED) {
                game.changeState(GameState.PLAYING);
            }
            return;
        }

        // Ostatni klavesy se zpracovavaji jen behem hry
        if (game.getCurrentState() != GameState.PLAYING) {
            return;
        }

        Player player = game.getLevelManager().getPlayer();

        if (player == null) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setMovingUp(true);
            case KeyEvent.VK_S -> player.setMovingDown(true);
            case KeyEvent.VK_A -> player.setMovingLeft(true);
            case KeyEvent.VK_D -> player.setMovingRight(true);

            // Interakce s objektem pobliz hrace
            case KeyEvent.VK_E -> game.getLevelManager().interactWithNearbyObject();

            // Zobrazeni nebo skryti inventare
            case KeyEvent.VK_I -> game.getUiManager().toggleInventory();

            // Zobrazeni nebo skryti crafting menu
            case KeyEvent.VK_C -> game.getUiManager().toggleCraftingMenu();

            // Crafting se provede pouze pri otevrenem crafting menu
            case KeyEvent.VK_ENTER -> {
                if (game.getUiManager().isCraftingMenuVisible()) {
                    game.getLevelManager().craftCurrentLevelRecipe();
                }
            }

            default -> {
            }
        }
    }

    /**
     * Reaguje na pusteni pohybove klavesy.
     */
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

    /**
     * Tato metoda zde musi byt kvuli rozhrani KeyListener.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }
}