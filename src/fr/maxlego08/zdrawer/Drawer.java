package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.zcore.utils.ZUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public class Drawer extends ZUtils {

    private final Location location;
    private final BlockFace blockFace;
    private Material material; // Use Material for the DEMO, but the real will use ItemStack
    private int amount;
    private ItemDisplay itemDisplay;
    private TextDisplay textDisplay;

    public Drawer(Location location, BlockFace blockFace) {
        this.location = location;
        this.blockFace = blockFace;
        this.spawnDisplay();
    }

    private void spawnDisplay() {

        Location location = this.location.clone();
        Location locationTextDisplay = this.location.clone();
        location.add(0.5, 0.5, -0.01);
        locationTextDisplay.add(0.5, 0.05, -0.02);

        location.setPitch(0f);
        locationTextDisplay.setPitch(0f);

        updateYaw(location, blockFace);
        updateYaw(locationTextDisplay, blockFace.getOppositeFace());

        this.itemDisplay = location.getWorld().spawn(location, ItemDisplay.class, display -> {

            // display.setItemStack(new ItemStack(Material.STONE));
            display.setBillboard(Display.Billboard.FIXED);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
            Transformation transformation = display.getTransformation();
            Vector3f scale = transformation.getScale();
            scale.set(0.6f, 0.6f, 0.0f);
            display.setTransformation(transformation);
            display.setInvulnerable(true);
        });

        this.textDisplay = location.getWorld().spawn(locationTextDisplay, TextDisplay.class, display -> {

            display.setInvulnerable(true);
            display.setBillboard(Display.Billboard.FIXED);
            display.setAlignment(TextDisplay.TextAlignment.CENTER);
            // display.text(Component.text("0", NamedTextColor.WHITE));
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

    public void addItem(Player player, ItemStack itemStack, EquipmentSlot hand) {

        // If the item does not exist, then we will create it
        if (this.material == null) {

            this.material = itemStack.getType();
            this.amount = itemStack.getAmount();
            this.itemDisplay.setItemStack(itemStack);

            textDisplay.text(Component.text(String.valueOf(this.amount), NamedTextColor.WHITE));
            player.getInventory().setItem(hand, new ItemStack(Material.AIR));

        } else if (material == itemStack.getType()) {

            // We will retrieve all the items that are similar to add them
            if (player.isSneaking()) {

                PlayerInventory inventory = player.getInventory();
                for (int slot = 0; slot != 36; slot++) {

                    ItemStack currentItemStack = inventory.getItem(slot);
                    if (currentItemStack != null && itemStack.getType() == currentItemStack.getType()) {
                        this.amount += currentItemStack.getAmount();
                        inventory.setItem(slot, new ItemStack(Material.AIR));
                    }

                    textDisplay.text(Component.text(String.valueOf(this.amount), NamedTextColor.WHITE));
                }

            } else {

                this.amount += itemStack.getAmount();
                textDisplay.text(Component.text(String.valueOf(this.amount), NamedTextColor.WHITE));
                player.getInventory().setItem(hand, new ItemStack(Material.AIR));
            }
        }
    }

    public void removeItem(Player player) {

        if (this.material == null) return;

        if (player.isSneaking()) {

            int itemStackAmount = Math.min(this.amount, 64);
            ItemStack itemStack = new ItemStack(this.material, itemStackAmount);
            this.amount -= itemStackAmount;

            give(player, itemStack);

        } else {

            ItemStack itemStack = new ItemStack(this.material, 1);
            this.amount -= 1;

            give(player, itemStack);
        }

        // If there is no more item, then it is deleted
        if (this.amount <= 0) {

            this.material = null;
            this.amount = 0;
            itemDisplay.setItemStack(new ItemStack(Material.AIR));
            textDisplay.text(Component.text("", NamedTextColor.WHITE));

        } else {
            textDisplay.text(Component.text(String.valueOf(this.amount), NamedTextColor.WHITE));
        }
    }
}
