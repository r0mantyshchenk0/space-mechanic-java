package cz.cvut.fel.pjv.spacemechanic.model;

import java.awt.Graphics;
import java.util.Objects;

public class Player extends Entity {

    private int health;
    private final Inventory inventory;
    private String currentTool;

    private boolean movingUp;
    private boolean movingDown;
    private boolean movingLeft;
    private boolean movingRight;

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.health = 100;
        this.inventory = new Inventory();
    }

    @Override
    public void update() {
        velocityX = 0;
        velocityY = 0;

        if (movingUp) {
            velocityY = -3;
        }
        if (movingDown) {
            velocityY = 3;
        }
        if (movingLeft) {
            velocityX = -3;
        }
        if (movingRight) {
            velocityX = 3;
        }

        move();
    }

    @Override
    public void render(Graphics g) {
        g.drawRect(x, y, width, height);
        g.drawString("P", x + 10, y + 20);
    }

    public void interact() {
        // pozdeji interakce s objekty
    }

    public void collectItem(Item item) {
        inventory.addItem(item);
    }

    public void useTool(String toolName) {
        if (inventory.containsItem(toolName)) {
            currentTool = toolName;
        }
    }

    public boolean hasTool(String toolName) {
        return Objects.equals(currentTool, toolName);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingDown(boolean movingDown) {
        this.movingDown = movingDown;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
}
