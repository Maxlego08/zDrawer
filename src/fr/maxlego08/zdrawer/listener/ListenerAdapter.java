package fr.maxlego08.zdrawer.listener;

import fr.maxlego08.zdrawer.zcore.utils.ZUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.List;

@SuppressWarnings("deprecation")
public abstract class ListenerAdapter extends ZUtils {

    protected void onConnect(PlayerJoinEvent event, Player player) {
    }

    protected void onQuit(PlayerQuitEvent event, Player player) {
    }

    protected void onMove(PlayerMoveEvent event, Player player) {
    }

    protected void onInventoryClick(InventoryClickEvent event, Player player) {
    }

    protected void onInventoryClose(InventoryCloseEvent event, Player player) {
    }

    protected void onInventoryDrag(InventoryDragEvent event, Player player) {
    }

    protected void onBlockBreak(BlockBreakEvent event, Player player) {
    }

    protected void onBlockPlace(BlockPlaceEvent event, Player player) {
    }

    protected void onEntityDeath(EntityDeathEvent event, Entity entity) {
    }

    protected void onInteract(PlayerInteractEvent event, Player player) {
    }

    protected void onPlayerTalk(AsyncPlayerChatEvent event, String message) {
    }

    protected void onCraftItem(CraftItemEvent event) {
    }

    protected void onCommand(PlayerCommandPreprocessEvent event, Player player, String message) {
    }

    protected void onGamemodeChange(PlayerGameModeChangeEvent event, Player player) {
    }

    protected void onDrop(PlayerDropItemEvent event, Player player) {
    }

    protected void onPickUp(PlayerPickupItemEvent event, Player player) {
    }

    protected void onMobSpawn(CreatureSpawnEvent event) {
    }

    protected void onDamageByEntity(EntityDamageByEntityEvent event, DamageCause cause, double damage, LivingEntity damager, LivingEntity entity) {
    }

    protected void onPlayerDamagaByPlayer(EntityDamageByEntityEvent event, DamageCause cause, double damage, Player damager, Player entity) {
    }

    protected void onPlayerDamagaByArrow(EntityDamageByEntityEvent event, DamageCause cause, double damage, Projectile damager, Player entity) {
    }

    protected void onItemisOnGround(PlayerDropItemEvent event, Player player, Item item, Location location) {
    }

    protected void onItemMove(PlayerDropItemEvent event, Player player, Item item, Location location, Block block) {
    }

    protected void onPlayerWalk(PlayerMoveEvent event, Player player, int i) {
    }

    protected void onPrepareItemCraft(PrepareItemCraftEvent event, Recipe recipe) {

    }

    protected void onInventoryOpen(InventoryOpenEvent event, Player player, Inventory inventory) {
    }

    protected void onExplode(BlockExplodeEvent event, List<Block> blocks, Block block) {
    }

    protected void onExplode(EntityExplodeEvent event, List<Block> blocks, Entity entity) {
    }

    protected void onInventoryMove(InventoryMoveItemEvent event, Inventory destination, ItemStack item, Inventory source, Inventory initiator) {

    }

    protected void onLoad(ChunkLoadEvent event, Chunk chunk) {

    }

    protected void onUnLoad(ChunkUnloadEvent event, Chunk chunk) {

    }

    protected void onWorldLoad(WorldLoadEvent event, World world) {
    }
}
