package fr.maxlego08.zdrawer.api;

import fr.maxlego08.zdrawer.api.craft.Craft;
import org.bukkit.inventory.ItemStack;

public interface DrawerUpgrade {

    String getName();

    Craft getCraft();

    ItemStack getDisplayItemStack();

    long getLimit();

    String getDisplayName();

}
