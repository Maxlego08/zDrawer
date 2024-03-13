package fr.maxlego08.zdrawer.command.commands;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class CommandDrawerDebug extends VCommand {

    public CommandDrawerDebug(DrawerPlugin plugin) {
        super(plugin);
        this.addSubCommand("debug");
        this.setPermission(Permission.ZDRAWER_DEBUG);
        this.setDescription(Message.DESCRIPTION_VERSION);
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(DrawerPlugin plugin) {

        Chunk chunk = this.player.getChunk();
        plugin.getStorage().getStorage().getDrawers(chunk).forEach(Drawer::onDisable);
        message(plugin, sender, Message.DRAWER_DEBUG_DISABLE);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            plugin.getStorage().getStorage().getDrawers(chunk).forEach(Drawer::onLoad);
            message(plugin, sender, Message.DRAWER_DEBUG_ENABLE);
        }, 20);


        return CommandType.SUCCESS;
    }

}
