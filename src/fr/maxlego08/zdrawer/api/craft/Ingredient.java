package fr.maxlego08.zdrawer.api.craft;

import fr.maxlego08.menu.api.MenuItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Interface for defining ingredients used in crafting recipes.
 * Ingredients can be associated with either crafts or upgrades and can be represented as menu items.
 */
public interface Ingredient {

    /**
     * Gets the representation of this ingredient as a MenuItemStack.
     * MenuItemStacks are often used in custom GUIs to display items.
     *
     * @return The MenuItemStack representation of this ingredient.
     */
    MenuItemStack getMenuItemStack();

    /**
     * Gets the name of the craft associated with this ingredient, if applicable.
     *
     * @return The name of the craft associated with this ingredient, or null if not associated with a craft.
     */
    String getCraftName();

    /**
     * Gets the name of the upgrade associated with this ingredient, if applicable.
     *
     * @return The name of the upgrade associated with this ingredient, or null if not associated with an upgrade.
     */
    String getUpgradeName();

    /**
     * Checks if this ingredient is associated with a craft name.
     *
     * @return true if this ingredient is associated with a craft, false otherwise.
     */
    boolean isCraftName();

    /**
     * Checks if this ingredient is associated with an upgrade name.
     *
     * @return true if this ingredient is associated with an upgrade, false otherwise.
     */
    boolean isUpgradeName();

    /**
     * Builds and returns the actual ItemStack for this ingredient for a given player.
     * This allows for player-specific customization of ingredients.
     *
     * @param player The player for whom the ingredient ItemStack is being built.
     * @return The built ItemStack for this ingredient.
     */
    ItemStack build(Player player);

}
