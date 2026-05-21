package cz.cvut.fel.pjv.spacemechanic.model;

import java.util.logging.Logger;

import java.awt.Color;
import java.awt.Graphics;

public class Chest extends GameObject {

    private static final Logger LOGGER = Logger.getLogger(Chest.class.getName());

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
            // Open base
            g.setColor(new Color(120, 85, 45));
            g.fillRoundRect(x, y + 8, width, height - 8, 6, 6);

            // Open lid
            g.setColor(new Color(165, 120, 65));
            g.fillRoundRect(x + 1, y, width - 2, 10, 6, 6);

            g.setColor(new Color(75, 50, 28));
            g.drawRoundRect(x, y + 8, width, height - 8, 6, 6);
            g.drawRoundRect(x + 1, y, width - 2, 10, 6, 6);
        } else {
            // Chest body
            g.setColor(new Color(130, 92, 48));
            g.fillRoundRect(x, y, width, height, 6, 6);

            // Lid
            g.setColor(new Color(185, 135, 75));
            g.fillRoundRect(x, y, width, height / 2, 6, 6);

            // Border
            g.setColor(new Color(75, 50, 28));
            g.drawRoundRect(x, y, width, height, 6, 6);

            // Lock
            g.setColor(new Color(220, 190, 90));
            g.fillRect(x + width / 2 - 4, y + height / 2 - 2, 8, 8);
            g.drawArc(x + width / 2 - 4, y + height / 2 - 8, 8, 8, 0, 180);
        }
    }

    public String getItemName() {
        return itemName;
    }

    public boolean isOpened() {
        return opened;
    }

    public void open() {
        opened = true;
        LOGGER.info("Chest opened. Item: " + itemName + ".");
    }
}