package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Color;
import java.awt.Graphics;

public class Elevator extends GameObject {

    public Elevator(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
        // Elevator is static and reacts only to player interaction.
    }

    @Override
    public void render(Graphics g) {
        // Main elevator body
        g.setColor(new Color(60, 125, 230));
        g.fillRoundRect(x, y, width, height, 10, 10);

        // Dark border
        g.setColor(new Color(20, 45, 90));
        g.drawRoundRect(x, y, width, height, 10, 10);

        // Elevator door split
        g.setColor(new Color(150, 190, 255));
        g.drawLine(x + width / 2, y + 8, x + width / 2, y + height - 8);

        // Light strip
        g.setColor(new Color(190, 220, 255));
        g.drawLine(x + 8, y + height / 2, x + width - 8, y + height / 2);

        // Label
        g.setColor(Color.WHITE);
        String label = "Lift";
        int labelWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, x + width / 2 - labelWidth / 2, y - 6);

        // Interaction hint
        g.setColor(new Color(220, 235, 255));
        g.drawString("E", x + width / 2 - 4, y + height / 2 + 17);
    }
}