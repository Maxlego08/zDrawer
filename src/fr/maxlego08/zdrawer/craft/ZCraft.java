package fr.maxlego08.zdrawer.craft;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.craft.Craft;
import fr.maxlego08.zdrawer.api.craft.Ingredient;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ZCraft implements Craft {

    protected final DrawerPlugin plugin;
    private final String name;
    private final NamespacedKey namespacedKey;
    private final String[] shade;
    private final Map<Character, Ingredient> ingredients;
    private final MenuItemStack result;

    public ZCraft(DrawerPlugin plugin, String path, YamlConfiguration configuration, String name, File file) throws InventoryException {
        this.plugin = plugin;
        this.name = name;
        this.namespacedKey = new NamespacedKey(plugin, name);
        this.shade = configuration.getStringList(path + "shade").toArray(new String[0]);

        Loader<MenuItemStack> loader = new MenuItemStackLoader(plugin.getInventoryManager());
        this.result = loader.load(configuration, path + "result.", file);
        this.ingredients = new HashMap<>();
        for (String ingredientKey : configuration.getConfigurationSection(path + "ingredients.").getKeys(false)) {
            Ingredient ingredient;
            if (configuration.contains(path + ".ingredients." + ingredientKey + ".customCraft")) {
                ingredient = new ZIngredient(plugin, configuration.getString(path + ".ingredients." + ingredientKey + ".customCraft"));
            } else if (configuration.contains(path + ".ingredients." + ingredientKey + ".upgrade")) {
                ingredient = new ZIngredient(configuration.getString(path + ".ingredients." + ingredientKey + ".upgrade"), plugin);
            } else {
                ingredient = new ZIngredient(plugin, loader.load(configuration, path + ".ingredients." + ingredientKey + ".", file));
            }
            this.ingredients.put(ingredientKey.charAt(0), ingredient);
        }
    }

    @Override
    public NamespacedKey getKey() {
        return this.namespacedKey;
    }

    @Override
    public void register() {

        this.unregister();

        ItemStack resultItemStack = getResultItemStack(null);

        ShapedRecipe recipe = new ShapedRecipe(this.namespacedKey, resultItemStack);
        recipe.shape(this.shade);

        ingredients.forEach((identifier, ingredient) -> {
            ItemStack itemStack = ingredient.build(null);
            System.out.println(identifier + " - " + itemStack);
            recipe.setIngredient(identifier, new RecipeChoice.ExactChoice(itemStack));
        });

        Server server = this.plugin.getServer();
        server.addRecipe(recipe);
        server.updateRecipes();

    }

    @Override
    public void unregister() {
        Server server = this.plugin.getServer();
        server.removeRecipe(this.namespacedKey, false);
    }

    @Override
    public String[] getShade() {
        return this.shade;
    }

    @Override
    public Map<Character, Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public MenuItemStack getResult() {
        return this.result;
    }

    @Override
    public ItemStack getResultItemStack(Player player) {
        ItemStack resultItemStack = result.build(player, false);

        ItemMeta itemMeta = resultItemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(this.plugin.getNamespacedKeyCraft(), PersistentDataType.BOOLEAN, true);
        resultItemStack.setItemMeta(itemMeta);
        return resultItemStack;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
