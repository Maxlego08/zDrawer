package fr.maxlego08.zdrawer.configuration;

import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.zdrawer.api.configuration.DrawerBorder;
import fr.maxlego08.zdrawer.api.utils.DisplaySize;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ZDrawerBorder implements DrawerBorder {

    private final boolean enable;
    private final MenuItemStack itemStack;
    private final DisplaySize up;
    private final DisplaySize down;
    private final DisplaySize left;
    private final DisplaySize right;

    public ZDrawerBorder(YamlConfiguration configuration, String path, InventoryManager inventoryManager, File file) {
        this.enable = configuration.getBoolean(path + "enable");
        this.itemStack = inventoryManager.loadItemStack(configuration, path + "item.", file);
        this.up = new DisplaySize(configuration, path + "scale.up.");
        this.down = new DisplaySize(configuration, path + "scale.down.");
        this.left = new DisplaySize(configuration, path + "scale.left.");
        this.right = new DisplaySize(configuration, path + "scale.right.");
    }

    public MenuItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public MenuItemStack getMenuItemStack() {
        return this.itemStack;
    }

    @Override
    public DisplaySize getUpScale() {
        return this.up;
    }

    @Override
    public DisplaySize getDownScale() {
        return this.down;
    }

    @Override
    public DisplaySize getLeftScale() {
        return this.left;
    }

    @Override
    public DisplaySize getRightScale() {
        return this.right;
    }
}
