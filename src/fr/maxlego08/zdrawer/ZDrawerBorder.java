package fr.maxlego08.zdrawer;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.zdrawer.api.DrawerBorder;
import fr.maxlego08.zdrawer.api.utils.DisplaySize;
import org.bukkit.configuration.file.YamlConfiguration;

public class ZDrawerBorder implements DrawerBorder {

    private final boolean enable;
    private final MenuItemStack itemStack;
    private final DisplaySize up;
    private final DisplaySize down;
    private final DisplaySize left;
    private final DisplaySize right;

    public ZDrawerBorder(boolean enable, MenuItemStack itemStack, DisplaySize up, DisplaySize down, DisplaySize left, DisplaySize right) {
        this.enable = enable;
        this.itemStack = itemStack;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
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
