package fr.maxlego08.zdrawer.command.commands;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.command.commands.give.CommandDrawerGive;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;

public class CommandDrawer extends VCommand {

	public CommandDrawer(DrawerPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.ZDRAWER_USE);
		this.addSubCommand(new CommandDrawerReload(plugin));
		this.addSubCommand(new CommandDrawerGive(plugin));
	}

	@Override
	protected CommandType perform(DrawerPlugin plugin) {
		syntaxMessage();
		return CommandType.SUCCESS;
	}

}
