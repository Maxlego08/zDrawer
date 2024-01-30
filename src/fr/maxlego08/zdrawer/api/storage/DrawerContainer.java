package fr.maxlego08.zdrawer.api.storage;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;

public class DrawerContainer {

    private final String location;
    private final BlockFace blockFace;
    private final String drawerName;
    private final String itemStack;
    private final String upgrade;
    private final long amount;

    public DrawerContainer(String location, BlockFace blockFace, String drawerName, String itemStack, String upgrade, long amount) {
        this.location = location;
        this.blockFace = blockFace;
        this.drawerName = drawerName;
        this.itemStack = itemStack;
        this.upgrade = upgrade;
        this.amount = amount;
    }

    public String getDrawerName() {
        return drawerName;
    }

    public String getLocation() {
        return this.location;
    }

    public String getItemStack() {
        return this.itemStack;
    }

    public long getAmount() {
        return this.amount;
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    public boolean hasItemStack() {
        return this.itemStack != null;
    }

    public String getUpgrade() {
        return this.upgrade;
    }

    public boolean hasUpgrade() {
        return this.upgrade != null;
    }

    public boolean isWorldLoaded() {
        return Bukkit.getWorld(getWorldName()) != null;
    }

    public String getWorldName() {
        String[] locationArray = this.location.split(",");
        return locationArray[0];
    }
}
