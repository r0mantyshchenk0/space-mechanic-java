package cz.cvut.fel.pjv.spacemechanic.collision;

import cz.cvut.fel.pjv.spacemechanic.model.GameObject;
import cz.cvut.fel.pjv.spacemechanic.model.Item;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.RepairableObject;

import java.util.List;

public class CollisionManager {

    public void checkCollisions(Player player, List<GameObject> objects) {
        for (GameObject object : objects) {
            if (!object.isActive()) {
                continue;
            }

            if (player.getBounds().intersects(object.getBounds())) {
                resolveInteraction(player, object);
            }
        }
    }

    public void resolveInteraction(Player player, GameObject object) {
        if (object instanceof Item item) {
            item.applyEffect(player);
        }

        if (object instanceof RepairableObject repairable) {
            repairable.repair(player);
        }
    }
}
