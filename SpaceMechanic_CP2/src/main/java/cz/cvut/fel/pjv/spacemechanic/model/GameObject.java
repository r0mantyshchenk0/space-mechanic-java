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
    /**
     * Aktualizuje stav objektu.
     */
    public abstract void update();
    /**
     * Vykresli objekt na obrazovku.
     *
     * @param g graficky kontext
     */
    public abstract void render(Graphics g);
    /**
     * Vraci obdelnik pouzivany pro kontrolu kolizi.
     *
     * @return hranice objektu
     */
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
