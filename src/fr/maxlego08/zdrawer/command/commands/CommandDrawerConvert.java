package fr.maxlego08.zdrawer.command.commands;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.api.enums.StorageType;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.storage.storages.JsonStorage;
import fr.maxlego08.zdrawer.storage.storages.SqliteStorage;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;
import fr.maxlego08.zdrawer.zcore.utils.nms.Base64ItemStack;
import fr.maxlego08.zdrawer.zcore.utils.nms.ItemStackUtils;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CommandDrawerConvert extends VCommand {

    public CommandDrawerConvert(DrawerPlugin plugin) {
        super(plugin);
        this.setConsoleCanUse(false);
        this.setPermission(Permission.ZDRAWER_CLEAR);
        this.addSubCommand("convert");
        this.addRequireArg("type", (a, b) -> Arrays.asList("JSONTOSQLITE", "BASE64TO121"));
        this.setDescription(Message.DESCRIPTION_CLEAR);
    }

    @Override
    protected CommandType perform(DrawerPlugin plugin) {

        String type = this.argAsString(0);
        if (!type.equalsIgnoreCase("JSONTOSQLITE") && !type.equalsIgnoreCase("BASE64TO121")) {
            return CommandType.SYNTAX_ERROR;
        }

        if(type.equalsIgnoreCase("JSONTOSQLITE")){
            if (plugin.getStorage().getStorageType() == StorageType.JSON) {
                message(plugin, sender, "§cYou must put the storage in SQLITE before making this command.");
                return CommandType.DEFAULT;
            }

            JsonStorage jsonStorage = new JsonStorage(plugin);
            jsonStorage = plugin.getPersist().loadOrSaveDefault((JsonStorage) jsonStorage, JsonStorage.class, "drawers");

            jsonStorage.getDrawersContainer().forEach(drawerContainer -> {
                SqliteStorage sqliteStorage = (SqliteStorage) plugin.getStorage().getStorage();
                sqliteStorage.updateDrawer(drawerContainer);
                sqliteStorage.createDrawer(drawerContainer);
            });

            message(plugin, sender, "§aYou just converted data from JSON storage to SQLITE storage.");
        }

        if(type.equalsIgnoreCase("BASE64TO121")){
            if(plugin.getStorage().getStorageType() != StorageType.SQLITE){
                message(plugin, sender, "§cOnly SQLITE storage is supported for conversion.");
                return CommandType.DEFAULT;
            }

            SqliteStorage sqliteStorage = (SqliteStorage) plugin.getStorage().getStorage();
            sqliteStorage.getAllDrawers().forEach(drawerContainer -> {
                String data = drawerContainer.getData();

                String[] values = data.split(",");
                int dataAmount = Integer.parseInt(values[0]);

                for (int index = 1; index < dataAmount + 1; index++) {
                    String[] currentValues = values[index].split(";");
                    ItemStack itemStack = ItemStackUtils.deserializeItemStack(currentValues[0]);

                    if(itemStack == null){
                        continue;
                    }

                    int amount = Integer.parseInt(currentValues[1]);
                    currentValues[0] = Base64ItemStack.encode(itemStack);
                    currentValues[1] = String.valueOf(amount);

                    values[index] = String.join(";", currentValues);
                }

                String newData = String.join(",", values);
                drawerContainer.setData(newData);
                sqliteStorage.updateDrawer(drawerContainer);
            });

            message(plugin, sender, "§aYou just converted data from 1.20 to 1.21.");
        }

        return CommandType.SUCCESS;
    }

}
