package fr.maxlego08.zdrawer.storage.storages;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.storage.DrawerContainer;
import fr.maxlego08.zdrawer.zcore.ZPlugin;
import fr.maxlego08.zdrawer.zcore.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteStorage extends JsonStorage {

    private final String tableName;
    protected Connection connection;
    private File databaseFile = null;

    public SqliteStorage(DrawerPlugin plugin, boolean createFile) {
        super(plugin);

        FileConfiguration configuration = plugin.getConfig();
        this.tableName = configuration.getString("sql.tableName", "zdrawers");

        if (createFile) {
            this.databaseFile = new File(plugin.getDataFolder(), "database.db");
            if (!databaseFile.exists()) {
                try {
                    databaseFile.createNewFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    @Override
    public void load() {
        ZPlugin.service.execute(() -> {

            this.initializeDatabase();
            this.drawers = this.getAllDrawers();

            Bukkit.getScheduler().runTask(this.plugin, super::load);
        });
    }

    @Override
    public void save() {
        super.save();

        for (Drawer drawer : this.drawerMap.values()) {
            if (drawer.needToUpdate()) {
                drawer.setNeedToUpdate(false);
                this.updateDrawer(drawer, true);
            }
        }

        this.disconnect();
    }

    @Override
    public void storeDrawer(Drawer drawer) {
        super.storeDrawer(drawer);
        this.updateDrawer(drawer, false);
    }

    @Override
    public void removeDrawer(Location location) {
        super.removeDrawer(location);

        ZPlugin.service.execute(() -> {
            String sqlRequest = "DELETE FROM " + this.tableName + " WHERE location = ?";
            try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sqlRequest)) {
                preparedStatement.setString(1, locationToString(location));
                preparedStatement.executeUpdate();
            } catch (SQLException exception) {
                Logger.info("Error removing drawer from SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
            }
        });
    }

    @Override
    public void purge(World world, boolean destroyBlock) {

        super.purge(world, destroyBlock);
        ZPlugin.service.execute(() -> {
            String sql = "DELETE FROM " + this.tableName + " WHERE location LIKE ?";
            try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
                preparedStatement.setString(1, world.getName() + ",%");
                preparedStatement.executeUpdate();
            } catch (SQLException exception) {
                Logger.info("Error purging drawers from SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
            }
        });
    }

    private boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed() && connection.isValid(1);
    }

    public void disconnect() {
        try {
            if (isConnected()) connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void connection() {
        try {
            if (!isConnected()) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Connection getConnection() {
        this.connection();
        return connection;
    }

    private void initializeDatabase() {
        String sqlRequest = "CREATE TABLE IF NOT EXISTS " + this.tableName + " (location TEXT PRIMARY KEY, blockFace TEXT, configurationName TEXT, content TEXT, upgradeName TEXT)";
        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sqlRequest)) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.info("Could not initialize SQLite database: " + exception.getMessage(), Logger.LogType.ERROR);
        }
    }

    private void updateDrawer(Drawer drawer, boolean sync) {
        Runnable runnable = () -> {
            String sql = "INSERT INTO " + this.tableName + " (location, blockFace, configurationName, content, upgradeName) " + "VALUES (?, ?, ?, ?, ?) " + "ON CONFLICT(location) DO UPDATE SET " + "blockFace = EXCLUDED.blockFace, " + "configurationName = EXCLUDED.configurationName, " + "content = EXCLUDED.content, " + "upgradeName = EXCLUDED.upgradeName";

            try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
                preparedStatement.setString(1, locationToString(drawer.getLocation()));
                preparedStatement.setString(2, drawer.getBlockFace().name());
                preparedStatement.setString(3, drawer.getConfigurationName());
                preparedStatement.setString(4, drawer.getData());
                preparedStatement.setString(5, drawer.getUpgradeName());
                preparedStatement.executeUpdate();
            } catch (SQLException exception) {
                Logger.info("Error storing or updating drawer in SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
            }
        };
        if (sync) runnable.run();
        else ZPlugin.service.execute(runnable);
    }

    public void updateDrawer(DrawerContainer drawerContainer) {
        ZPlugin.service.execute(() -> {
            String sql = "INSERT INTO " + this.tableName + " (location, blockFace, configurationName, content, upgradeName) " + "VALUES (?, ?, ?, ?, ?) " + "ON CONFLICT(location) DO UPDATE SET " + "blockFace = EXCLUDED.blockFace, " + "configurationName = EXCLUDED.configurationName, " + "content = EXCLUDED.content, " + "upgradeName = EXCLUDED.upgradeName";

            try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
                preparedStatement.setString(1, drawerContainer.getLocation());
                preparedStatement.setString(2, drawerContainer.getBlockFace().name());
                preparedStatement.setString(3, drawerContainer.getDrawerName());
                preparedStatement.setString(4, drawerContainer.getData());
                preparedStatement.setString(5, drawerContainer.getUpgrade());
                preparedStatement.executeUpdate();
            } catch (SQLException exception) {
                Logger.info("Error storing or updating drawer in SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
            }
        });
    }

    public List<DrawerContainer> getAllDrawers() {
        List<DrawerContainer> drawers = new ArrayList<>();
        String sql = "SELECT location, blockFace, configurationName, content, upgradeName FROM " + this.tableName;

        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String location = resultSet.getString("location");
                BlockFace blockFace = BlockFace.valueOf(resultSet.getString("blockFace"));
                String drawerName = resultSet.getString("configurationName");
                String data = resultSet.getString("content");
                String upgrade = resultSet.getString("upgradeName");

                DrawerContainer drawerContainer = new DrawerContainer(location, blockFace, drawerName, data, upgrade);
                drawers.add(drawerContainer);
            }
        } catch (SQLException exception) {
            Logger.info("Error retrieving drawers from SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
        }

        return drawers;
    }

    @Override
    public void update() {
        for (Drawer drawer : this.drawerMap.values()) {
            if (drawer.needToUpdate()) {
                drawer.setNeedToUpdate(false);
                this.updateDrawer(drawer, false);
            }
        }
    }
}
