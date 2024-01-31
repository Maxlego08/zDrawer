package fr.maxlego08.zdrawer.api.configuration;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.zdrawer.api.utils.DisplaySize;

/**
 * Interface for configuring the border of a Drawer.
 * <p>
 * This interface defines methods for enabling or disabling the border,
 * specifying the appearance of the border through a MenuItemStack,
 * and setting the scaling of the border on different sides.
 * </p>
 */
public interface DrawerBorder {

    /**
     * Determines if the border is enabled for the Drawer.
     * <p>
     * When enabled, the border is displayed around the Drawer.
     * When disabled, the Drawer will have no visible border.
     * </p>
     *
     * @return true if the border is enabled, false otherwise.
     */
    boolean isEnable();

    /**
     * Retrieves the MenuItemStack that represents the appearance of the Drawer border.
     * <p>
     * This stack is used to visually represent the border in user interfaces.
     * </p>
     *
     * @return the {@link MenuItemStack} representing the appearance of the border.
     */
    MenuItemStack getMenuItemStack();

    /**
     * Gets the scaling configuration for the upper border of the Drawer.
     * <p>
     * This defines how the upper border should be scaled in terms of its display size.
     * </p>
     *
     * @return the {@link DisplaySize} representing the scaling of the upper border.
     */
    DisplaySize getUpScale();

    /**
     * Gets the scaling configuration for the lower border of the Drawer.
     * <p>
     * This defines how the lower border should be scaled in terms of its display size.
     * </p>
     *
     * @return the {@link DisplaySize} representing the scaling of the lower border.
     */
    DisplaySize getDownScale();

    /**
     * Gets the scaling configuration for the left border of the Drawer.
     * <p>
     * This defines how the left border should be scaled in terms of its display size.
     * </p>
     *
     * @return the {@link DisplaySize} representing the scaling of the left border.
     */
    DisplaySize getLeftScale();

    /**
     * Gets the scaling configuration for the right border of the Drawer.
     * <p>
     * This defines how the right border should be scaled in terms of its display size.
     * </p>
     *
     * @return the {@link DisplaySize} representing the scaling of the right border.
     */
    DisplaySize getRightScale();

}

