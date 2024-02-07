package fr.maxlego08.zdrawer.command.commands;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.api.enums.StorageType;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.storage.storages.JsonStorage;
import fr.maxlego08.zdrawer.storage.storages.SqliteStorage;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;

import java.util.Arrays;

public class CommandDrawerConvert extends VCommand {

    public CommandDrawerConvert(DrawerPlugin plugin) {
        super(plugin);
        this.setConsoleCanUse(false);
        this.setPermission(Permission.ZDRAWER_CLEAR);
        this.addSubCommand("convert");
        this.addRequireArg("type", (a, b) -> Arrays.asList("JSONTOSQLITE"));
        this.setDescription(Message.DESCRIPTION_CLEAR);
    }

    @Override
    protected CommandType perform(DrawerPlugin plugin) {

        String type = this.argAsString(0);
        if (!type.equalsIgnoreCase("JSONTOSQLITE")) {
            return CommandType.SYNTAX_ERROR;
        }

        if (plugin.getStorage().getStorageType() == StorageType.JSON) {
            message(plugin, sender, "§cYou must put the storage in SQLITE before making this command.");
            return CommandType.DEFAULT;
        }

        JsonStorage jsonStorage = new JsonStorage(plugin);
        jsonStorage = plugin.getPersist().loadOrSaveDefault((JsonStorage) jsonStorage, JsonStorage.class, "drawers");

        jsonStorage.getDrawers().forEach(drawerContainer -> {
            SqliteStorage sqliteStorage = (SqliteStorage) plugin.getStorage().getStorage();
            sqliteStorage.updateDrawer(drawerContainer);
            sqliteStorage.createDrawer(drawerContainer);
        });

        message(plugin, sender, "§aYou just converted data from JSON storage to SQLITE storage.");

        return CommandType.SUCCESS;
    }

}
