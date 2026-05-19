package cz.cvut.fel.pjv.spacemechanic.main;

import cz.cvut.fel.pjv.spacemechanic.input.InputHandler;
import cz.cvut.fel.pjv.spacemechanic.level.LevelManager;
import cz.cvut.fel.pjv.spacemechanic.ui.UIManager;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.util.logging.Logger;

/**
 * Main game class.
 * Initializes the main game components and controls the current game state.
 */
public class Game {

    private static final Logger LOGGER =
            Logger.getLogger(Game.class.getName());

    private static final boolean LOGGING_ENABLED =
            Boolean.parseBoolean(System.getProperty("logging", "true"));

    private GameState currentState;
    private final GamePanel gamePanel;
    private final LevelManager levelManager;
    private final UIManager uiManager;
    private final InputHandler inputHandler;

    /**
     * Creates the main game objects and connects the game logic with the UI.
     */
    public Game() {
        this.currentState = GameState.MENU;
        this.levelManager = new LevelManager();
        this.uiManager = new UIManager(this);
        this.inputHandler = new InputHandler(this);
        this.gamePanel = new GamePanel(this, inputHandler);

        log("Game initialized.");
    }

    /**
     * Creates the main game window and starts the game loop.
     */
    public void start() {
        JFrame frame = new JFrame("Space Mechanic");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.startGameLoop();

        log("Game window created and game loop started.");
    }

    /**
     * Updates the game logic according to the current game state.
     */
    public void update() {
        if (currentState == GameState.PLAYING) {
            levelManager.update();

            if (levelManager.isLevelCompleted()) {
                changeState(GameState.WIN);
            }

            if (levelManager.getPlayer().getHealth() <= 0) {
                changeState(GameState.GAME_OVER);
            }
        }
    }

    /**
     * Renders the current level and the user interface.
     *
     * @param g graphics context
     */
    public void render(Graphics g) {
        levelManager.render(g);
        uiManager.render(g);
    }

    /**
     * Changes the current game state.
     *
     * @param newState new game state
     */
    public void changeState(GameState newState) {
        this.currentState = newState;
        log("Game state changed to: " + newState);
    }

    /**
     * Saves the current game state.
     */
    public void saveGameState() {
        levelManager.saveGameState();
        log("Game state saved.");
    }

    /**
     * Loads the previously saved game state.
     */
    public void loadGameState() {
        levelManager.loadGameState();
        log("Game state loaded.");
    }

    private static void log(String message) {
        if (LOGGING_ENABLED) {
            LOGGER.info(message);
        }
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}