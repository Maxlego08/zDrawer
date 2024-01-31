package fr.maxlego08.zdrawer.api;

import fr.maxlego08.zdrawer.api.craft.Craft;
import org.bukkit.inventory.ItemStack;

public interface DrawerUpgrade {

    /**
     * Gets the unique name identifier for this upgrade.
     *
     * @return The name of the upgrade.
     */
    String getName();

    /**
     * Gets the crafting recipe associated with this upgrade.
     *
     * @return The Craft object representing the crafting recipe for this upgrade.
     */
    Craft getCraft();

    /**
     * Gets the ItemStack used to display this upgrade in GUIs or menus.
     *
     * @return The display ItemStack for this upgrade.
     */
    ItemStack getDisplayItemStack();

    /**
     * Gets the additional limit this upgrade adds to a drawer.
     * This limit will be the new limit of the drawer
     *
     * @return The additional item limit provided by this upgrade.
     */
    long getLimit();

    /**
     * Gets the display name of this upgrade, which may include formatting or color codes.
     *
     * @return The formatted display name of the upgrade.
     */
    String getDisplayName();

}
