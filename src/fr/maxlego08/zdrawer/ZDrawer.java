package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerUpgrade;
import fr.maxlego08.zdrawer.zcore.utils.ZUtils;
import fr.maxlego08.zdrawer.zcore.utils.nms.ItemStackUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

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

    public ZDrawer(DrawerPlugin plugin, Location location, BlockFace blockFace) {
        this.plugin = plugin;
        this.location = location;
        this.blockFace = blockFace;
        this.spawnDisplay();

        Block block = location.getBlock();
        block.setType(Material.BARREL);
        Barrel barrel = (Barrel) block.getBlockData();
        barrel.setFacing(blockFace.getOppositeFace());
        block.setBlockData(barrel, false);
    }

    public ZDrawer(Material material, long amount, DrawerUpgrade drawerUpgrade) {

        this.plugin = null;
        this.location = null;
        this.blockFace = null;

        this.itemStack = material == null ? null : new ItemStack(material);
        this.amount = amount;
        this.drawerUpgrade = drawerUpgrade;
    }

    private void spawnDisplay() {

        Location location = this.location.clone();
        Location locationUpgradeDisplay = this.location.clone();
        Location locationTextDisplay = this.location.clone();

        location.setPitch(0f);
        locationTextDisplay.setPitch(0f);
        locationUpgradeDisplay.setPitch(0f);

        switch (blockFace) {
            case NORTH:
                location.add(0.5, 0.5, 1.01);
                locationTextDisplay.add(0.5, 0.05, 1.02);
                locationUpgradeDisplay.add(0.5, 0.9, 1.03);
                break;
            case WEST:
                location.add(1.01, 0.5, 0.5);
                locationTextDisplay.add(1.02, 0.05, 0.5);
                locationUpgradeDisplay.add(1.03, 0.9, 0.5);
                break;
            case EAST:
                location.add(-0.01, 0.5, 0.5);
                locationTextDisplay.add(-0.02, 0.05, 0.5);
                locationUpgradeDisplay.add(-0.02, 0.9, 0.5);
                break;
            case DOWN:
                location.add(0.5, 1.01, 0.5);
                locationTextDisplay.add(0.03, 1.02, 0.5);
                locationUpgradeDisplay.add(0.9, 1.02, 0.5);
                location.setPitch(90f);
                locationUpgradeDisplay.setPitch(90f);
                locationTextDisplay.setPitch(-90f);
                break;
            case UP:
                location.add(0.5, -0.01, 0.5);
                locationTextDisplay.add(0.97, -0.02, 0.5);
                locationUpgradeDisplay.add(0.1, -0.02, 0.5);
                location.setPitch(-90f);
                locationUpgradeDisplay.setPitch(-90f);
                locationTextDisplay.setPitch(90f);
                break;
            default:
                location.add(0.5, 0.5, -0.01);
                locationTextDisplay.add(0.5, 0.05, -0.02);
                locationUpgradeDisplay.add(0.5, 0.9, -0.03);
                break;
        }

        updateYaw(location, blockFace);
        updateYaw(locationUpgradeDisplay, blockFace);
        updateYaw(locationTextDisplay, blockFace.getOppositeFace());

        World world = location.getWorld();

        this.itemDisplay = world.spawn(location, ItemDisplay.class, display -> {
            display.setBillboard(Display.Billboard.FIXED);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
            Transformation transformation = display.getTransformation();
            Vector3f scale = transformation.getScale();
            scale.set(0.6f, 0.6f, 0.01f);
            display.setTransformation(transformation);
            display.setInvulnerable(true);
        });

        this.upgradeDisplay = world.spawn(locationUpgradeDisplay, ItemDisplay.class, display -> {
            display.setBillboard(Display.Billboard.FIXED);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
            Transformation transformation = display.getTransformation();
            Vector3f scale = transformation.getScale();
            scale.set(0.15f, 0.15f, 0.01f);
            display.setTransformation(transformation);
            display.setInvulnerable(true);
        });

        this.textDisplay = world.spawn(locationTextDisplay, TextDisplay.class, display -> {

            display.setInvulnerable(true);
            display.setBillboard(Display.Billboard.FIXED);
            display.setAlignment(TextDisplay.TextAlignment.CENTER);
            Transformation transformation = display.getTransformation();
            Vector3f scale = transformation.getScale();
            double size = 0.6;
            scale.set(size, size, size);
            display.setTransformation(transformation);
        });

    }

    private void updateYaw(Location location, BlockFace blockFace) {
        float yaw;
        switch (blockFace) {
            case NORTH:
                yaw = -180f;
                break;
            case WEST:
            case UP:
            case DOWN:
                yaw = 90f;
                break;
            case EAST:
                yaw = -90f;
                break;
            default:
                yaw = 0f;
        }
        location.setYaw(yaw);
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
        this.textDisplay.text(Component.text(String.valueOf(amount), NamedTextColor.WHITE));
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

            textDisplay.text(Component.text(String.valueOf(this.amount), NamedTextColor.WHITE));

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
                textDisplay.text(Component.text(String.valueOf(this.amount), NamedTextColor.WHITE));

            } else {

                int addAmount = (int) Math.min(itemStack.getAmount(), this.getLimit() - this.amount);
                this.amount += addAmount;
                textDisplay.text(Component.text(String.valueOf(this.amount), NamedTextColor.WHITE));
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

            int itemStackAmount = (int) Math.min(this.amount, 64);
            ItemStack itemStack = this.itemStack.clone();
            itemStack.setAmount(itemStackAmount);
            this.amount -= itemStackAmount;

            give(player, itemStack);

        } else {

            ItemStack itemStack = this.itemStack.clone();
            itemStack.setAmount(1);
            this.amount -= 1;

            give(player, itemStack);
        }

        // If there is no more item, then it is deleted
        if (this.amount <= 0) {

            this.itemStack = null;
            this.amount = 0;
            this.itemDisplay.setItemStack(new ItemStack(Material.AIR));
            this.textDisplay.text(Component.text("", NamedTextColor.WHITE));

        } else {
            this.textDisplay.text(Component.text(String.valueOf(this.amount), NamedTextColor.WHITE));
        }
    }

    @Override
    public void onDisable() {
        this.textDisplay.remove();
        this.itemDisplay.remove();
        this.upgradeDisplay.remove();
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
}
