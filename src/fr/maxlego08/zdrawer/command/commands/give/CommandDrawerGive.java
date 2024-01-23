package fr.maxlego08.zdrawer.command.commands.give;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;

public class CommandDrawerGive extends VCommand {

    public CommandDrawerGive(DrawerPlugin plugin) {
        super(plugin);
        this.addSubCommand("give");
        this.addSubCommand(new CommandDrawerGiveDrawer(plugin));
        this.setPermission(Permission.ZDRAWER_GIVE);
        this.setDescription(Message.DESCRIPTION_GIVE_USE);
    }

    @Override
    protected CommandType perform(DrawerPlugin plugin) {
        syntaxMessage();
        return CommandType.SUCCESS;
    }

}
