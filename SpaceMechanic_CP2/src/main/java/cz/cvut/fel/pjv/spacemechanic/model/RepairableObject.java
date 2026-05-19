package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Color;
import java.awt.Graphics;
/**
 * Zakladni trida pro objekty, ktere lze opravit.
 * Obsahuje potrebnou soucastku, stav opravy a postup opravy.
 */
public abstract class RepairableObject extends GameObject {

    // Part needed to repair this object
    protected String requiredPart;

    // True when the object is fully repaired
    protected boolean repaired;

    // Current repair progress in percent
    protected int repairProgress;

    public RepairableObject(int x, int y, int width, int height, String requiredPart) {
        super(x, y, width, height);
        this.requiredPart = requiredPart;
        this.repaired = false;
        this.repairProgress = 0;
    }

    @Override
    public void update() {
        // Static repair objects do not need frame-by-frame logic yet
    }

    @Override
    public void render(Graphics g) {
        // Draw repairable system with color based on repair state
        if (repaired) {
            g.setColor(new Color(70, 180, 110));
        } else {
            g.setColor(new Color(190, 140, 45));
        }

        g.fillRoundRect(x, y, width, height, 8, 8);

        g.setColor(new Color(20, 20, 20));
        g.drawRoundRect(x, y, width, height, 8, 8);

        // Small inner detail to make objects look less empty
        g.setColor(new Color(40, 45, 55));
        g.drawLine(x + 6, y + 8, x + width - 6, y + 8);
        g.drawLine(x + 6, y + height - 8, x + width - 6, y + height - 8);

        // Draw short object name above the object
        g.setColor(Color.WHITE);
        String label = getDisplayName();
        int labelWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, x + width / 2 - labelWidth / 2, y - 6);
    }

    private String getDisplayName() {
        String className = getClass().getSimpleName();

        if (className.equals("DoorSystem")) {
            return "Door";
        }

        return className;
    }
    /**
     * Pokusi se opravit objekt pomoci predmetu z inventare hrace.
     *
     * @param player hrac, ktery objekt opravuje
     */
    public void repair(Player player) {
        // Do nothing if the object is already repaired
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
    }/**
     * Vraci informaci, jestli je objekt opraveny.
     *
     * @return true, pokud je objekt opraveny
     */

    public boolean isRepaired() {
        return repaired;
    }
    /**
     * Vraci aktualni postup opravy.
     *
     * @return postup opravy v procentech
     */
    public int getRepairProgress() {
        return repairProgress;
    }

    public String getRequiredPart() {
        return requiredPart;
    }

    /**
     * Marks the object as fully repaired.
     * This method is used when loading a saved game state.
     */
    public void forceRepair() {
        this.repaired = true;
        this.repairProgress = 100;
    }
}