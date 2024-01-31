# zDrawer

## Commands and permissions:
- /zdrawer » Main command (Alaises: /drawer) - zdrawer.use
- /zdrawer reload » Reload config files - zdrawer.reload
- /zdrawer give » Give commands - zdrawer.craft.use
- /zdrawer give drawer <player> [<upgrade name>] [<material>] [<amount>] » Give a drawer - zdrawer.give.drawer
- /zdrawer give craft <player> [<craft name>] » Give a craft item - zdrawer.give.craft
- /zdrawer place <world name> <x> <y> <z> <block face> [<upgrade name>] [<material>] [<amount>] » Place a drawer - zdrawer.place
- /zdrawer purge <world name> <break block> » Delete all the drawer in a specific world - zdrawer.purge
- /zdrawer clear » Allows to remove all entities that come from the plugin. In case of a crash of your server or other it is possible that entities are duplicated. This command deletes them. - zdrawer.clear

## Placeholders
- %zdrawer_content_<index>% » Displays the contents of the drawer with which the player interacts
- %zdrawer_amount_<index>% » Displays the amount of the drawer with which the player interacts
- %zdrawer_amount_formatted_<index>% » Displays the amount with the format of the drawer with which the player interacts
- %zdrawer_upgrade% » Displays the upgrade of the drawer with which the player interacts

For item configuration, you can use the zMenu documentation: https://docs.zmenu.dev/configurations/items
