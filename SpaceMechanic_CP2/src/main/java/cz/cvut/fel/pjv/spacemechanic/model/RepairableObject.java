package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Graphics;

public abstract class RepairableObject extends GameObject {

    protected String requiredPart;
    protected boolean repaired;
    protected int damageLevel;

    public RepairableObject(int x, int y, int width, int height, String requiredPart) {
        super(x, y, width, height);
        this.requiredPart = requiredPart;
        this.repaired = false;
        this.damageLevel = 100;
    }

    @Override
    public void update() {
        // zatim bez dalsi logiky
    }

    @Override
    public void render(Graphics g) {
        g.drawRect(x, y, width, height);
        g.drawString(getClass().getSimpleName(), x - 5, y - 5);
    }

    public void repair(Player player) {
        if (player.getInventory().containsItem(requiredPart)) {
            repaired = true;
            damageLevel = 0;
        }
    }

    public boolean isRepaired() {
        return repaired;
    }

    public String getRequiredPart() {
        return requiredPart;
    }
}
