package cz.cvut.fel.pjv.spacemechanic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
/**
 * Player inventory.
 * Stores collected items and crafted modules.
 */
public class Inventory {

    private static final Logger LOGGER = Logger.getLogger(Inventory.class.getName());

    private final List<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }
    /**
     * Adds an item to the inventory.
     *
     * @param item item to add
     */
    public void addItem(Item item) {
        items.add(item);
        LOGGER.info("Inventory item added: " + item.getName() + ".");
    }
    /**
     * Removes the first item with the given name.
     *
     * @param itemName name of the item
     */
    public void removeItem(Item item) {
        if (items.remove(item)) {
            LOGGER.info("Inventory item removed: " + item.getName() + ".");
        }
    }
    /**
     * Removes the first item with the given name.
     *
     * @param itemName name of the item
     */
    public void removeItem(String itemName) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(itemName)) {
                items.remove(i);
                return;
            }
        }
    }
    /**
     * Checks whether the inventory contains an item.
     *
     * @param itemName name of the searched item
     * @return true if the item is in the inventory
     */
    public boolean containsItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    public List<Item> getItems() {
        return items;
    }
}
