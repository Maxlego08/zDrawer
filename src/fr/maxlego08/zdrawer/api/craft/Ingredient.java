package fr.maxlego08.zdrawer.api.craft;

import fr.maxlego08.menu.MenuItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Ingredient {

    MenuItemStack getMenuItemStack();

    String getCraftName();

    String getUpgradeName();

    boolean isCraftName();

    boolean isUpgradeName();

    ItemStack build(Player player);

}
