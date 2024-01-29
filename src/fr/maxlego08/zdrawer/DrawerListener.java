package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.api.storage.IStorage;
import fr.maxlego08.zdrawer.listener.ListenerAdapter;
import fr.maxlego08.zdrawer.save.Config;
import fr.maxlego08.zdrawer.zcore.utils.nms.ItemStackUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Barrel;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Optional;

public class DrawerListener extends ListenerAdapter {

    private final DrawerPlugin plugin;
    private final DrawerManager manager;

    public DrawerListener(DrawerPlugin plugin) {
        this.plugin = plugin;
        this.manager = plugin.getManager();
    }

    @Override
    protected void onInteract(PlayerInteractEvent event, Player player) {

        if (event.isCancelled()) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        Optional<Drawer> optional = this.manager.getStorage().getDrawer(block.getLocation());

        if (!optional.isPresent()) return;

        Drawer drawer = optional.get();

        // The player will type another face than the one in front, so he wants to perform another action, like placing a block. We stop the code here.
        if (event.getBlockFace().getOppositeFace() != drawer.getBlockFace()) return;

        event.setCancelled(true);

        ItemStack itemStack = event.getItem();

        if (itemStack != null && Config.breakMaterials.contains(itemStack.getType()) && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (itemStack != null && Config.blacklistMaterials.contains(itemStack.getType())) return;

        // Check for upgrade
        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            if (persistentDataContainer.has(this.manager.getNamespaceContainer().getDataKeyUpgrade())) {
                String drawerUpgradeName = persistentDataContainer.get(this.manager.getNamespaceContainer().getDataKeyUpgrade(), PersistentDataType.STRING);
                this.manager.getUpgrade(drawerUpgradeName).ifPresent(drawerUpgrade -> {

                    if (drawer.getLimit() >= drawerUpgrade.getLimit()) {
                        message(this.plugin, player, Message.UPGRADE_ERROR_LIMIT);
                        return;
                    }

                    drawer.setUpgrade(drawerUpgrade);

                    if (itemStack.getAmount() > 1) itemStack.setAmount(itemStack.getAmount() - 1);
                    else player.getInventory().setItem(event.getHand(), new ItemStack(Material.AIR));

                    message(this.plugin, player, Message.UPGRADE_SUCCESS, "%name%", drawerUpgrade.getDisplayName());
                });
                return;
            }

        }

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
        Optional<Drawer> optional = this.manager.getStorage().getDrawer(block.getLocation());

        if (!optional.isPresent()) return;

        Drawer drawer = optional.get();
        PlayerInventory inventory = event.getPlayer().getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();
        ItemStack itemInOffHand = inventory.getItemInOffHand();

        if (Config.breakMaterials.contains(itemInMainHand.getType()) || Config.breakMaterials.contains(itemInOffHand.getType())) {

            event.setCancelled(false);
            event.setDropItems(false);
            event.setExpToDrop(0);

            drawer.onDisable();
            this.manager.getStorage().removeDrawer(block.getLocation());

            this.manager.getCurrentPlayerDrawer().put(player.getUniqueId(), drawer);

            ItemStack itemStack = this.manager.buildDrawer(player, drawer);
            block.getWorld().dropItem(block.getLocation().add(0.5, 0.1, 0.5), itemStack);
        } else {

            event.setCancelled(true);
        }
    }

    @Override
    protected void onBlockPlace(BlockPlaceEvent event, Player player) {

        if (event.isCancelled()) return;

        Block block = event.getBlock();
        if (block.getType() != Material.BARREL) return;

        ItemStack itemStack = event.getItemInHand();
        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().getPersistentDataContainer().has(this.manager.getNamespaceContainer().getDataKeyDrawer()))
            return;

        Barrel barrel = (Barrel) block.getBlockData();
        BlockFace blockFace = barrel.getFacing().getOppositeFace();
        Location location = block.getLocation().clone();

        Drawer drawer = new ZDrawer(plugin, location, blockFace);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        if (persistentDataContainer.has(this.manager.getNamespaceContainer().getDataKeyAmount()) && persistentDataContainer.has(this.manager.getNamespaceContainer().getDataKeyItemstack())) {
            String itemStackAsString = persistentDataContainer.getOrDefault(this.manager.getNamespaceContainer().getDataKeyItemstack(), PersistentDataType.STRING, "null");
            long amount = persistentDataContainer.getOrDefault(this.manager.getNamespaceContainer().getDataKeyAmount(), PersistentDataType.LONG, 0L);
            ItemStack drawerItemStack = ItemStackUtils.deserializeItemStack(itemStackAsString);
            if (drawerItemStack != null) {
                drawer.setItemStack(drawerItemStack);
                drawer.setAmount(amount);
            }

            if (persistentDataContainer.has(this.manager.getNamespaceContainer().getDataKeyUpgrade())) {
                manager.getUpgrade(persistentDataContainer.get(this.manager.getNamespaceContainer().getDataKeyUpgrade(), PersistentDataType.STRING)).ifPresent(drawer::setUpgrade);
            }
        }

        this.manager.getStorage().storeDrawer(drawer);
    }

    @Override
    protected void onQuit(PlayerQuitEvent event, Player player) {
        this.manager.getCurrentPlayerDrawer().remove(player.getUniqueId());
    }

    @Override
    public void onExplode(EntityExplodeEvent event, List<Block> blocks, Entity entity) {
        onExplode(blocks);
    }

    @Override
    public void onExplode(BlockExplodeEvent event, List<Block> blocks, Block block) {
        onExplode(blocks);
    }

    private void onExplode(List<Block> blocks) {
        blocks.removeIf(block -> this.manager.getStorage().getDrawer(block.getLocation()).isPresent());
    }

    @Override
    public void onInventoryMove(InventoryMoveItemEvent event, Inventory destination, ItemStack item, Inventory source, Inventory initiator) {

        if (event.isCancelled()) return;

        IStorage storage = this.manager.getStorage();
        // If the source is a barrel, then the item that will start green another inventory
        if (source.getHolder() instanceof org.bukkit.block.Barrel) {

            org.bukkit.block.Barrel barrel = (org.bukkit.block.Barrel) source.getHolder();
            Optional<Drawer> optional = storage.getDrawer(barrel.getLocation());
            if (!optional.isPresent()) return;
            Drawer drawer = optional.get();

            event.setCancelled(true);

            if (drawer.getAmount() <= 0) return;

            ItemStack newItemStack = drawer.getItemStack().clone();
            newItemStack.setAmount(1);
            destination.addItem(newItemStack);

            drawer.remove(1);

            return;
        }

        // Si la destination est un baril, alors l’article va dans le tiroir
        if (destination.getHolder() instanceof org.bukkit.block.Barrel) {

            org.bukkit.block.Barrel barrel = (org.bukkit.block.Barrel) destination.getHolder();
            Optional<Drawer> optional = storage.getDrawer(barrel.getLocation());
            if (!optional.isPresent()) return;
            Drawer drawer = optional.get();

            ItemStack newItemStack = item.clone();

            // Si le drawer n'a aucun item, alors on va ajouter l'item du hopper
            if (!drawer.hasItemStack()) {

                drawer.setAmount(newItemStack.getAmount());
                drawer.setItemStack(newItemStack);
                event.setItem(new ItemStack(Material.AIR));

            } else if (drawer.getItemStack().isSimilar(newItemStack)) {

                if (drawer.isFull()) {
                    event.setCancelled(true);
                    return;
                }

                int addAmount = (int) Math.min(newItemStack.getAmount(), drawer.getLimit() - drawer.getAmount());
                drawer.add(addAmount);
                event.setItem(new ItemStack(Material.AIR));

            } else {
                event.setCancelled(true);
            }
        }
    }

    @Override
    protected void onPrepareItemCraft(PrepareItemCraftEvent event, Recipe recipe) {
        event.getViewers().stream().findFirst().ifPresent(viewer -> {
            if (viewer instanceof Player) {

                if (recipe == null) return;

                ItemStack itemStack = recipe.getResult();
                if (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(this.manager.getNamespaceContainer().getDataKeyDrawer())) {
                    this.manager.getCurrentPlayerDrawer().remove(viewer.getUniqueId());
                    event.getInventory().setItem(0, this.manager.buildDrawer((Player) viewer, null));
                }
            }
        });
    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event, Player player, Inventory inventory) {
        if (inventory.getHolder() instanceof org.bukkit.block.Barrel) {
            org.bukkit.block.Barrel barrel = (org.bukkit.block.Barrel) inventory.getHolder();
            this.manager.getStorage().getDrawer(barrel.getLocation()).ifPresent(drawer -> {
                event.setCancelled(true);
            });
        }
    }
}
