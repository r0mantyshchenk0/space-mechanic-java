package cz.cvut.fel.pjv.spacemechanic.main;

import cz.cvut.fel.pjv.spacemechanic.input.InputHandler;
import cz.cvut.fel.pjv.spacemechanic.level.LevelManager;
import cz.cvut.fel.pjv.spacemechanic.ui.UIManager;

import javax.swing.JFrame;

public class Game {

    private GameState currentState;
    private final GamePanel gamePanel;
    private final LevelManager levelManager;
    private final UIManager uiManager;
    private final InputHandler inputHandler;

    public Game() {
        this.currentState = GameState.MENU;
        this.levelManager = new LevelManager();
        this.uiManager = new UIManager(this);
        this.inputHandler = new InputHandler(this);
        this.gamePanel = new GamePanel(this, inputHandler);
    }

    public void start() {
        JFrame frame = new JFrame("Space Mechanic");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.startGameLoop();
    }

    public void update() {
        if (currentState == GameState.PLAYING) {
            levelManager.update();
// Player wins when all repairable objects are fixed
            if (levelManager.isLevelCompleted()) {
                changeState(GameState.WIN);
            }

            if (levelManager.getPlayer().getHealth() <= 0) {
                changeState(GameState.GAME_OVER);
            }
        }
    }

    public void changeState(GameState newState) {
        this.currentState = newState;
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
