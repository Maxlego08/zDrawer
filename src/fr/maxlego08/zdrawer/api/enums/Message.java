package fr.maxlego08.zdrawer.api.enums;

import fr.maxlego08.zdrawer.zcore.enums.MessageType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Message {

    PREFIX("#4ae0b6zDrawer #575757• &f"),

    TELEPORT_MOVE("§cYou must not move!"),
    TELEPORT_MESSAGE("§7Teleportation in §3%second% §7seconds!"),
    TELEPORT_ERROR("§cYou already have a teleportation in progress!"),
    TELEPORT_SUCCESS("§7Teleportation done!"),
    INVENTORY_CLONE_NULL("§cThe inventory clone is null!"),
    INVENTORY_OPEN_ERROR("§cAn error occurred with the opening of the inventory §6%id%§c."),
    TIME_DAY("%02d %day% %02d %hour% %02d %minute% %02d %second%"),
    TIME_HOUR("%02d %hour% %02d minute(s) %02d %second%"),
    TIME_MINUTE("%02d %minute% %02d %second%"),
    TIME_SECOND("%02d %second%"),

    FORMAT_SECOND("second"),
    FORMAT_SECONDS("seconds"),

    FORMAT_MINUTE("minute"),
    FORMAT_MINUTES("minutes"),

    FORMAT_HOUR("hour"),
    FORMAT_HOURS("hours"),

    FORMAT_DAY("d"),
    FORMAT_DAYS("days"),

    COMMAND_SYNTAXE_ERROR("§cYou must execute the command like this§7: §a%syntax%"),
    COMMAND_NO_PERMISSION("§cYou do not have permission to run this command."),
    COMMAND_NO_CONSOLE("§cOnly one player can execute this command."),
    COMMAND_NO_ARG("§cImpossible to find the command with its arguments."),
    COMMAND_SYNTAXE_HELP("§f%syntax% §7» §7%description%"),

    RELOAD("§aYou have just reloaded the configuration files."),
    DESCRIPTION_RELOAD("Reload configuration files"),
    DESCRIPTION_CLEAR("Allows to remove all entities that come from the plugin. In case of a crash of your server or other it is possible that entities are duplicated. This command deletes them."),
    DESCRIPTION_CONVERT("Convert your data to another type of storage."),
    DESCRIPTION_GIVE_USE("Show give commands"),
    DESCRIPTION_GIVE_DRAWER("Give a drawer to a player"),
    DESCRIPTION_PLACE("Place a drawer"),
    DESCRIPTION_PURGE("Delete all the drawer in a specific world"),
    DESCRIPTION_GIVE_CRAFT("Give a custom craft item to a player"),
    DESCRIPTION_VERSION("Show plugin version"),
    DESCRIPTION_DEBUG("Do a debug thing"),

    UPGRADE_ERROR_LIMIT("§cThe drawer already has a larger limit, you can not add this limit."),

    UPGRADE_SUCCESS("§aYou just put the upgrade §f%name%§a on the drawer."),

    EMPTY_DRAWER("Empty"),
    EMPTY_UPGRADE("#e32f1b✘"),

    DRAWER_NOT_FOUND("§cCan’t find the drawer %name%§c."),

    DRAWER_GIVE_RECEIVE("§aYou just got a drawer."),
    DRAWER_GIVE_SENDER("§aYou just gave a Drawer to §f%player%§a."),

    CRAFT_GIVE_ERROR("§cCraft §f%name%§c not found."),
    CRAFT_GIVE_RECEIVE("§aYou just got a §f%name%§a."),
    CRAFT_GIVE_SENDER("§aYou just gave a §b%name%§a to §f%player%§a."),

    DRAWER_PLACE_ERROR("§cA drawer is already in this position."),
    DRAWER_PLACE_SUCCESS("§aYou have just placed a drawer in the world §f§n%world%§r §aat §f%x%§7, §f%y%§7, §f%z%§a."),
    DRAWER_PURGE("§aYou just purged the world §f%world%§a."),

    CLEAR_DISPLAY("§aYou just removed all the entities that come from the plugin. §7Entities deleted§8: §f%amount%"),

    DISABLE_WORLD("§cThe world is disabled. You cannot place a drawer here."),
    BREAK_LIMIT("§cYour drawer contains too many items to be broken, you must empty it before."),

    DRAWER_DEBUG_DISABLE("§cDisable drawers in your chunk. §8(§7Enable draw in 20 ticks§8)"),
    DRAWER_DEBUG_ENABLE("§eEnable drawers in your chunk !"),

    ;

    private List<String> messages;
    private String message;
    private Map<String, Object> titles = new HashMap<>();
    private boolean use = true;
    private MessageType type = MessageType.TCHAT;

    private ItemStack itemStack;

    /**
     * @param message
     */
    private Message(String message) {
        this.message = message;
        this.use = true;
    }

    /**
     * @param title
     * @param subTitle
     * @param a
     * @param b
     * @param c
     */
    private Message(String title, String subTitle, int a, int b, int c) {
        this.use = true;
        this.titles.put("title", title);
        this.titles.put("subtitle", subTitle);
        this.titles.put("start", a);
        this.titles.put("time", b);
        this.titles.put("end", c);
        this.titles.put("isUse", true);
        this.type = MessageType.TITLE;
    }

    /**
     * @param message
     */
    private Message(String... message) {
        this.messages = Arrays.asList(message);
        this.use = true;
    }

    /**
     * @param message
     */
    private Message(MessageType type, String... message) {
        this.messages = Arrays.asList(message);
        this.use = true;
        this.type = type;
    }

    /**
     * @param message
     */
    private Message(MessageType type, String message) {
        this.message = message;
        this.use = true;
        this.type = type;
    }

    /**
     * @param message
     * @param use
     */
    private Message(String message, boolean use) {
        this.message = message;
        this.use = use;
    }

    public String getMessage() {
        return message;
    }

    public String toMsg() {
        return message;
    }

    public String msg() {
        return message;
    }

    public boolean isUse() {
        return use;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getMessages() {
        return messages == null ? Arrays.asList(message) : messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public boolean isMessage() {
        return messages != null && messages.size() > 1;
    }

    public String getTitle() {
        return (String) titles.get("title");
    }

    public Map<String, Object> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, Object> titles) {
        this.titles = titles;
        this.type = MessageType.TITLE;
    }

    public String getSubTitle() {
        return (String) titles.get("subtitle");
    }

    public boolean isTitle() {
        return titles.containsKey("title");
    }

    public int getStart() {
        return ((Number) titles.get("start")).intValue();
    }

    public int getEnd() {
        return ((Number) titles.get("end")).intValue();
    }

    public int getTime() {
        return ((Number) titles.get("time")).intValue();
    }

    public boolean isUseTitle() {
        return (boolean) titles.getOrDefault("isUse", "true");
    }

    public String replace(String a, String b) {
        return message.replace(a, b);
    }

    public MessageType getType() {
        return type;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public boolean isValid() {

        switch (type){
            case ACTION:
                return this.message != null;
            case CENTER:
            case TCHAT: return this.message != null || !this.messages.isEmpty();
            case TITLE:
            case NONE: return true;
        }

        return true;
    }
}

