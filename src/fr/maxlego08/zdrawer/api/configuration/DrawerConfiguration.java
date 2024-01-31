package fr.maxlego08.zdrawer.api.configuration;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.zdrawer.api.craft.Craft;
import fr.maxlego08.zdrawer.api.enums.DrawerType;

/**
 * Interface for configuring a Drawer.
 * <p>
 * This interface provides methods to access various configurations for a Drawer.
 * Each method allows the retrieval of a specific property of the Drawer configuration,
 * such as its type, limit, menu item, border, crafting details, name, and hopper interaction.
 * </p>
 */
public interface DrawerConfiguration {

    /**
     * Returns the type of the Drawer.
     *
     * @return the {@link DrawerType} representing the type of the Drawer.
     */
    DrawerType getDrawerType();

    /**
     * Retrieves the item limit for the Drawer.
     * <p>
     * This limit defines the maximum number of items the Drawer can hold.
     * </p>
     *
     * @return a long value representing the item limit.
     */
    long getLimit();

    /**
     * Gets the MenuItemStack associated with the Drawer.
     * <p>
     * This stack is used to represent the Drawer in various UI elements.
     * </p>
     *
     * @return the {@link MenuItemStack} associated with this Drawer.
     */
    MenuItemStack getMenuItemStack();

    /**
     * Returns the border configuration of the Drawer.
     * <p>
     * The border configuration defines the visual and interactive boundary of the Drawer.
     * </p>
     *
     * @return the {@link DrawerBorder} representing the border of the Drawer.
     */
    DrawerBorder getBorder();

    /**
     * Retrieves the crafting configuration for the Drawer.
     * <p>
     * This includes details about how the Drawer can be crafted within the game.
     * </p>
     *
     * @return the {@link Craft} object representing the crafting details of the Drawer.
     */
    Craft getCraft();

    /**
     * Gets the name of the Drawer.
     * <p>
     * The name is typically used for identification and display purposes.
     * </p>
     *
     * @return a String representing the name of the Drawer.
     */
    String getName();

    /**
     * Determines whether hopper interactions are disabled for the Drawer.
     * <p>
     * If true, hoppers will not be able to interact with this Drawer.
     * </p>
     *
     * @return true if hopper interactions are disabled, false otherwise.
     */
    boolean isDisableHopper();
}

