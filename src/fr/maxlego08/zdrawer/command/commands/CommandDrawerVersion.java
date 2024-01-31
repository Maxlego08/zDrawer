package fr.maxlego08.zdrawer.command.commands;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;

public class CommandDrawerVersion extends VCommand {

    public CommandDrawerVersion(DrawerPlugin plugin) {
        super(plugin);
        this.addSubCommand("version", "ver", "v", "?");
        this.setDescription(Message.DESCRIPTION_VERSION);
    }

    @Override
    protected CommandType perform(DrawerPlugin plugin) {

        message(plugin, sender, "#11ed7fVersion du plugin#757575: #46a3eb" + plugin.getDescription().getVersion());
        message(plugin, sender, "#11ed7fAuteur#757575: #46a3ebMaxlego08");
        message(plugin, sender, "#11ed7fDiscord#757575: #46a3ebhttp://discord.groupez.dev/");
        message(plugin, sender, "#11ed7fDownload here#757575: #46a3ebhttps://groupez.dev/resources/313");
        message(plugin, sender, "#11ed7fSponsor#757575: #bf3030https://serveur-minecraft-vote.fr/?ref=345");

        return CommandType.SUCCESS;
    }

}
