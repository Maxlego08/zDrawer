package fr.maxlego08.zdrawer.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerAccess;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardAccess implements DrawerAccess {
    @Override
    public boolean hasAccess(Player player, Location location, Drawer drawer) {

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(location.getWorld()));
        if (regionManager == null) return true;

        ApplicableRegionSet regionSet = regionManager.getApplicableRegions(BukkitAdapter.adapt(location).toVector().toBlockPoint());
        return !regionSet.testState(WorldGuardPlugin.inst().wrapPlayer(player), Flags.BLOCK_BREAK);
    }
}
