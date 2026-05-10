package cz.cvut.fel.pjv.spacemechanic.main;

import cz.cvut.fel.pjv.spacemechanic.input.InputHandler;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;

public class GamePanel extends JPanel implements Runnable {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 800;

    private final Game game;
    private Thread gameThread;
    private boolean running;

    public GamePanel(Game game, InputHandler inputHandler) {
        this.game = game;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(inputHandler);
    }

    public void startGameLoop() {
        if (gameThread == null) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        while (running) {
            game.update();
            repaint();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw dark space-like background
        g.setColor(new java.awt.Color(12, 16, 24));
        g.fillRect(0, 0, getWidth(), getHeight());

        game.render(g);
    }
}
