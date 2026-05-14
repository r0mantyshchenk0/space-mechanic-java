package cz.cvut.fel.pjv.spacemechanic.main;

import cz.cvut.fel.pjv.spacemechanic.input.InputHandler;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Panel, na kterem bezi herni smycka a vykreslovani hry.
 */
public class GamePanel extends JPanel implements Runnable {

    private static final int BASE_WIDTH = 1280;
    private static final int BASE_HEIGHT = 720;

    // Vetsi meritko pro prezentaci na notebooku/projektoru.
    private static final double PRESENTATION_SCALE = 1.7;

    private final Game game;
    private final InputHandler inputHandler;

    private Thread gameThread;
    private boolean running;

    public GamePanel(Game game, InputHandler inputHandler) {
        this.game = game;
        this.inputHandler = inputHandler;

        int windowWidth = (int) (BASE_WIDTH * PRESENTATION_SCALE);
        int windowHeight = (int) (BASE_HEIGHT * PRESENTATION_SCALE);

        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setBackground(new Color(7, 11, 20));
        setOpaque(true);
        setFocusable(true);
        addKeyListener(inputHandler);
    }

    /**
     * Spusti hlavni herni smycku.
     */
    public void startGameLoop() {
        if (gameThread == null) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    /**
     * Hlavni game loop.
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
     * Vykresli hru ve vetsim meritku pro prezentaci.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(7, 11, 20));
        g.fillRect(0, 0, getWidth(), getHeight());

        Graphics2D g2 = (Graphics2D) g.create();

        g2.scale(PRESENTATION_SCALE, PRESENTATION_SCALE);
        game.render(g2);

        g2.dispose();
    }
}