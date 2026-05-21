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
        // Outer elevator frame
        g.setColor(new Color(24, 34, 56));
        g.fillRoundRect(x - 10, y - 12, width + 20, height + 24, 14, 14);

        // Side rails
        g.setColor(new Color(95, 120, 155));
        g.fillRoundRect(x - 6, y - 6, 5, height + 12, 5, 5);
        g.fillRoundRect(x + width + 1, y - 6, 5, height + 12, 5, 5);

        // Main cabin
        g.setColor(new Color(55, 125, 230));
        g.fillRoundRect(x, y, width, height, 10, 10);

        // Door panels
        g.setColor(new Color(38, 88, 180));
        g.fillRect(x + 5, y + 8, width / 2 - 5, height - 16);
        g.fillRect(x + width / 2, y + 8, width / 2 - 5, height - 16);

        // Cabin border
        g.setColor(new Color(10, 30, 75));
        g.drawRoundRect(x, y, width, height, 10, 10);

        // Center split
        g.setColor(new Color(190, 220, 255));
        g.drawLine(x + width / 2, y + 8, x + width / 2, y + height - 8);

        // Top status lamp
        g.setColor(new Color(90, 255, 180));
        g.fillOval(x + width / 2 - 5, y - 8, 10, 10);

        // Bottom light
        g.setColor(new Color(115, 180, 255));
        g.drawLine(x + 7, y + height - 8, x + width - 7, y + height - 8);

        // Interaction hint
        g.setColor(Color.WHITE);
        g.drawString("E", x + width / 2 - 4, y + height / 2 + 5);

        // Label
        String label = "Lift";
        int labelWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, x + width / 2 - labelWidth / 2, y - 10);
    }
}