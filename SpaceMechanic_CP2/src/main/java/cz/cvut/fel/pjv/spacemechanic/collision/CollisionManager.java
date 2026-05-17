package cz.cvut.fel.pjv.spacemechanic.collision;

import cz.cvut.fel.pjv.spacemechanic.model.GameObject;
import cz.cvut.fel.pjv.spacemechanic.model.Item;
import cz.cvut.fel.pjv.spacemechanic.model.Player;
import cz.cvut.fel.pjv.spacemechanic.model.RepairableObject;

import java.util.List;
/**
 * Spravuje zakladni kolize mezi hracem a hernimi objekty.
 */
public class CollisionManager {
    /**
     * Zkontroluje kolize mezi hracem a aktivnimi objekty.
     *
     * @param player hrac
     * @param objects objekty v aktualnim levelu
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
            item.applyEffect(player);
        }


    }
}
