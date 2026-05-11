package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Graphics;

public class Wall extends GameObject {

    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
        // Static invisible collision wall.
    }

    @Override
    public void render(Graphics g) {
        // Walls are used only for collision.
        // Visual room borders are drawn by LevelManager.
    }
}