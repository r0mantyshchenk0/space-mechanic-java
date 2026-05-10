package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Color;
import java.awt.Graphics;

public class Elevator extends GameObject {

    public Elevator(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
        // Elevator is static, it only reacts to player interaction
    }

    @Override
    public void render(Graphics g) {
        // Draw elevator as a blue terminal
        g.setColor(new Color(70, 120, 220));
        g.fillRoundRect(x, y, width, height, 8, 8);

        g.setColor(new Color(20, 30, 60));
        g.drawRoundRect(x, y, width, height, 8, 8);

        g.setColor(Color.WHITE);
        g.drawString("Lift", x + 4, y - 6);

        g.setColor(new Color(180, 210, 255));
        g.drawLine(x + 6, y + height / 2, x + width - 6, y + height / 2);
        g.drawString("E", x + width / 2 - 4, y + height / 2 + 14);
    }
}