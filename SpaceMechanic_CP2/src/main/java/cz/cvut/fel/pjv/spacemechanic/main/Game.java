package cz.cvut.fel.pjv.spacemechanic.main;

import cz.cvut.fel.pjv.spacemechanic.input.InputHandler;
import cz.cvut.fel.pjv.spacemechanic.level.LevelManager;
import cz.cvut.fel.pjv.spacemechanic.ui.UIManager;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Hlavni trida hry.
 */
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

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                levelManager.saveInventoryToFile();
            }
        });

        frame.setVisible(true);

        gamePanel.startGameLoop();
    }

    public void update() {
        if (currentState == GameState.PLAYING) {
            levelManager.update();

            if (levelManager.isLevelCompleted()) {
                levelManager.saveInventoryToFile();
                changeState(GameState.WIN);
            }

            if (levelManager.getPlayer().getHealth() <= 0) {
                changeState(GameState.GAME_OVER);
            }
        }
    }

    public void render(Graphics g) {
        levelManager.render(g);
        uiManager.render(g);
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