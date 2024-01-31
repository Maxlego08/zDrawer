package fr.maxlego08.zdrawer.api;

import fr.maxlego08.zdrawer.api.configuration.DrawerConfiguration;
import fr.maxlego08.zdrawer.api.configuration.DrawerSize;
import fr.maxlego08.zdrawer.api.craft.Craft;
import fr.maxlego08.zdrawer.api.enums.DrawerType;
import fr.maxlego08.zdrawer.api.storage.IStorage;
import fr.maxlego08.zdrawer.api.storage.Savable;
import fr.maxlego08.zdrawer.api.utils.DisplaySize;
import fr.maxlego08.zdrawer.api.utils.DrawerPosition;
import fr.maxlego08.zdrawer.api.utils.NamespaceContainer;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface DrawerManager extends Savable {

    /**
     * Retrieves a craft recipe by its name.
     *
     * @param craftName The name of the craft recipe.
     * @return An Optional containing the Craft if found, or an empty Optional otherwise.
     */
    Optional<Craft> getCraft(String craftName);

    /**
     * Retrieves a drawer upgrade by its name.
     *
     * @param upgradeName The name of the drawer upgrade.
     * @return An Optional containing the DrawerUpgrade if found, or an empty Optional otherwise.
     */
    Optional<DrawerUpgrade> getUpgrade(String upgradeName);

    /**
     * Retrieves the configuration for a Drawer by its name.
     * <p>
     * This method looks up the configuration of a Drawer based on its name.
     * It is useful when you need to access specific properties or settings of a Drawer
     * that has been identified or categorized by a unique name.
     * </p>
     *
     * @param drawerName The name of the Drawer for which the configuration is sought.
     * @return An {@link Optional<DrawerConfiguration>} containing the configuration of the specified Drawer.
     *         The Optional is empty if no Drawer with the specified name exists.
     */
    Optional<DrawerConfiguration> getDrawer(String drawerName);

    /**
     * Gets a list of all the names of Drawers available.
     * <p>
     * This method provides a comprehensive list of all the Drawer names.
     * This can be used for display purposes or for iterating through all available Drawers
     * to perform some action or analysis.
     * </p>
     *
     * @return A List of Strings, each representing the name of a Drawer.
     */
    List<String> getDrawerNames();

    /**
     * Gives a Drawer to a player with specified configurations.
     * <p>
     * This method is used to provide a player with a Drawer item that has specific characteristics
     * such as name, upgrade, material, and amount. It's useful for commands or events where
     * a player needs to receive a Drawer directly.
     * </p>
     *
     * @param sender          The CommandSender who is issuing the command.
     * @param player          The Player who will receive the Drawer.
     * @param drawerName      The name of the Drawer to be given.
     * @param drawerUpgrade   The upgrade to be applied to the Drawer.
     * @param material        The Material of the Drawer.
     * @param amount          The amount of the Drawer items to be given.
     */
    void giveDrawer(CommandSender sender, Player player, String drawerName, DrawerUpgrade drawerUpgrade, Material material, Long amount);

    /**
     * Builds an ItemStack representing a Drawer based on its configuration.
     * <p>
     * This method is responsible for creating an ItemStack that visually and functionally
     * represents a Drawer, using the specified DrawerConfiguration.
     * </p>
     *
     * @param drawerConfiguration The configuration of the Drawer to build.
     * @param player              The Player for whom the Drawer is being built.
     * @param drawer              The Drawer instance to build the ItemStack for.
     * @return The built ItemStack representing the specified Drawer.
     */
    ItemStack buildDrawer(DrawerConfiguration drawerConfiguration, Player player, Drawer drawer);

    /**
     * Gets a list of all available upgrade names.
     *
     * @return A list of upgrade names.
     */
    List<String> getUpgradeNames();

    /**
     * Gives a craft item to a player.
     * <p>
     * This method is used to provide a player with a craft item specified by its name.
     * It's typically used in scenarios where players need to receive predefined craft items.
     * </p>
     *
     * @param sender   The CommandSender who is issuing the command.
     * @param player   The Player who will receive the craft item.
     * @param craftName The name of the craft item to be given.
     */
    void giveCraft(CommandSender sender, Player player, String craftName);

    /**
     * Gets a list of all available craft names.
     *
     * @return A list of craft names.
     */
    List<String> getCraftNames();

    /**
     * Places a Drawer in the world at a specified location.
     * <p>
     * This method is used to programmatically place a Drawer in the world.
     * It sets up the Drawer at the specified coordinates with the given configuration.
     * </p>
     *
     * @param sender         The CommandSender who is issuing the command.
     * @param drawerName     The name of the Drawer to be placed.
     * @param world          The World where the Drawer is to be placed.
     * @param x              The x-coordinate for the Drawer's location.
     * @param y              The y-coordinate for the Drawer's location.
     * @param z              The z-coordinate for the Drawer's location.
     * @param blockFace      The BlockFace indicating the direction the Drawer is facing.
     * @param drawerUpgrade  The upgrade to apply to the Drawer.
     * @param material       The Material of the Drawer.
     * @param amount         The amount of the material for the Drawer.
     */
    void placeDrawer(CommandSender sender, String drawerName, World world, double x, double y, double z, BlockFace blockFace, DrawerUpgrade drawerUpgrade, Material material, long amount);


    /**
     * Purges all Drawer entities from a world, with an option to destroy their blocks.
     * <p>
     * This method is used to remove all Drawer entities from a specified world.
     * It can also destroy the blocks associated with the Drawers if specified.
     * </p>
     *
     * @param sender       The CommandSender who is issuing the command.
     * @param world        The World from which to purge the Drawers.
     * @param destroyBlock A boolean indicating whether to destroy the block along with the Drawer entity.
     */
    void purgeWorld(CommandSender sender, World world, boolean destroyBlock);

    /**
     * Formats a number according to the plugin's formatting rules.
     *
     * @param number The number to format.
     * @param force  Force the format
     * @return A string representation of the formatted number.
     */
    String numberFormat(long number, boolean force);

    /**
     * Determines the position of a drawer relative to a given block face.
     * This can be used to understand how a drawer is oriented or positioned in the world,
     * which is particularly useful when interacting with or placing drawers programmatically.
     *
     * @param blockFace The face of the block relative to which the drawer's position is to be determined.
     *                  This could be one of the six basic directions (NORTH, EAST, SOUTH, WEST, UP, DOWN) in Minecraft.
     * @return The DrawerPosition representing the drawer's orientation or position relative to the specified block face.
     */
    DrawerPosition getDrawerPosition(BlockFace blockFace);

    /**
     * Retrieves the display size for the item within a drawer.
     *
     * @return The DisplaySize value representing the size of the item display.
     */
    DisplaySize getItemDisplaySize();

    /**
     * Retrieves the display size for the upgrade associated with a drawer.
     *
     * @return The DisplaySize value representing the size of the upgrade display.
     */
    DisplaySize getUpgradeDisplaySize();

    /**
     * Retrieves the display size for the text associated with a drawer.
     *
     * @return The DisplaySize value representing the size of the text display.
     */
    DisplaySize getTextDisplaySize();

    /**
     * Retrieves the storage mechanism being used.
     *
     * @return IStorage The storage interface being used for data management.
     */
    IStorage getStorage();

    /**
     * Retrieves a map of the current player drawers.
     * The map keys are player UUIDs, and the values are the corresponding Drawer objects.
     *
     * @return Map<UUID, Drawer> A map linking player UUIDs to their respective Drawer objects.
     */
    Map<UUID, Drawer> getCurrentPlayerDrawer();

    /**
     * Retrieves the namespace container.
     * This container may hold various namespaces used in the system.
     *
     * @return NamespaceContainer The container holding various namespaces.
     */
    NamespaceContainer getNamespaceContainer();

    /**
     * Retrieves the size configuration for a given type of Drawer.
     * <p>
     * This method returns the size specifications for a Drawer of the specified type.
     * The size can include dimensions or any other relevant size-related properties
     * that are defined for the DrawerType. This is useful for determining the
     * physical space or capacity characteristics of different types of Drawers.
     * </p>
     *
     * @param drawerType The {@link DrawerType} for which the size configuration is requested.
     * @return The {@link DrawerSize} representing the size of the specified DrawerType.
     */
    DrawerSize getSize(DrawerType drawerType);


}
