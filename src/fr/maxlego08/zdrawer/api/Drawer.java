package fr.maxlego08.zdrawer.api;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public interface Drawer {

    Location getLocation();

    BlockFace getBlockFace();

    ItemStack getItemStack();

    void setItemStack(ItemStack itemStack);

    String getItemStackAsString();

    long getAmount();

    void setAmount(long amount);

    ItemDisplay getItemDisplay();

    TextDisplay getTextDisplay();

    void addItem(Player player, ItemStack itemStack, EquipmentSlot hand);

    void removeItem(Player player);

    void onDisable();

    boolean hasItemStack();

    long getLimit();

    boolean hasLimit();

    DrawerUpgrade getUpgrade();

    String getUpgradeName();

    void setUpgrade(DrawerUpgrade drawerUpgrade);

    boolean hasUpgrade();
}
