package cz.cvut.fel.pjv.spacemechanic.main;

import cz.cvut.fel.pjv.spacemechanic.input.InputHandler;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Panel responsible for the game loop and rendering.
 */
public class GamePanel extends JPanel implements Runnable {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private final Game game;
    private final InputHandler inputHandler;

    private Thread gameThread;
    private boolean running;

    /**
     * Creates the game panel and connects keyboard input handling.
     *
     * @param game main game instance
     * @param inputHandler keyboard input handler
     */
    public GamePanel(Game game, InputHandler inputHandler) {
        this.game = game;
        this.inputHandler = inputHandler;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(7, 11, 20));
        setOpaque(true);
        setFocusable(true);
        addKeyListener(inputHandler);
    }

    /**
     * Starts the main game loop in a separate thread.
     */
    public void startGameLoop() {
        if (gameThread == null) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    /**
     * Main game loop.
     * It regularly updates the game logic and repaints the panel.
     */
    @Override
    public void run() {
        final int fps = 60;
        final double drawInterval = 1_000_000_000.0 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                game.update();
                repaint();
                delta--;
            }
        }
    }

    /**
     * Renders the current game state on the panel.
     *
     * @param g graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(7, 11, 20));
        g.fillRect(0, 0, getWidth(), getHeight());

        game.render(g);
    }
}