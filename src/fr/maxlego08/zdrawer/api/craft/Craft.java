package fr.maxlego08.zdrawer.api.craft;

import fr.maxlego08.menu.MenuItemStack;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public interface Craft {

    String getName();

    NamespacedKey getKey();

    void register();

    void unregister();

    String[] getShade();

    Map<Character, Ingredient> getIngredients();

    MenuItemStack getResult();

    ItemStack getResultItemStack(Player player);

    boolean isEnable();

}
