package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Color;
import java.awt.Graphics;

public class Chest extends GameObject {

    private final String itemName;
    private boolean opened;

    public Chest(int x, int y, int width, int height, String itemName) {
        super(x, y, width, height);
        this.itemName = itemName;
        this.opened = false;
    }

    @Override
    public void update() {
        // Chest is static.
    }

    @Override
    public void render(Graphics g) {
        if (opened) {
            // Open chest
            g.setColor(new Color(130, 95, 55));
            g.fillRoundRect(x, y + 8, width, height - 8, 6, 6);

            g.setColor(new Color(170, 125, 75));
            g.fillRoundRect(x, y, width, 10, 6, 6);

            g.setColor(new Color(80, 55, 30));
            g.drawRoundRect(x, y + 8, width, height - 8, 6, 6);
            g.drawRoundRect(x, y, width, 10, 6, 6);
        } else {
            // Closed chest
            g.setColor(new Color(140, 100, 55));
            g.fillRoundRect(x, y, width, height, 6, 6);

            g.setColor(new Color(185, 135, 75));
            g.fillRoundRect(x, y, width, height / 2, 6, 6);

            g.setColor(new Color(80, 55, 30));
            g.drawRoundRect(x, y, width, height, 6, 6);

            // Lock
            g.setColor(new Color(220, 190, 90));
            g.fillRect(x + width / 2 - 4, y + height / 2 - 3, 8, 8);
            g.drawArc(x + width / 2 - 4, y + height / 2 - 8, 8, 8, 0, 180);
        }

        g.setColor(Color.WHITE);
        String label = "Chest";
        int labelWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, x + width / 2 - labelWidth / 2, y - 6);
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isOpened() {
        return opened;
    }

    public void open() {
        opened = true;
    }
}