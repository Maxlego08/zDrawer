package fr.maxlego08.zdrawer.api.craft;

import fr.maxlego08.menu.MenuItemStack;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Interface for defining and managing custom crafting recipes.
 */
public interface Craft {

    /**
     * Gets the name of the craft.
     *
     * @return The name of the craft.
     */
    String getName();

    /**
     * Gets the unique identifier (key) for this craft.
     *
     * @return The NamespacedKey that uniquely identifies this craft.
     */
    NamespacedKey getKey();

    /**
     * Registers the craft with the crafting system.
     * This method should make the craft available for use within the game.
     */
    void register();

    /**
     * Unregisters the craft from the crafting system.
     * This method should remove the craft from availability within the game.
     */
    void unregister();

    /**
     * Gets the shape definition of this craft.
     * The shape is defined by an array of strings where each string represents a row in the crafting grid.
     *
     * @return An array of strings representing the shape of the craft.
     */
    String[] getShade();

    /**
     * Gets a map of ingredient keys to their corresponding ingredients.
     * The keys in the map correspond to characters used in the shape definition of the craft.
     *
     * @return A map where each key (character) is mapped to an Ingredient.
     */
    Map<Character, Ingredient> getIngredients();

    /**
     * Gets the result of the craft as a MenuItemStack.
     * This is typically used for displaying the crafting result in custom menus or GUIs.
     *
     * @return The crafting result as a MenuItemStack.
     */
    MenuItemStack getResult();

    /**
     * Gets the actual ItemStack result of the craft for a given player.
     * This method can be used to provide player-specific crafting results.
     *
     * @param player The player for whom the crafting result is being generated.
     * @return The ItemStack result of the craft.
     */
    ItemStack getResultItemStack(Player player);

    /**
     * Checks if the craft is enabled and available for use.
     *
     * @return true if the craft is enabled, false otherwise.
     */
    boolean isEnable();
}
