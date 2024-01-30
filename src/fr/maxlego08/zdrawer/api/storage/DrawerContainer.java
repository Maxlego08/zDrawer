package fr.maxlego08.zdrawer.api.storage;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.zcore.utils.nms.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class DrawerContainer {

    private final String location;
    private final BlockFace blockFace;
    private final String drawerName;
    private final String data;
    private final String upgrade;

    public DrawerContainer(String location, BlockFace blockFace, String drawerName, String data, String upgrade) {
        this.location = location;
        this.blockFace = blockFace;
        this.drawerName = drawerName;
        this.data = data;
        this.upgrade = upgrade;
    }

    public String getLocation() {
        return location;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public String getDrawerName() {
        return drawerName;
    }

    public String getData() {
        return data;
    }

    public String getUpgrade() {
        return upgrade;
    }

    public boolean isWorldLoaded() {
        return Bukkit.getWorld(getWorldName()) != null;
    }

    public String getWorldName() {
        String[] locationArray = this.location.split(",");
        return locationArray[0];
    }

    public boolean hasUpgrade() {
        return this.upgrade != null;
    }

    public boolean hasData() {
        return this.data != null;
    }

    public void loadData(Drawer drawer) {
        String[] values = this.data.split(",");
        int dataAmount = Integer.parseInt(values[0]);
        for (int index = 1; index < dataAmount + 1; index++) {
            String[] currentValues = values[index].split(";");
            ItemStack itemStack = ItemStackUtils.deserializeItemStack(currentValues[0]);
            int amount = Integer.parseInt(currentValues[1]);
            drawer.setItemStack(index - 1, itemStack);
            drawer.setAmount(index - 1, amount);
        }
    }
}
