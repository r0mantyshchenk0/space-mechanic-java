package cz.cvut.fel.pjv.spacemechanic.main;

import cz.cvut.fel.pjv.spacemechanic.input.InputHandler;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Panel, na kterem bezi herni smycka a vykreslovani hry.
 */
public class GamePanel extends JPanel implements Runnable {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private final Game game;
    private final InputHandler inputHandler;

    private Thread gameThread;
    private boolean running;

    /**
     * Vytvori herni panel a pripoji zpracovani vstupu.
     *
     * @param game hlavni instance hry
     * @param inputHandler zpracovani vstupu z klavesnice
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
     * Spusti hlavni herni smycku v samostatnem vlakne.
     */
    public void startGameLoop() {
        if (gameThread == null) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    /**
     * Hlavni herni smycka.
     * Pravidelne aktualizuje logiku hry a prekresluje panel.
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
     * Vykresli aktualni stav hry na panel.
     *
     * @param g graficky kontext
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(7, 11, 20));
        g.fillRect(0, 0, getWidth(), getHeight());

        game.render(g);
    }
}