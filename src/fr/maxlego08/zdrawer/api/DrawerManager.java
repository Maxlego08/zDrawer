package fr.maxlego08.zdrawer.api;

import fr.maxlego08.zdrawer.api.craft.Craft;
import fr.maxlego08.zdrawer.api.storage.Savable;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public interface DrawerManager extends Savable {

    long getDrawerLimit();

    Optional<Craft> getCraft(String craftName);

    Optional<DrawerUpgrade> getUpgrade(String upgradeName);

    void giveDrawer(CommandSender sender, Player player, DrawerUpgrade drawerUpgrade, Material material, Long amount);

    ItemStack buildDrawer(Player player, Drawer drawer);

    List<String> getUpgradeNames();

    void giveCraft(CommandSender sender, Player player, String craftName);

    List<String> getCraftNames();
}
