package cz.cvut.fel.pjv.spacemechanic.input;

import cz.cvut.fel.pjv.spacemechanic.main.Game;
import cz.cvut.fel.pjv.spacemechanic.main.GameState;
import cz.cvut.fel.pjv.spacemechanic.model.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

/**
 * Handles keyboard input.
 * Passes actions to the game, level manager and UI manager.
 */
public class InputHandler implements KeyListener {

    private static final Logger LOGGER = Logger.getLogger(InputHandler.class.getName());

    private final Game game;

    public InputHandler(Game game) {
        this.game = game;
    }

    /**
     * Handles a key press event.
     *
     * @param e keyboard event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Any key in the menu starts the game
        if (game.getCurrentState() == GameState.MENU) {
            LOGGER.info("Start key pressed. Leaving menu.");
            game.changeState(GameState.PLAYING);
            return;
        }

        /**
         * Handles releasing a movement key.
         *
         * @param e keyboard event
         */
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (game.getCurrentState() == GameState.PLAYING) {
                LOGGER.info("Pause requested by ESC.");
                game.changeState(GameState.PAUSED);
            } else if (game.getCurrentState() == GameState.PAUSED) {
                LOGGER.info("Resume requested by ESC.");
                game.changeState(GameState.PLAYING);
            }
            return;
        }

        // Other keys are handled only during gameplay
        if (game.getCurrentState() != GameState.PLAYING) {
            return;
        }

        Player player = game.getLevelManager().getPlayer();

        if (player == null) {
            LOGGER.warning("Input ignored because player is not available.");
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setMovingUp(true);
            case KeyEvent.VK_S -> player.setMovingDown(true);
            case KeyEvent.VK_A -> player.setMovingLeft(true);
            case KeyEvent.VK_D -> player.setMovingRight(true);

            // Interaction with an object near the player
            case KeyEvent.VK_E -> {
                LOGGER.info("Interaction key pressed.");
                game.getLevelManager().interactWithNearbyObject();
            }

            // Show or hide the inventory
            case KeyEvent.VK_I -> {
                LOGGER.info("Inventory toggle key pressed.");
                game.getUiManager().toggleInventory();
            }

            // Show or hide the crafting menu
            case KeyEvent.VK_C -> {
                LOGGER.info("Crafting menu toggle key pressed.");
                game.getUiManager().toggleCraftingMenu();
            }

            // Crafting is performed only when the crafting menu is open
            case KeyEvent.VK_ENTER -> {
                if (game.getUiManager().isCraftingMenuVisible()) {
                    LOGGER.info("Crafting confirmed by ENTER.");
                    game.getLevelManager().craftCurrentLevelRecipe();
                }
            }

            default -> {
            }
        }
    }

    /**
     * Reacts to releasing a movement key.
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
     * This method is required by the KeyListener interface.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }
}