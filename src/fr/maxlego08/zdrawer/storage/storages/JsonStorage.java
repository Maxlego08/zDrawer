package fr.maxlego08.zdrawer.storage.storages;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.ZDrawer;
import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.configuration.DrawerConfiguration;
import fr.maxlego08.zdrawer.api.storage.DrawerContainer;
import fr.maxlego08.zdrawer.api.storage.IStorage;
import fr.maxlego08.zdrawer.zcore.logger.Logger;
import fr.maxlego08.zdrawer.zcore.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JsonStorage extends ZUtils implements IStorage {

    protected transient final List<DrawerContainer> waitingDrawers = new ArrayList<>();
    protected transient Map<String, List<Drawer>> drawerMapChunk = new HashMap<>();
    protected transient DrawerPlugin plugin;
    protected transient Map<String, Drawer> drawerMap = new HashMap<>();
    protected List<DrawerContainer> drawers = new ArrayList<>();

    public JsonStorage(DrawerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Optional<Drawer> getDrawer(Location location) {
        return Optional.ofNullable(drawerMap.getOrDefault(locationToString(location), null));
    }

    @Override
    public void storeDrawer(Drawer drawer) {

        String stringLocation = locationToString(drawer.getLocation());
        this.drawerMap.put(stringLocation, drawer);

        DrawerContainer drawerContainer = new DrawerContainer(stringLocation, drawer.getBlockFace(), drawer.getConfigurationName(), drawer.getData(), drawer.getUpgradeName());
        this.drawers.add(drawerContainer);
    }

    @Override
    public void removeDrawer(Location location) {

        String stringLocation = locationToString(location);
        this.drawerMap.remove(stringLocation);

        this.drawers.removeIf(dc -> dc.getLocation().equals(stringLocation));
    }

    @Override
    public void createDrawer(DrawerContainer drawerContainer) {
        Location location = stringToLocation(drawerContainer.getLocation());

        Optional<DrawerConfiguration> optional = plugin.getManager().getDrawer(drawerContainer.getDrawerName());
        if (!optional.isPresent()) {
            Logger.info("Impossible to load a drawer, configuration " + drawerContainer.getDrawerName() + " doesn't exit !", Logger.LogType.ERROR);
            return;
        }
        Drawer drawer = new ZDrawer(this.plugin, optional.get(), location, drawerContainer.getBlockFace());

        if (drawerContainer.hasData()) {
            drawer.load(drawerContainer.getData());
        }

        if (drawerContainer.hasUpgrade()) {
            this.plugin.getManager().getUpgrade(drawerContainer.getUpgrade()).ifPresent(drawer::setUpgrade);
        }
        drawerMap.put(drawerContainer.getLocation(), drawer);

        if (this.drawerMapChunk == null) this.drawerMapChunk = new HashMap<>();
        List<Drawer> drawerList = this.drawerMapChunk.computeIfAbsent(location.getChunk().getX() + "," + location.getChunk().getZ(), a -> new ArrayList<>());
        drawerList.add(drawer);
    }

    @Override
    public void load() {

        Bukkit.getWorlds().forEach(world -> clearWorld(this.plugin, world));

        this.drawerMap = new HashMap<>();
        Logger.info("Loading " + this.drawers.size() + " drawers.", Logger.LogType.INFO);
        this.drawers.forEach(drawerContainer -> {

            if (!drawerContainer.isWorldLoaded()) {
                this.waitingDrawers.add(drawerContainer);
                return;
            }

            createDrawer(drawerContainer);
        });
    }

    @Override
    public void save() {
        this.drawers = new ArrayList<>();
        this.drawerMap.forEach((stringLocation, drawer) -> {
            drawer.onDisable();
            DrawerContainer drawerContainer = new DrawerContainer(stringLocation, drawer.getBlockFace(), drawer.getConfigurationName(), drawer.getData(), drawer.getUpgradeName());
            this.drawers.add(drawerContainer);
        });
    }

    @Override
    public void purge(World world, boolean destroyBlock) {
        this.drawers.removeIf(drawerContainer -> stringToLocation(drawerContainer.getLocation()).getWorld().getName().equals(world.getName()));
        this.drawerMap.entrySet().removeIf(entry -> {
            boolean needToDelete = stringToLocation(entry.getKey()).getWorld().getName().equals(world.getName());
            if (needToDelete) {
                Drawer drawer = entry.getValue();
                drawer.onDisable();
                if (destroyBlock) drawer.getLocation().getBlock().setType(Material.AIR);
            }
            return needToDelete;
        });
    }

    @Override
    public List<DrawerContainer> getWaitingWorldDrawers() {
        return this.waitingDrawers;
    }

    protected String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }

    private Location stringToLocation(String string) {
        String[] locationArray = string.split(",");
        return new Location(Bukkit.getServer().getWorld(locationArray[0]), Integer.parseInt(locationArray[1]), Integer.parseInt(locationArray[2]), Integer.parseInt(locationArray[3]));
    }

    public void setPlugin(DrawerPlugin plugin) {
        this.plugin = plugin;
        this.drawerMapChunk = new HashMap<>();
    }

    public List<Drawer> getDrawers(Chunk chunk) {
        return drawerMapChunk.getOrDefault(chunk.getX() + "," + chunk.getZ(), new ArrayList<>());
    }

    @Override
    public void update() {

    }

    public List<DrawerContainer> getDrawers() {
        return drawers;
    }
}
