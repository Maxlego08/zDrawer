package fr.maxlego08.zdrawer.api;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Drawer {

    String getConfigurationName();

    /**
     * Gets the location of this drawer in the world.
     *
     * @return The location of the drawer.
     */
    Location getLocation();

    /**
     * Gets the face of the block this drawer is attached to.
     *
     * @return The block face the drawer is facing.
     */
    BlockFace getBlockFace();

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

    /**
     * Gets the TextDisplay associated with this drawer.
     *
     * @return The TextDisplay for the drawer.
     */
    TextDisplay getTextDisplay();

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
     * Gets the maximum limit of items this drawer can hold.
     *
     * @return The limit of items.
     */
    long getLimit();

    /**
     * Checks if this drawer has a defined limit for items.
     *
     * @return true if there is a limit, false otherwise.
     */
    boolean hasLimit();

    /**
     * Gets the upgrade applied to this drawer.
     *
     * @return The current upgrade.
     */
    DrawerUpgrade getUpgrade();

    /**
     * Sets the upgrade for this drawer.
     *
     * @param drawerUpgrade The new upgrade to be applied.
     */
    void setUpgrade(DrawerUpgrade drawerUpgrade);

    /**
     * Gets the name of the upgrade applied to this drawer.
     *
     * @return The name of the current upgrade.
     */
    String getUpgradeName();

    /**
     * Checks if this drawer has an upgrade applied.
     *
     * @return true if there is an upgrade, false otherwise.
     */
    boolean hasUpgrade();

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

    List<ItemDisplay> getBorderDisplays();
}
