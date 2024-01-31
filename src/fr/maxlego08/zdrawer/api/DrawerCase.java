package fr.maxlego08.zdrawer.api;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public interface DrawerCase {

    /**
     * Gets the ItemStack currently stored in this drawer.
     *
     * @return The ItemStack in the drawer.
     */
    ItemStack getItemStack();

    /**
     * Sets the ItemStack for this drawer.
     *
     * @param itemStack The new ItemStack to be placed in the drawer.
     */
    void setItemStack(ItemStack itemStack);

    /**
     * Gets a string representation of the ItemStack in this drawer.
     *
     * @return St7ring representation of the ItemStack.
     */
    String getItemStackAsString();

    /**
     * Gets the total amount of items in this drawer.
     *
     * @return The total amount of items.
     */
    long getAmount();

    /**
     * Sets the total amount of items in this drawer.
     *
     * @param amount The new amount of items to be set.
     */
    void setAmount(long amount);

    /**
     * Gets the ItemDisplay associated with this drawer.
     *
     * @return The ItemDisplay for the drawer.
     */
    ItemDisplay getItemDisplay();

    void setItemDisplay(ItemDisplay itemDisplay);

    /**
     * Gets the TextDisplay associated with this drawer.
     *
     * @return The TextDisplay for the drawer.
     */
    TextDisplay getTextDisplay();

    void setTextDisplay(TextDisplay textDisplay);

    /**
     * Adds an item to the drawer from a player's inventory.
     *
     * @param player    The player adding the item.
     * @param itemStack The item to add to the drawer.
     * @param hand      The hand (slot) from which the item is being added.
     */
    void addItem(Player player, ItemStack itemStack, EquipmentSlot hand);

    /**
     * Removes the stored item from the drawer for a player.
     *
     * @param player The player removing the item.
     */
    void removeItem(Player player);

    /**
     * Performs necessary actions when the plugin is disabled.
     */
    void onDisable();

    void onLoad();

    /**
     * Checks if there is an ItemStack stored in this drawer.
     *
     * @return true if there is an ItemStack, false otherwise.
     */
    boolean hasItemStack();

    /**
     * Removes a specified amount of items from this drawer.
     *
     * @param amount The amount of items to remove.
     */
    void remove(long amount);

    /**
     * Adds a specified amount of items to this drawer.
     *
     * @param amount The amount of items to add.
     */
    void add(long amount);

    /**
     * Checks if the drawer is full and cannot accept more items.
     *
     * @return true if the drawer is full, false otherwise.
     */
    boolean isFull();

    /**
     * Updates the text displayed with the TextDisplay entity
     */
    void updateText();

}
