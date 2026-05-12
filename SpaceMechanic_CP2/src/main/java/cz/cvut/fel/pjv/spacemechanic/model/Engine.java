package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Color;
import java.awt.Graphics;

public class Engine extends RepairableObject {

    public Engine(int x, int y, int width, int height, String requiredPart) {
        super(x, y, width, height, requiredPart);
    }

    @Override
    public void render(Graphics g) {
        if (repaired) {
            g.setColor(new Color(70, 180, 110));
        } else {
            g.setColor(new Color(210, 120, 45));
        }

        g.fillRoundRect(x, y, width, height, 10, 10);

        g.setColor(new Color(35, 35, 35));
        g.drawRoundRect(x, y, width, height, 10, 10);

        g.setColor(new Color(80, 50, 35));
        g.drawLine(x + 6, y + 10, x + width - 6, y + 10);
        g.drawLine(x + 6, y + 20, x + width - 6, y + 20);
        g.drawLine(x + 6, y + 30, x + width - 6, y + 30);

        g.setColor(Color.WHITE);
        String label = "Engine";
        int labelWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, x + width / 2 - labelWidth / 2, y - 6);
    }
}