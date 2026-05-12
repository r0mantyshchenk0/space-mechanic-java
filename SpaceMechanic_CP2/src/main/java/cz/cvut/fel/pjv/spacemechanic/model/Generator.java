package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Color;
import java.awt.Graphics;

public class Generator extends RepairableObject {

    public Generator(int x, int y, int width, int height, String requiredPart) {
        super(x, y, width, height, requiredPart);
    }

    @Override
    public void render(Graphics g) {
        if (repaired) {
            g.setColor(new Color(70, 180, 110));
        } else {
            g.setColor(new Color(210, 180, 65));
        }

        g.fillRoundRect(x, y, width, height, 10, 10);

        g.setColor(new Color(35, 35, 35));
        g.drawRoundRect(x, y, width, height, 10, 10);

        g.setColor(new Color(60, 110, 190));
        g.fillOval(x + 10, y + 10, width - 20, height - 20);

        g.setColor(new Color(20, 40, 80));
        g.drawOval(x + 10, y + 10, width - 20, height - 20);

        g.setColor(Color.WHITE);
        String label = "Generator";
        int labelWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, x + width / 2 - labelWidth / 2, y - 6);
    }
}