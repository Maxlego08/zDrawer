package fr.maxlego08.zdrawer.api;

import fr.maxlego08.zdrawer.api.craft.Craft;
import fr.maxlego08.zdrawer.api.storage.IStorage;
import fr.maxlego08.zdrawer.api.storage.Savable;
import fr.maxlego08.zdrawer.api.utils.DisplaySize;
import fr.maxlego08.zdrawer.api.utils.DrawerPosition;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
     * Gets the global limit for the number of items a single drawer can hold.
     *
     * @return The maximum number of items allowed in a drawer.
     */
    long getDrawerLimit();

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
     * Gives a customized drawer item to a player.
     *
     * @param sender        The command sender who initiates the action.
     * @param player        The player who will receive the drawer.
     * @param drawerUpgrade The upgrade applied to the drawer, if any.
     * @param material      The material of the drawer.
     * @param amount        The amount of the item to be given.
     */
    void giveDrawer(CommandSender sender, Player player, DrawerUpgrade drawerUpgrade, Material material, Long amount);

    /**
     * Constructs a drawer ItemStack based on the specified parameters.
     *
     * @param player The player for whom the drawer is being built.
     * @param drawer The drawer configuration.
     * @return The ItemStack representing the built drawer.
     */
    ItemStack buildDrawer(Player player, Drawer drawer);

    /**
     * Gets a list of all available upgrade names.
     *
     * @return A list of upgrade names.
     */
    List<String> getUpgradeNames();

    /**
     * Gives a craft item associated with a specified craft name to a player.
     *
     * @param sender    The command sender who initiates the action.
     * @param player    The player who will receive the craft item.
     * @param craftName The name of the craft recipe.
     */
    void giveCraft(CommandSender sender, Player player, String craftName);

    /**
     * Gets a list of all available craft names.
     *
     * @return A list of craft names.
     */
    List<String> getCraftNames();

    /**
     * Places a drawer in the world at the specified location with the given parameters.
     *
     * @param sender        The command sender who initiates the action.
     * @param world         The world where the drawer will be placed.
     * @param x             The x-coordinate where the drawer will be placed.
     * @param y             The y-coordinate where the drawer will be placed.
     * @param z             The z-coordinate where the drawer will be placed.
     * @param blockFace     The face of the block against which the drawer is placed.
     * @param drawerUpgrade The upgrade applied to the drawer, if any.
     * @param material      The material of the drawer.
     * @param amount        The amount of the item to be placed inside the drawer.
     */
    void placeDrawer(CommandSender sender, World world, double x, double y, double z, BlockFace blockFace, DrawerUpgrade drawerUpgrade, Material material, long amount);

    /**
     * Purges all drawers in a specified world, effectively removing them.
     *
     * @param sender The command sender who initiates the purge.
     * @param world  The world where drawers will be purged.
     */
    void purgeWorld(CommandSender sender, World world);

    /**
     * Formats a number according to the plugin's formatting rules.
     *
     * @param number The number to format.
     * @return A string representation of the formatted number.
     */
    String numberFormat(long number);

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


    IStorage getStorage();

    Map<UUID, Drawer> getCurrentPlayerDrawer();

    NamespacedKey getDataKeyItemStack();

    NamespacedKey getDataKeyDrawer();

    NamespacedKey getDataKeyAmount();

    NamespacedKey getDataKeyUpgrade();
}
