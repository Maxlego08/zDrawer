package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerBorder;
import fr.maxlego08.zdrawer.api.DrawerUpgrade;
import fr.maxlego08.zdrawer.api.utils.DrawerPosition;
import fr.maxlego08.zdrawer.zcore.utils.ZUtils;
import fr.maxlego08.zdrawer.zcore.utils.nms.ItemStackUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Barrel;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ZDrawer extends ZUtils implements Drawer {

    private final DrawerPlugin plugin;
    private final Location location;
    private final BlockFace blockFace;
    private ItemStack itemStack; // Use Material for the DEMO, but the real will use ItemStack
    private long amount;
    private ItemDisplay itemDisplay;
    private ItemDisplay upgradeDisplay;
    private TextDisplay textDisplay;
    private DrawerUpgrade drawerUpgrade;
    private final List<ItemDisplay> borderDisplays = new ArrayList<>();

    public ZDrawer(DrawerPlugin plugin, Location location, BlockFace blockFace) {
        this.plugin = plugin;
        this.location = location;
        this.blockFace = blockFace;
        this.spawnDisplay();
        this.spawnBorder(plugin.getManager().getBorder());

        Block block = location.getBlock();
        block.setType(Material.BARREL);
        Barrel barrel = (Barrel) block.getBlockData();
        barrel.setFacing(blockFace.getOppositeFace());
        block.setBlockData(barrel, false);
        org.bukkit.block.Barrel blockBarrel = (org.bukkit.block.Barrel) block.getState();
        blockBarrel.getInventory().setItem(0, new ItemStack(Material.STONE, 2));
    }

    public ZDrawer(Material material, long amount, DrawerUpgrade drawerUpgrade) {

        this.plugin = null;
        this.location = null;
        this.blockFace = null;

        this.itemStack = material == null ? null : new ItemStack(material);
        this.amount = amount;
        this.drawerUpgrade = drawerUpgrade;
    }

    public void spawnBorder(DrawerBorder drawerBorder) {
        if (!drawerBorder.isEnable()) return;
        DrawerPosition drawerPosition = this.plugin.getManager().getDrawerPosition(blockFace);
        drawerPosition.getBorderPositions().spawn(this.plugin, this, drawerBorder);
    }

    private void spawnDisplay() {

        Location location = this.location.clone();
        Location locationUpgradeDisplay = this.location.clone();
        Location locationTextDisplay = this.location.clone();

        DrawerPosition drawerPosition = this.plugin.getManager().getDrawerPosition(blockFace);
        drawerPosition.getItemDisplay().apply(location);
        drawerPosition.getTextDisplay().apply(locationTextDisplay);
        drawerPosition.getUpgradeDisplay().apply(locationUpgradeDisplay);

        World world = location.getWorld();

        this.itemDisplay = world.spawn(location, ItemDisplay.class, display -> {
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
            setSize(display, 0.6f, 0.6f, 0.01f);
        });

        this.upgradeDisplay = world.spawn(locationUpgradeDisplay, ItemDisplay.class, display -> {
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
            setSize(display, 0.15f, 0.15f, 0.01f);
        });

        this.textDisplay = world.spawn(locationTextDisplay, TextDisplay.class, display -> {
            display.setAlignment(TextDisplay.TextAlignment.CENTER);
            setSize(display, 0.6, 0.6, 0.6);
        });

        updateText();
    }

    private void setSize(Display display, double x, double y, double z) {
        Transformation transformation = display.getTransformation();
        Vector3f scale = transformation.getScale();
        scale.set(x, y, z);
        display.setBillboard(Display.Billboard.FIXED);
        display.setInvulnerable(true);
        display.setTransformation(transformation);
        display.setMetadata("zdrawer-entity", new FixedMetadataValue(this.plugin, true));
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemDisplay.setItemStack(itemStack);
    }

    @Override
    public String getItemStackAsString() {
        return ItemStackUtils.serializeItemStack(this.itemStack);
    }

    @Override
    public long getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
        updateText();
    }

    @Override
    public void updateText() {
        this.textDisplay.setText(this.plugin.getManager().numberFormat(amount));
        TextDisplayUtil.setDisplayedText(this.textDisplay, this.plugin.getManager().numberFormat(amount));
    }

    @Override
    public ItemDisplay getItemDisplay() {
        return this.itemDisplay;
    }

    @Override
    public TextDisplay getTextDisplay() {
        return this.textDisplay;
    }

    @Override
    public void addItem(Player player, ItemStack itemStack, EquipmentSlot hand) {

        if (isFull()) return;

        // If the item does not exist, then we will create it
        if (this.itemStack == null) {

            this.itemStack = itemStack;
            this.itemDisplay.setItemStack(itemStack);

            if (this.hasLimit()) {

                this.amount = Math.min(itemStack.getAmount(), this.getLimit());
                if (this.amount == itemStack.getAmount()) {
                    player.getInventory().setItem(hand, new ItemStack(Material.AIR));
                } else itemStack.setAmount((int) (itemStack.getAmount() - this.amount));
            } else {

                this.amount = itemStack.getAmount();
                player.getInventory().setItem(hand, new ItemStack(Material.AIR));
            }

            updateText();

        } else if (itemStack.isSimilar(this.itemStack)) {

            /// We will retrieve all the items that are similar to add them
            if (player.isSneaking()) {

                PlayerInventory inventory = player.getInventory();
                for (int slot = 0; slot != 36; slot++) {

                    ItemStack currentItemStack = inventory.getItem(slot);
                    if (currentItemStack != null && this.itemStack.isSimilar(currentItemStack)) {
                        int addAmount = (int) Math.min(currentItemStack.getAmount(), this.getLimit() - this.amount);
                        this.amount += addAmount;
                        if (addAmount < currentItemStack.getAmount()) {
                            currentItemStack.setAmount(currentItemStack.getAmount() - addAmount);
                            inventory.setItem(slot, currentItemStack);
                            break; // Stop the loop if maxAmount is reached
                        } else {
                            inventory.setItem(slot, new ItemStack(Material.AIR));
                        }
                    }

                }
                updateText();

            } else {

                int addAmount = (int) Math.min(itemStack.getAmount(), this.getLimit() - this.amount);
                this.amount += addAmount;
                updateText();
                if (addAmount < itemStack.getAmount()) {
                    itemStack.setAmount(itemStack.getAmount() - addAmount);
                    player.getInventory().setItem(hand, itemStack);
                } else {
                    player.getInventory().setItem(hand, new ItemStack(Material.AIR));
                }
            }
        }
    }

    @Override
    public void removeItem(Player player) {

        if (this.itemStack == null) return;

        if (player.isSneaking()) {

            int itemStackAmount = (int) Math.min(this.amount, itemStack.getMaxStackSize());
            ItemStack itemStack = this.itemStack.clone();
            itemStack.setAmount(itemStackAmount);
            remove(itemStack.getMaxStackSize());
            give(player, itemStack);

        } else {

            ItemStack itemStack = this.itemStack.clone();
            itemStack.setAmount(1);
            remove(1);

            give(player, itemStack);
        }
    }

    @Override
    public void remove(long amount) {
        setAmount(this.amount - amount);

        if (this.amount <= 0) {

            this.itemStack = null;
            this.amount = 0;
            this.itemDisplay.setItemStack(new ItemStack(Material.AIR));
        }

        updateText();
    }

    @Override
    public void add(long amount) {
        setAmount(this.amount + amount);
    }

    @Override
    public void onDisable() {
        this.textDisplay.remove();
        this.itemDisplay.remove();
        this.upgradeDisplay.remove();
        this.borderDisplays.forEach(Display::remove);
    }

    @Override
    public boolean hasItemStack() {
        return this.itemStack != null;
    }

    @Override
    public long getLimit() {
        return this.drawerUpgrade == null ? this.plugin.getManager().getDrawerLimit() : this.drawerUpgrade.getLimit();
    }

    @Override
    public boolean hasLimit() {
        return this.getLimit() > 0;
    }

    @Override
    public DrawerUpgrade getUpgrade() {
        return this.drawerUpgrade;
    }

    @Override
    public void setUpgrade(DrawerUpgrade drawerUpgrade) {
        this.drawerUpgrade = drawerUpgrade;
        this.upgradeDisplay.setItemStack(drawerUpgrade.getDisplayItemStack());
    }

    @Override
    public boolean hasUpgrade() {
        return this.drawerUpgrade != null;
    }

    @Override
    public String getUpgradeName() {
        return this.drawerUpgrade == null ? null : this.drawerUpgrade.getName();
    }

    @Override
    public boolean isFull() {
        return this.hasLimit() && this.amount >= this.getLimit();
    }

    @Override
    public List<ItemDisplay> getBorderDisplays() {
        return this.borderDisplays;
    }
}
