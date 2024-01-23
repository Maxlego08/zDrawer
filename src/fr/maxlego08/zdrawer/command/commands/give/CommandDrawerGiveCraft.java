package fr.maxlego08.zdrawer.command.commands.give;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;
import org.bukkit.entity.Player;

public class CommandDrawerGiveCraft extends VCommand {

    public CommandDrawerGiveCraft(DrawerPlugin plugin) {
        super(plugin);

        this.addSubCommand("craft");
        this.setPermission(Permission.ZDRAWER_GIVE_CRAFT);
        this.setDescription(Message.DESCRIPTION_GIVE_CRAFT);
        this.addRequireArg("player");
        this.addRequireArg("craftName", (a, b) -> plugin.getManager().getCraftNames());
    }

    @Override
    protected CommandType perform(DrawerPlugin plugin) {

        DrawerManager manager = plugin.getManager();

        Player player = this.argAsPlayer(0);
        String craftName = this.argAsString(1);
        if (craftName == null) return CommandType.SYNTAX_ERROR;

        manager.giveCraft(this.sender, player, craftName);

        return CommandType.SUCCESS;
    }

}
