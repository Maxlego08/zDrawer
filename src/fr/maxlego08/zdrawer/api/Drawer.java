package fr.maxlego08.zdrawer.api;

import fr.maxlego08.zdrawer.api.configuration.DrawerConfiguration;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * Represents a Drawer in the game, containing various configurations and actions related to its functionality.
 * <p>
 * This class provides methods for managing the drawer's properties, interactions with players,
 * item storage, and upgrades. It also handles drawer configuration and state management.
 * </p>
 */
public interface Drawer {

    /**
     * Gets the name of the configuration associated with this drawer.
     *
     * @return The name of the drawer's configuration.
     */
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
     * Sets the ItemStack for this drawer.
     *
     * @param itemStack The new ItemStack to be placed in the drawer.
     */
    void setItemStack(int index, ItemStack itemStack);

    /**
     * Sets the total amount of items in this drawer.
     *
     * @param amount The new amount of items to be set.
     */
    void setAmount(int index, long amount);

    /**
     * Adds an item to the drawer from a player's inventory.
     *
     * @param player    The player adding the item.
     * @param itemStack The item to add to the drawer.
     * @param hand      The hand (slot) from which the item is being added.
     * @param position
     */
    void addItem(Player player, ItemStack itemStack, EquipmentSlot hand, Location position);

    /**
     * Removes the stored item from the drawer for a player.
     *
     * @param player   The player removing the item.
     * @param position
     */
    void removeItem(Player player, Location position);

    /**
     * Performs necessary actions when the plugin is disabled.
     */
    void onDisable();

    /**
     * Performs necessary actions when the drawer is loaded.
     */
    void onLoad();

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
     * Gets a list of ItemDisplays representing the border of the drawer.
     *
     * @return A list of ItemDisplays for the drawer's border.
     */
    List<ItemDisplay> getBorderDisplays();

    /**
     * Retrieves the data representation of the drawer, often used for saving and loading.
     *
     * @return A string representing the drawer's data.
     */
    String getData();

    /**
     * Checks if the drawer has an ItemStack at the specified index.
     *
     * @param index The index to check.
     * @return true if there is an ItemStack at the specified index, false otherwise.
     */
    boolean hasItemStack(int index);

    /**
     * Retrieves the ItemStack at a specified index in the drawer.
     *
     * @param index The index from which to retrieve the ItemStack.
     * @return The ItemStack at the specified index, or null if none exists.
     */
    ItemStack getItemStack(int index);

    /**
     * Gets the amount of a specific item stored at a given index in the drawer.
     *
     * @param index The index to query the amount from.
     * @return The amount of items at the specified index.
     */
    long getAmount(int index);

    /**
     * Loads the drawer's data from a string representation, typically used for restoring state.
     *
     * @param data The string data to load the drawer's state from.
     */
    void load(String data);

    /**
     * Searches for a DrawerCase that best matches the provided ItemStack.
     * <p>
     * This method iterates through the DrawerCases associated with the Drawer.
     * It first looks for a DrawerCase that contains an ItemStack similar to the provided one
     * and is not full. If none is found, it defaults to the first DrawerCase that does not contain any ItemStack.
     * </p>
     *
     * @param itemStack The ItemStack to match against the DrawerCases.
     * @return An {@link Optional<DrawerCase>} that contains either:
     * - A DrawerCase matching the provided ItemStack.
     * - The first empty DrawerCase, if no matching DrawerCase is found.
     * - An empty Optional if no suitable DrawerCase is found.
     */
    Optional<DrawerCase> findDrawerCase(ItemStack itemStack);

    /**
     * Finds the first available DrawerCase that does not contain an ItemStack.
     * <p>
     * This method is useful for locating the first empty space in a Drawer.
     * It iterates through the DrawerCases and returns the first one found that is empty.
     * </p>
     *
     * @return An {@link Optional<DrawerCase>} containing the first empty DrawerCase,
     * or an empty Optional if all DrawerCases are occupied.
     */
    Optional<DrawerCase> findFirstCase();

    /**
     * Retrieves the configuration settings for this Drawer.
     * <p>
     * The configuration includes various properties and behaviors of the Drawer,
     * such as type, limit, menu item, border, and other specific settings.
     * </p>
     *
     * @return The {@link DrawerConfiguration} associated with this Drawer.
     */
    DrawerConfiguration getConfiguration();

    /**
     * Retrieves the total number of items stored in the Drawer.
     * <p>
     * This method calculates and returns the sum of all items currently held within the Drawer.
     * It is useful for getting a quick overview of the Drawer's capacity usage or for
     * inventory management purposes. The total amount reflects the combined count of all
     * item types present in the Drawer.
     * </p>
     *
     * @return The total number of items contained in the Drawer.
     */
    long getTotalAmount();

    /**
     * Drops all the contents of the Drawer at the specified location.
     * <p>
     * This method is used to physically drop the items contained in the Drawer into the game world.
     * When invoked, it will create item entities at the given location, representing each item
     * stored in the Drawer. This is typically used when a Drawer is broken or needs to be
     * emptied in the game world for any reason.
     * </p>
     *
     * @param location The {@link Location} in the game world where the items should be dropped.
     */
    void dropContent(Location location);
}
