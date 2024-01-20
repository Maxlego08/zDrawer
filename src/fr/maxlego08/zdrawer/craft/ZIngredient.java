package fr.maxlego08.zdrawer.craft;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.craft.Craft;
import fr.maxlego08.zdrawer.api.craft.Ingredient;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ZIngredient implements Ingredient {

    private final DrawerPlugin plugin;
    private final MenuItemStack menuItemStack;
    private final String craftName;

    public ZIngredient(DrawerPlugin plugin, MenuItemStack menuItemStack) {
        this.plugin = plugin;
        this.menuItemStack = menuItemStack;
        this.craftName = null;
    }

    public ZIngredient(DrawerPlugin plugin, String craftName) {
        this.plugin = plugin;
        this.menuItemStack = null;
        this.craftName = craftName;
    }

    @Override
    public MenuItemStack getMenuItemStack() {
        return this.menuItemStack;
    }

    @Override
    public String getCraftName() {
        return this.craftName;
    }

    @Override
    public boolean isCraftName() {
        return this.craftName != null;
    }

    @Override
    public ItemStack build(Player player) {
        if (isCraftName()) {
            Optional<Craft> optional = this.plugin.getManager().getCraft(this.craftName);
            return optional.isPresent() ? optional.get().getResultItemStack(player) : new ItemStack(Material.BARRIER);
        }
        return this.menuItemStack.build(player, false);
    }
}
