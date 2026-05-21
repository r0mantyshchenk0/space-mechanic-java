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
        // Terminal is static and only provides a hint.
    }

    @Override
    public void render(Graphics g) {
        // Terminal stand
        g.setColor(new Color(45, 38, 90));
        g.fillRoundRect(x + 5, y + height - 6, width - 10, 10, 6, 6);

        // Main terminal screen
        g.setColor(new Color(115, 80, 220));
        g.fillRoundRect(x, y, width, height, 8, 8);

        // Screen border
        g.setColor(new Color(35, 25, 85));
        g.drawRoundRect(x, y, width, height, 8, 8);

        // Inner display
        g.setColor(new Color(45, 35, 115));
        g.fillRoundRect(x + 6, y + 6, width - 12, height - 14, 6, 6);

        // Question mark
        g.setColor(new Color(220, 220, 255));
        g.drawString("?", x + width / 2 - 3, y + height / 2 + 5);

        // Label
        g.setColor(Color.WHITE);
        String label = "Terminal";
        int labelWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, x + width / 2 - labelWidth / 2, y - 6);
    }

    public String getHint() {
        return hint;
    }
}