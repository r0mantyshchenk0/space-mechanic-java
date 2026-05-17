package cz.cvut.fel.pjv.spacemechanic.main;

import cz.cvut.fel.pjv.spacemechanic.input.InputHandler;
import cz.cvut.fel.pjv.spacemechanic.level.LevelManager;
import cz.cvut.fel.pjv.spacemechanic.ui.UIManager;

import javax.swing.JFrame;
import java.awt.Graphics;


/**
 * Hlavni trida hry.
 * Inicializuje hlavni herni komponenty a ridi aktualni stav hry.
 */
public class Game {

    private GameState currentState;
    private final GamePanel gamePanel;
    private final LevelManager levelManager;
    private final UIManager uiManager;
    private final InputHandler inputHandler;

    /**
     * Vytvori zakladni objekty hry a propoji herni logiku s UI.
     */
    public Game() {
        this.currentState = GameState.MENU;
        this.levelManager = new LevelManager();
        this.uiManager = new UIManager(this);
        this.inputHandler = new InputHandler(this);
        this.gamePanel = new GamePanel(this, inputHandler);
    }
    /**
     * Vytvori hlavni okno hry a spusti herni smycku.
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



        frame.setVisible(true);

        gamePanel.startGameLoop();
    }

    /**
     * Aktualizuje herni logiku podle aktualniho stavu hry.
     */
    public void update()  {
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
     * Vykresli aktualni level a uzivatelske rozhrani.
     *
     * @param g graficky kontext
     */
    public void render(Graphics g) {
        levelManager.render(g);
        uiManager.render(g);
    }

    /**
     * Zmeni aktualni stav hry.
     *
     * @param newState novy stav hry
     */
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