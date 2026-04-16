package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean active;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
    }

    public abstract void update();

    public abstract void render(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
