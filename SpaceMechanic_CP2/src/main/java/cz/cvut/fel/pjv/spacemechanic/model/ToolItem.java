package cz.cvut.fel.pjv.spacemechanic.model;

public class ToolItem extends Item {

    public ToolItem(int x, int y, int width, int height, String name) {
        super(x, y, width, height, name);
    }

    @Override
    public void applyEffect(Player player) {
        player.collectItem(this);
        player.useTool(name);
        this.active = false;
    }
}
