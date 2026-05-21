package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Color;
import java.awt.Graphics;

public class Generator extends RepairableObject {

    public Generator(int x, int y, int width, int height, String requiredPart) {
        super(x, y, width, height, requiredPart);
    }

    @Override
    public void render(Graphics g) {
        // Outer shadow / base
        g.setColor(new Color(32, 38, 48));
        g.fillRoundRect(x - 5, y - 5, width + 10, height + 10, 12, 12);

        // Main body
        if (repaired) {
            g.setColor(new Color(70, 175, 110));
        } else {
            g.setColor(new Color(205, 165, 60));
        }
        g.fillRoundRect(x, y, width, height, 10, 10);

        // Border
        g.setColor(new Color(35, 35, 35));
        g.drawRoundRect(x, y, width, height, 10, 10);

        // Side pylons
        g.setColor(new Color(110, 85, 35));
        g.fillRect(x + 4, y + 8, 5, height - 16);
        g.fillRect(x + width - 9, y + 8, 5, height - 16);

        // Core ring
        g.setColor(new Color(55, 75, 110));
        g.fillOval(x + 9, y + 9, width - 18, height - 18);

        // Energy core
        if (repaired) {
            g.setColor(new Color(120, 255, 180));
        } else {
            g.setColor(new Color(60, 125, 235));
        }
        g.fillOval(x + 13, y + 13, width - 26, height - 26);

        // Core outline
        g.setColor(new Color(15, 35, 70));
        g.drawOval(x + 13, y + 13, width - 26, height - 26);

        // Small energy mark
        g.setColor(Color.WHITE);
        g.drawLine(x + width / 2, y + 6, x + width / 2 + 3, y + 11);
        g.drawLine(x + width / 2 + 3, y + 11, x + width / 2 - 1, y + 16);

        // Label
        g.setColor(Color.WHITE);
        String label = "Generator";
        int labelWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, x + width / 2 - labelWidth / 2, y - 6);
    }
}