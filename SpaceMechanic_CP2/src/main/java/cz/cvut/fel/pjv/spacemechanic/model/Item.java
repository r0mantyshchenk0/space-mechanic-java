package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Graphics;

public abstract class Item extends GameObject {

    protected String name;

    public Item(int x, int y, int width, int height, String name) {
        super(x, y, width, height);
        this.name = name;
    }

    @Override
    public void update() {
        // item se sam nehybe
    }

    @Override
    public void render(Graphics g) {
        g.drawOval(x, y, width, height);
        g.drawString(name, x - 5, y - 5);
    }

    public String getName() {
        return name;
    }

    public abstract void applyEffect(Player player);
}
