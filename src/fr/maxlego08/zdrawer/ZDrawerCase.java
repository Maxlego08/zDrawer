package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerCase;
import fr.maxlego08.zdrawer.zcore.utils.ZUtils;
import fr.maxlego08.zdrawer.zcore.utils.meta.Meta;
import fr.maxlego08.zdrawer.zcore.utils.nms.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Optional;

public class ZDrawerCase extends ZUtils implements DrawerCase {

    private final DrawerPlugin plugin;
    private final Drawer drawer;
    private ItemDisplay itemDisplay;
    private TextDisplay textDisplay;
    private ItemStack itemStack;
    private long amount;

    public ZDrawerCase(DrawerPlugin plugin, Drawer drawer) {
        this.plugin = plugin;
        this.drawer = drawer;
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
    public ItemDisplay getItemDisplay() {
        return this.itemDisplay;
    }

    @Override
    public void setItemDisplay(ItemDisplay itemDisplay) {
        this.itemDisplay = itemDisplay;
    }

    @Override
    public TextDisplay getTextDisplay() {
        return this.textDisplay;
    }

    @Override
    public void setTextDisplay(TextDisplay textDisplay) {
        this.textDisplay = textDisplay;
    }

    @Override
    public void addItem(Player player, ItemStack itemStack, EquipmentSlot hand) {
        if (isFull() || itemStack == null) return;

        if (this.itemStack == null) {
            createNewItemStack(itemStack);
        } else if (itemStack.isSimilar(this.itemStack)) {
            addItemToExistingStack(player, itemStack);
        }
        updateInventory(player, itemStack, hand);
        updateText();
    }

    private void createNewItemStack(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
        this.itemDisplay.setItemStack(this.itemStack);
        this.amount = Math.min(itemStack.getAmount(), calculateAvailableSpace());
    }

    private void addItemToExistingStack(Player player, ItemStack itemStack) {
        if (player.isSneaking()) {
            addAllSimilarItems(player.getInventory());
        } else {
            this.amount += Math.min(itemStack.getAmount(), calculateAvailableSpace());
        }
    }

    private long calculateAvailableSpace() {
        return this.drawer.hasLimit() ? this.drawer.getLimit() - this.amount : Long.MAX_VALUE;
    }

    private void updateInventory(Player player, ItemStack itemStack, EquipmentSlot hand) {
        int remaining = itemStack.getAmount() - (int) Math.min(itemStack.getAmount(), calculateAvailableSpace());
        if (remaining > 0) {
            itemStack.setAmount(remaining);
        } else {
            player.getInventory().setItem(hand, new ItemStack(Material.AIR));
        }
    }

    private void addAllSimilarItems(PlayerInventory inventory) {
        for (int slot = 0; slot != 36; slot++) {

            ItemStack currentItemStack = inventory.getItem(slot);
            if (currentItemStack != null && this.itemStack.isSimilar(currentItemStack)) {
                int addAmount = (int) Math.min(currentItemStack.getAmount(), this.drawer.getLimit() - this.amount);
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
    }

    @Override
    public void removeItem(Player player) {

        if (this.itemStack == null) return;

        if (player.isSneaking()) {

            int itemStackAmount = (int) Math.min(this.amount, itemStack.getMaxStackSize());
            ItemStack itemStack = this.itemStack.clone();
            itemStack.setAmount(itemStackAmount);
            remove(itemStack.getMaxStackSize());
            give(player, itemStack, true);

        } else {

            ItemStack itemStack = this.itemStack.clone();
            itemStack.setAmount(1);
            remove(1);

            give(player, itemStack, true);
        }
    }

    @Override
    public void onDisable() {
        Optional.ofNullable(this.textDisplay).ifPresent(TextDisplay::remove);
        Optional.ofNullable(this.itemDisplay).ifPresent(ItemDisplay::remove);
    }

    @Override
    public void onLoad() {
        this.itemDisplay.setItemStack(itemStack);
        updateText();
    }

    @Override
    public boolean hasItemStack() {
        return this.itemStack != null;
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
    public boolean isFull() {
        return this.drawer.hasLimit() && this.amount >= this.drawer.getLimit();
    }

    @Override
    public void updateText() {
        Meta.meta.updateText(this.textDisplay, this.plugin.getManager().numberFormat(amount, false));
    }
}
