package cz.cvut.fel.pjv.spacemechanic.model;

import java.util.logging.Logger;

public class SparePart extends Item {

    private static final Logger LOGGER = Logger.getLogger(SparePart.class.getName());

    public SparePart(int x, int y, int width, int height, String name) {
        super(x, y, width, height, name);
    }

    @Override
    public void applyEffect(Player player) {
        player.collectItem(this);
        this.active = false;
    }
}
