package cz.cvut.fel.pjv.spacemechanic.collision;

import cz.cvut.fel.pjv.spacemechanic.model.GameObject;
import cz.cvut.fel.pjv.spacemechanic.model.Item;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.RepairableObject;

import java.util.List;
import java.util.logging.Logger;
/**
 * Manages basic collisions between the player and game objects.
 */
public class CollisionManager {

    private static final Logger LOGGER = Logger.getLogger(CollisionManager.class.getName());
    /**
     * Checks collisions between the player and active objects.
     *
     * @param player player object
     * @param objects objects in the current level
     */
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
            LOGGER.info("Player collected item: " + item.getName() + ".");
            item.applyEffect(player);
        }


    }
}
