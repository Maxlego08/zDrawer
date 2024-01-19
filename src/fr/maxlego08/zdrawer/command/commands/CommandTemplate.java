package fr.maxlego08.zdrawer.command.commands;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;

public class CommandTemplate extends VCommand {

	public CommandTemplate(DrawerPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.EXAMPLE_PERMISSION);
		this.addSubCommand(new CommandTemplateReload(plugin));
	}

	@Override
	protected CommandType perform(DrawerPlugin plugin) {
		syntaxMessage();
		return CommandType.SUCCESS;
	}

}
