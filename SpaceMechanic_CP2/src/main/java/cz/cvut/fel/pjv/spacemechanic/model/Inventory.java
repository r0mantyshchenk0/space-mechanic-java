package cz.cvut.fel.pjv.spacemechanic.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Inventar hrace.
 * Uchovava sebrane predmety a vyrobene moduly.
 */
public class Inventory {

    private final List<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }
    /**
     * Prida predmet do inventare.
     *
     * @param item pridavany predmet
     */
    public void addItem(Item item) {
        items.add(item);
    }
    /**
     * Odebere prvni predmet se zadanym nazvem.
     *
     * @param item Name nazev predmetu
     */
    public void removeItem(Item item) {
        items.remove(item);
    }
    /**
     * Odebere prvni predmet se zadanym nazvem.
     *
     * @param itemName nazev predmetu
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
     * Zkontroluje, jestli inventar obsahuje predmet.
     *
     * @param itemName nazev hledaneho predmetu
     * @return true, pokud je predmet v inventari
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
