package fr.maxlego08.zdrawer.storage;

import org.bukkit.block.BlockFace;

public class DrawerContainer {

    private final String location;
    private final BlockFace blockFace;
    private final String itemStack;
    private final String upgrade;
    private final long amount;

    public DrawerContainer(String location, BlockFace blockFace, String itemStack, String upgrade, long amount) {
        this.location = location;
        this.blockFace = blockFace;
        this.itemStack = itemStack;
        this.upgrade = upgrade;
        this.amount = amount;
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

    @Override
    public String toString() {
        return "DrawerContainer{" +
                "location='" + location + '\'' +
                ", blockFace=" + blockFace +
                ", itemStack='" + itemStack + '\'' +
                ", upgrade='" + upgrade + '\'' +
                ", amount=" + amount +
                '}';
    }

    public boolean hasUpgrade() {
        return this.upgrade != null;
    }
}
