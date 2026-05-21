package cz.cvut.fel.pjv.spacemechanic.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Player inventory.
 * Stores collected items and crafted modules.
 */
public class Inventory {

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
    }
    /**
     * Removes the first item with the given name.
     *
     * @param itemName name of the item
     */
    public void removeItem(Item item) {
        items.remove(item);
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
