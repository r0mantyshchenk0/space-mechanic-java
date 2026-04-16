package cz.cvut.fel.pjv.spacemechanic.model;

public abstract class Entity extends GameObject {

    protected int velocityX;
    protected int velocityY;

    public Entity(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void move() {
        x += velocityX;
        y += velocityY;
    }
}
