package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Color;
import java.awt.Graphics;



public abstract class Item extends GameObject {

    protected String name;

    public Item(int x, int y, int width, int height, String name) {
        super(x, y, width, height);
        this.name = name;
    }

    @Override
    public void update() {
        // item does not move by itself
    }

    @Override
    public void render(Graphics g) {
        // Draw item as a collectible spare part
        g.setColor(new java.awt.Color(90, 220, 160));
        g.fillOval(x, y, width, height);

        g.setColor(new java.awt.Color(20, 80, 60));
        g.drawOval(x, y, width, height);

        // Draw item name above the object
        g.setColor(java.awt.Color.WHITE);
        g.drawString(name, x - 8, y - 6);
    }

    public String getName() {
        return name;
    }

    public abstract void applyEffect(Player player);
}
