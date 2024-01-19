package fr.maxlego08.zdrawer.storage;

import org.bukkit.block.BlockFace;

public class DrawerContainer {

    private final String location;
    private final BlockFace blockFace;
    private final String itemStack;
    private final long amount;

    public DrawerContainer(String location, BlockFace blockFace, String itemStack, long amount) {
        this.location = location;
        this.blockFace = blockFace;
        this.itemStack = itemStack;
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

    @Override
    public String toString() {
        return "DrawerContainer{" +
                "location='" + location + '\'' +
                ", blockFace=" + blockFace +
                ", itemStack='" + itemStack + '\'' +
                ", amount=" + amount +
                '}';
    }
}
