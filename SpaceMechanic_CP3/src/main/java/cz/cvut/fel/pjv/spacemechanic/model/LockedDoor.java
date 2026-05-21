package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Color;
import java.awt.Graphics;

public class LockedDoor extends GameObject {

    private final String requiredModule;

    public LockedDoor(int x, int y, int width, int height, String requiredModule) {
        super(x, y, width, height);
        this.requiredModule = requiredModule;
    }

    @Override
    public void update() {
        // Locked door is removed after the correct module is used.
    }

    @Override
    public void render(Graphics g) {
        // Main locked door
        g.setColor(new Color(160, 65, 65));
        g.fillRoundRect(x, y, width, height, 8, 8);

        // Door border
        g.setColor(new Color(60, 20, 20));
        g.drawRoundRect(x, y, width, height, 8, 8);

        // Door panels
        g.setColor(new Color(115, 40, 40));
        g.drawLine(x + 6, y + 10, x + width - 6, y + 10);
        g.drawLine(x + 6, y + height - 10, x + width - 6, y + height - 10);

        // Lock icon
        g.setColor(new Color(235, 210, 110));
        int lockX = x + width / 2 - 5;
        int lockY = y + height / 2 - 2;
        g.drawArc(lockX, lockY - 8, 10, 10, 0, 180);
        g.fillRect(lockX, lockY - 2, 10, 9);

        // Label
        g.setColor(Color.WHITE);
        String label = "Locked";
        int labelWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, x + width / 2 - labelWidth / 2, y - 6);
    }

    public String getRequiredModule() {
        return requiredModule;
    }
}