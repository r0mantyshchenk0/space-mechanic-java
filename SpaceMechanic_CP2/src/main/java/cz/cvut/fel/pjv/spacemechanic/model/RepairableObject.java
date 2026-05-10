package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Graphics;

public abstract class RepairableObject extends GameObject {

    protected String requiredPart; // Part needed to repair this object
    protected boolean repaired; // True when the object is fully repaired
    protected int repairProgress; // Current repair progress in percent

    public RepairableObject(int x, int y, int width, int height, String requiredPart) {
        super(x, y, width, height);
        this.requiredPart = requiredPart;
        this.repaired = false;
        this.repairProgress = 0;
    }

    @Override
    public void update() {
        // zatim bez dalsi logiky
    }

    @Override
    public void render(Graphics g) {
        // Draw object name and current repair status
        g.drawRect(x, y, width, height);
        g.drawString(getClass().getSimpleName(), x - 5, y - 5);

        if (repaired) {
            g.drawString("repaired", x - 5, y + height + 15);
        } else {
            g.drawString("repair: " + repairProgress + "%", x - 5, y + height + 15);
            g.drawString("needs: " + requiredPart, x - 5, y + height + 30);
        }
    }
    // Do nothing if the object is already repaired
    public void repair(Player player) {
        if (repaired) {
            return;
        }
// Player needs the correct part to repair this object

        if (!player.getInventory().containsItem(requiredPart)) {
            return;
        }
// Increase progress step by step
        repairProgress += 25;

        if (repairProgress >= 100) {
            repairProgress = 100;
            repaired = true;
        }
    }

    public boolean isRepaired() {
        return repaired;
    }

    public int getRepairProgress() {
        return repairProgress;
    }

    public String getRequiredPart() {
        return requiredPart;
    }
}