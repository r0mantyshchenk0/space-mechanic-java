package cz.cvut.fel.pjv.spacemechanic.model;

public class SparePart extends Item {

    public SparePart(int x, int y, int width, int height, String name) {
        super(x, y, width, height, name);
    }

    @Override
    public void applyEffect(Player player) {
        player.collectItem(this);
        this.active = false;
    }
}
