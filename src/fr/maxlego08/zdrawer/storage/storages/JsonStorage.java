package fr.maxlego08.zdrawer.storage.storages;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.ZDrawer;
import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.configuration.DrawerConfiguration;
import fr.maxlego08.zdrawer.api.storage.DrawerContainer;
import fr.maxlego08.zdrawer.api.storage.IStorage;
import fr.maxlego08.zdrawer.zcore.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JsonStorage implements IStorage {

    private transient final List<DrawerContainer> waitingDrawers = new ArrayList<>();
    private transient Map<String, List<Drawer>> drawerMapChunk = new HashMap<>();
    private transient DrawerPlugin plugin;
    private transient Map<String, Drawer> drawerMap = new HashMap<>();
    private List<DrawerContainer> drawers = new ArrayList<>();

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
            drawerContainer.loadData(drawer);
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
        this.drawerMap = new HashMap<>();
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
    public void purge(World world) {
        this.drawers.removeIf(drawerContainer -> stringToLocation(drawerContainer.getLocation()).getWorld().getName().equals(world.getName()));
        this.drawerMap.entrySet().removeIf(entry -> {
            boolean needToDelete = stringToLocation(entry.getKey()).getWorld().getName().equals(world.getName());
            if (needToDelete) entry.getValue().onDisable();
            return needToDelete;
        });
    }

    @Override
    public List<DrawerContainer> getWaitingWorldDrawers() {
        return this.waitingDrawers;
    }

    private String locationToString(Location location) {
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

    public Map<String, Drawer> getDrawerMap() {
        return drawerMap;
    }

    public Map<String, List<Drawer>> getDrawerMapChunk() {
        return drawerMapChunk;
    }

    public List<Drawer> getDrawers(Chunk chunk) {
        return drawerMapChunk.getOrDefault(chunk.getX() + "," + chunk.getZ(), new ArrayList<>());
    }
}
