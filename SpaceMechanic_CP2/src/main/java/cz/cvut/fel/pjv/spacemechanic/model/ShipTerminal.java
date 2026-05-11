package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Color;
import java.awt.Graphics;

public class ShipTerminal extends GameObject {

    private final String hint;

    public ShipTerminal(int x, int y, int width, int height, String hint) {
        super(x, y, width, height);
        this.hint = hint;
    }

    @Override
    public void update() {
        // Terminal is static and only provides a hint after interaction
    }

    @Override
    public void render(Graphics g) {
        // Draw terminal as a small computer panel
        g.setColor(new Color(120, 90, 220));
        g.fillRoundRect(x, y, width, height, 8, 8);

        g.setColor(new Color(35, 25, 80));
        g.drawRoundRect(x, y, width, height, 8, 8);

        g.setColor(Color.WHITE);
        g.drawString("Terminal", x - 8, y - 6);

        g.setColor(new Color(210, 210, 255));
        g.drawString("?", x + width / 2 - 3, y + height / 2 + 5);
    }

    public String getHint() {
        return hint;
    }
}