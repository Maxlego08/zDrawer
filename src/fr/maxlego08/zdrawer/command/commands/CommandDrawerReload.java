package fr.maxlego08.zdrawer.command.commands;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.zcore.enums.Message;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;

public class CommandDrawerReload extends VCommand {

	public CommandDrawerReload(DrawerPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.ZDRAWER_RELOAD);
		this.addSubCommand("reload", "rl");
		this.setDescription(Message.DESCRIPTION_RELOAD);
	}

	@Override
	protected CommandType perform(DrawerPlugin plugin) {

		plugin.reloadConfig();
		plugin.reloadFiles();
		message(sender, Message.RELOAD);
		
		return CommandType.SUCCESS;
	}

}
