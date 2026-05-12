package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Color;
import java.awt.Graphics;

public class DoorSystem extends RepairableObject {

    public DoorSystem(int x, int y, int width, int height, String requiredPart) {
        super(x, y, width, height, requiredPart);
    }

    @Override
    public void render(Graphics g) {
        if (repaired) {
            g.setColor(new Color(70, 180, 110));
        } else {
            g.setColor(new Color(120, 130, 150));
        }

        g.fillRoundRect(x, y, width, height, 8, 8);

        g.setColor(new Color(35, 35, 45));
        g.drawRoundRect(x, y, width, height, 8, 8);

        g.setColor(new Color(80, 90, 110));
        g.drawLine(x + width / 2, y + 5, x + width / 2, y + height - 5);

        g.setColor(new Color(60, 180, 220));
        g.fillOval(x + width - 12, y + height / 2 - 4, 6, 6);

        g.setColor(Color.WHITE);
        String label = "Door";
        int labelWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, x + width / 2 - labelWidth / 2, y - 6);
    }
}