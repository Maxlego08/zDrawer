package fr.maxlego08.zdrawer.command.commands;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.DrawerUpgrade;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.WorldInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandDrawerPurge extends VCommand {

    public CommandDrawerPurge(DrawerPlugin plugin) {
        super(plugin);

        this.addSubCommand("purge");
        this.setPermission(Permission.ZDRAWER_PURGE);
        this.setDescription(Message.DESCRIPTION_PURGE);
        this.addRequireArg("world name", (a, b) -> Bukkit.getWorlds().stream().map(WorldInfo::getName).collect(Collectors.toList()));
        this.addOptionalArg("destroy block", (a,b) -> Arrays.asList("true", "false"));
    }

    @Override
    protected CommandType perform(DrawerPlugin plugin) {

        DrawerManager manager = plugin.getManager();

        World world = this.argAsWorld(0);
        boolean destroyBlock = this.argAsBoolean(1, false);
        manager.purgeWorld(this.sender, world, destroyBlock);

        return CommandType.SUCCESS;
    }

}
