package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerCase;
import fr.maxlego08.zdrawer.api.DrawerUpgrade;
import fr.maxlego08.zdrawer.api.configuration.DrawerBorder;
import fr.maxlego08.zdrawer.api.configuration.DrawerConfiguration;
import fr.maxlego08.zdrawer.api.configuration.DrawerSize;
import fr.maxlego08.zdrawer.api.configuration.DrawerSizeDirection;
import fr.maxlego08.zdrawer.api.utils.DisplaySize;
import fr.maxlego08.zdrawer.api.utils.DrawerPosition;
import fr.maxlego08.zdrawer.zcore.utils.ZUtils;
import fr.maxlego08.zdrawer.zcore.utils.nms.ItemStackUtils;
import net.kyori.adventure.text.Component;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ZDrawer extends ZUtils implements Drawer {

    private final DrawerPlugin plugin;
    private final DrawerConfiguration drawerConfiguration;
    private final Location location;
    private final BlockFace blockFace;
    private final List<ItemDisplay> borderDisplays = new ArrayList<>();
    private final List<DrawerCase> drawerCases = new ArrayList<>();
    private ItemDisplay upgradeDisplay;
    private DrawerUpgrade drawerUpgrade;

    public ZDrawer(DrawerPlugin plugin, DrawerConfiguration drawerConfiguration, Location location, BlockFace blockFace) {
        this.plugin = plugin;
        this.drawerConfiguration = drawerConfiguration;
        this.location = location;
        this.blockFace = blockFace;

        Block block = location.getBlock();
        block.setType(Material.BARREL);
        Barrel barrel = (Barrel) block.getBlockData();
        barrel.setFacing(blockFace.getOppositeFace());
        block.setBlockData(barrel, false);
        org.bukkit.block.Barrel blockBarrel = (org.bukkit.block.Barrel) block.getState();
        blockBarrel.getInventory().setItem(0, new ItemStack(Material.STONE, 2));

        if (!location.getChunk().isLoaded()) return;

        DrawerSize size = this.plugin.getManager().getSize(drawerConfiguration.getDrawerType());
        DrawerSizeDirection drawerSizeDirection = size.getDrawerSizeDirectionMap().get(blockFace);
        drawerSizeDirection.getPositions().forEach(position -> this.drawerCases.add(new ZDrawerCase(plugin, this)));

        this.spawnDisplay();
        this.spawnBorder(drawerConfiguration.getBorder());
    }

    public ZDrawer(DrawerPlugin plugin, Material material, long amount, DrawerUpgrade drawerUpgrade, DrawerConfiguration drawerConfiguration) {

        this.drawerConfiguration = null;
        this.plugin = null;
        this.location = null;
        this.blockFace = null;

        this.drawerUpgrade = drawerUpgrade;

        for (int index = 0; index < drawerConfiguration.getDrawerType().getSize(); index++) {
            this.drawerCases.add(new ZDrawerCase(plugin, this, amount, material));
        }
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
        drawerPosition.getUpgradeDisplay().apply(locationUpgradeDisplay);
        drawerPosition.getItemDisplay().apply(location);
        drawerPosition.getTextDisplay().apply(locationTextDisplay);

        World world = location.getWorld();

        this.upgradeDisplay = world.spawn(locationUpgradeDisplay, ItemDisplay.class, display -> {
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
            setSize(display, this.plugin.getManager().getUpgradeDisplaySize());
        });

        DrawerSize size = this.plugin.getManager().getSize(drawerConfiguration.getDrawerType());
        DrawerSizeDirection drawerSizeDirection = size.getDrawerSizeDirectionMap().get(blockFace);
        DisplaySize scale = drawerSizeDirection.getScale();

        this.drawerCases.forEach(DrawerCase::onDisable);

        List<DrawerSizeDirection.DrawerSizePosition> drawerSizePositions = drawerSizeDirection.getPositions();
        for (int index = 0; index < drawerSizePositions.size(); index++) {

            DrawerSizeDirection.DrawerSizePosition position = drawerSizePositions.get(index);
            DrawerCase drawerCase = this.drawerCases.get(index);

            Location currentLocationItem = location.clone();
            Location currentLocationText = locationTextDisplay.clone();

            position.getItemSize().apply(currentLocationItem);
            position.getTextSize().apply(currentLocationText);

            drawerCase.setItemDisplay(world.spawn(currentLocationItem, ItemDisplay.class, display -> {
                display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
                setSize(display, this.plugin.getManager().getItemDisplaySize(), scale);
            }));

            drawerCase.setTextDisplay(world.spawn(currentLocationText, TextDisplay.class, display -> {
                display.text(Component.text("0"));
                display.setAlignment(TextDisplay.TextAlignment.CENTER);
                setSize(display, this.plugin.getManager().getTextDisplaySize(), scale);
            }));

            drawerCase.updateText();
        }
    }

    private void setSize(Display display, DisplaySize displaySize) {
        setSize(display, displaySize, new DisplaySize(0, 0, 0));
    }

    private void setSize(Display display, DisplaySize displaySize, DisplaySize displayMultiplicator) {
        Transformation transformation = display.getTransformation();
        Vector3f scale = transformation.getScale();
        scale.set(displaySize.getX() * displayMultiplicator.getX(), displaySize.getY() * displayMultiplicator.getY(), displaySize.getZ() * displayMultiplicator.getZ());
        display.setBillboard(Display.Billboard.FIXED);
        display.setInvulnerable(true);
        display.setTransformation(transformation);
        display.setMetadata("zdrawer-entity", new FixedMetadataValue(this.plugin, true));
    }

    @Override
    public String getConfigurationName() {
        return this.drawerConfiguration.getName();
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
    public void setItemStack(int index, ItemStack itemStack) {
        DrawerCase drawerCase = this.drawerCases.get(index);
        drawerCase.setItemStack(itemStack);
    }

    @Override
    public void setAmount(int index, long amount) {
        DrawerCase drawerCase = this.drawerCases.get(index);
        drawerCase.setAmount(amount);
    }

    private Optional<DrawerCase> findNearestCase(Location location) {
        return this.drawerCases.stream().min(Comparator.comparingDouble(drawerCase -> drawerCase.getItemDisplay().getLocation().distanceSquared(location)));
    }

    @Override
    public void addItem(Player player, ItemStack itemStack, EquipmentSlot hand, Location position) {
        findNearestCase(position).ifPresent(drawerCase -> drawerCase.addItem(player, itemStack, hand));
    }

    @Override
    public void removeItem(Player player, Location position) {
        findNearestCase(position).ifPresent(drawerCase -> drawerCase.removeItem(player));
    }

    @Override
    public void remove(long amount) {
        System.out.println("TO UPDATE REMOVE AMOUNT");
    }

    @Override
    public void add(long amount) {
        System.out.println("TO UPDATE ADD AMOUNT");
    }

    @Override
    public void onDisable() {
        this.drawerCases.forEach(DrawerCase::onDisable);
        Optional.ofNullable(this.upgradeDisplay).ifPresent(Display::remove);
        this.borderDisplays.forEach(Display::remove);
    }

    @Override
    public void onLoad() {
        this.spawnDisplay();
        this.spawnBorder(this.drawerConfiguration.getBorder());
        Optional.ofNullable(this.drawerUpgrade).ifPresent(upgrade -> this.upgradeDisplay.setItemStack(upgrade.getDisplayItemStack()));
        this.drawerCases.forEach(DrawerCase::onLoad);
    }

    @Override
    public long getLimit() {
        return this.drawerUpgrade == null ? this.drawerConfiguration.getLimit() : this.drawerUpgrade.getLimit();
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
    public List<ItemDisplay> getBorderDisplays() {
        return this.borderDisplays;
    }

    @Override
    public String getData() {
        return this.drawerCases.stream()
                .map(drawerCase -> drawerCase.getItemStackAsString() + ";" + drawerCase.getAmount())
                .collect(Collectors.joining(",", this.drawerCases.size() + ",", ""));
    }

    @Override
    public boolean hasItemStack(int index) {
        DrawerCase drawerCase = this.drawerCases.get(index);
        return drawerCase.hasItemStack();
    }

    @Override
    public ItemStack getItemStack(int index) {
        DrawerCase drawerCase = this.drawerCases.get(index);
        return drawerCase.getItemStack();
    }

    @Override
    public long getAmount(int index) {
        DrawerCase drawerCase = this.drawerCases.get(index);
        return drawerCase.getAmount();
    }

    @Override
    public void load(String data) {
        String[] values = data.split(",");
        int dataAmount = Integer.parseInt(values[0]);
        for (int index = 1; index < dataAmount + 1; index++) {
            String[] currentValues = values[index].split(";");
            ItemStack itemStack = ItemStackUtils.deserializeItemStack(currentValues[0]);
            int amount = Integer.parseInt(currentValues[1]);
            setItemStack(index - 1, itemStack);
            setAmount(index - 1, amount);
        }
    }
}
