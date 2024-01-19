package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.storage.IStorage;
import fr.maxlego08.zdrawer.listener.ListenerAdapter;
import fr.maxlego08.zdrawer.save.Config;
import fr.maxlego08.zdrawer.zcore.utils.nms.ItemStackUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Barrel;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class ZDrawerManager extends ListenerAdapter implements DrawerManager {

    private final DrawerPlugin plugin;
    private final NamespacedKey DATA_KEY_ITEMSTACK;
    private final NamespacedKey DATA_KEY_AMOUNT;

    public ZDrawerManager(DrawerPlugin plugin) {
        this.plugin = plugin;
        this.DATA_KEY_ITEMSTACK = new NamespacedKey(plugin, "zdrawerItemstack");
        this.DATA_KEY_AMOUNT = new NamespacedKey(plugin, "zdrawerAmount");
    }

    @Override
    protected void onBlockPlace(BlockPlaceEvent event, Player player) {

        if (event.isCancelled()) return;

        Block block = event.getBlock();
        if (block.getType() != Material.BARREL) return;

        Barrel barrel = (Barrel) block.getBlockData();
        BlockFace blockFace = barrel.getFacing().getOppositeFace();
        Location location = block.getLocation().clone();

        Drawer drawer = new ZDrawer(location, blockFace);

        ItemStack itemStack = event.getItemInHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        System.out.println(persistentDataContainer.has(DATA_KEY_AMOUNT) + " - " + persistentDataContainer.has(DATA_KEY_ITEMSTACK));
        if (persistentDataContainer.has(DATA_KEY_AMOUNT) && persistentDataContainer.has(DATA_KEY_ITEMSTACK)) {
            String itemStackAsString = persistentDataContainer.getOrDefault(DATA_KEY_ITEMSTACK, PersistentDataType.STRING, "null");
            long amount = persistentDataContainer.getOrDefault(DATA_KEY_AMOUNT, PersistentDataType.LONG, 0L);
            ItemStack drawerItemStack = ItemStackUtils.deserializeItemStack(itemStackAsString);
            if (drawerItemStack != null) {
                drawer.setItemStack(drawerItemStack);
                drawer.setAmount(amount);
            }
        }

        this.getStorage().storeDrawer(drawer);
    }

    @Override
    protected void onInteract(PlayerInteractEvent event, Player player) {

        Block block = event.getClickedBlock();
        if (block == null) return;

        Optional<Drawer> optional = this.getStorage().getDrawer(block.getLocation());

        if (!optional.isPresent()) return;

        ItemStack itemStack = event.getItem();

        if (itemStack != null && Config.breakMaterials.contains(itemStack.getType()) && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Drawer drawer = optional.get();
        event.setCancelled(true);

        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK) {

            if (itemStack == null) return;

            drawer.addItem(player, itemStack, event.getHand());
        } else if (action == Action.LEFT_CLICK_BLOCK) {

            drawer.removeItem(player);
        }
    }

    @Override
    protected void onBlockBreak(BlockBreakEvent event, Player player) {
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        Optional<Drawer> optional = this.getStorage().getDrawer(block.getLocation());

        if (!optional.isPresent()) return;

        Drawer drawer = optional.get();
        PlayerInventory inventory = event.getPlayer().getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();
        ItemStack itemInOffHand = inventory.getItemInOffHand();

        System.out.println(Config.breakMaterials.contains(itemInMainHand.getType()) + " - " + Config.breakMaterials.contains(itemInOffHand.getType()));
        if (Config.breakMaterials.contains(itemInMainHand.getType()) || Config.breakMaterials.contains(itemInOffHand.getType())) {

            event.setCancelled(false);
            event.setDropItems(false);
            event.setExpToDrop(0);

            drawer.onDisable();
            getStorage().removeDrawer(block.getLocation());

            ItemStack itemStack = new ItemStack(Material.BARREL);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(Component.text("Drawer", TextColor.fromHexString("#20d68d")));
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

            persistentDataContainer.set(DATA_KEY_ITEMSTACK, PersistentDataType.STRING, drawer.hasItemStack() ? drawer.getItemStackAsString() : "null");
            persistentDataContainer.set(DATA_KEY_AMOUNT, PersistentDataType.LONG, drawer.getAmount());

            itemStack.setItemMeta(itemMeta);

            block.getWorld().dropItem(block.getLocation().add(0.5, 0.1, 0.5), itemStack);
        } else {

            event.setCancelled(true);
        }
    }

    private IStorage getStorage() {
        return this.plugin.getStorage().getStorage();
    }
}
