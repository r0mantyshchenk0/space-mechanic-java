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
        // Locked door is removed only after correct module is used.
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(150, 60, 60));
        g.fillRoundRect(x, y, width, height, 8, 8);

        g.setColor(new Color(60, 20, 20));
        g.drawRoundRect(x, y, width, height, 8, 8);

        g.setColor(Color.WHITE);
        g.drawString("Locked", x - 6, y - 6);
    }

    public String getRequiredModule() {
        return requiredModule;
    }
}