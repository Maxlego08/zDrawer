package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.api.DrawerUpgrade;
import fr.maxlego08.zdrawer.api.craft.Craft;
import org.bukkit.inventory.ItemStack;

public class ZDrawerUpgrade implements DrawerUpgrade {

    private final String name;
    private final Craft craft;
    private final long limit;
    private final ItemStack displayItemStack;

    public ZDrawerUpgrade(String name, Craft craft, long limit, ItemStack displayItemStack) {
        this.name = name;
        this.craft = craft;
        this.limit = limit;
        this.displayItemStack = displayItemStack;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Craft getCraft() {
        return craft;
    }

    @Override
    public long getLimit() {
        return limit;
    }

    @Override
    public ItemStack getDisplayItemStack() {
        return displayItemStack;
    }
}
