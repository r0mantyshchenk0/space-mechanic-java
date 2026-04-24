package cz.cvut.fel.pjv.spacemechanic.level;

import cz.cvut.fel.pjv.spacemechanic.collision.CollisionManager;
import cz.cvut.fel.pjv.spacemechanic.model.DoorSystem;
import cz.cvut.fel.pjv.spacemechanic.model.Engine;
import cz.cvut.fel.pjv.spacemechanic.model.GameObject;
import cz.cvut.fel.pjv.spacemechanic.model.Generator;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.RepairableObject;
import cz.cvut.fel.pjv.spacemechanic.model.SparePart;
import cz.cvut.fel.pjv.spacemechanic.model.ToolItem;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private int currentLevel;
    private final List<GameObject> objects;
    private final Player player;
    private final CollisionManager collisionManager;

    public LevelManager() {
        this.currentLevel = 1;
        this.objects = new ArrayList<>();
        this.player = new Player(100, 100, 32, 32);
        this.collisionManager = new CollisionManager();

        loadLevel(currentLevel);
    }

    public void loadLevel(int level) {
        objects.clear();
        currentLevel = level;

        if (level == 1) {
            objects.add(new SparePart(250, 200, 24, 24, "EnginePart"));
            objects.add(new SparePart(350, 150, 24, 24, "GeneratorPart"));
            objects.add(new ToolItem(300, 250, 24, 24, "Wrench"));
            objects.add(new Engine(700, 350, 40, 40, "EnginePart"));
            objects.add(new Generator(600, 300, 40, 40, "GeneratorPart"));
            objects.add(new DoorSystem(680, 200, 36, 56, "Wrench"));
            objects.add(new SparePart(400, 100, 24, 24, "GeneratorPart"));
            objects.add(new SparePart(450, 180, 24, 24, "DoorPart"));
            objects.add(new DoorSystem(750, 200, 40, 40, "DoorPart"));
        }
    }

    public void update() {
        player.update();

        for (GameObject object : objects) {
            if (object.isActive()) {
                object.update();
            }
        }

        collisionManager.checkCollisions(player, objects);
    }

    public void render(Graphics g) {
        player.render(g);

        for (GameObject object : objects) {
            if (object.isActive()) {
                object.render(g);
            }
        }
    }

    public void interactWithNearbyObject() {
        for (GameObject object : objects) {
            if (!object.isActive()) {
                continue;
            }

            if (player.getBounds().intersects(object.getBounds())) {
                if (object instanceof RepairableObject repairable) {
                    repairable.repair(player);
                }
            }
        }
    }

    public boolean isLevelCompleted() {
        for (GameObject object : objects) {
            if (object instanceof RepairableObject repairable && !repairable.isRepaired()) {
                return false;
            }
        }
        return true;
    }

    public Player getPlayer() {
        return player;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
