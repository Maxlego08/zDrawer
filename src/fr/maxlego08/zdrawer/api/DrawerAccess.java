package fr.maxlego08.zdrawer.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The DrawerAccess interface defines a method for checking if a player has access
 * to a specific drawer at a given location. Implementations of this interface can
 * provide various access control mechanisms to determine whether access should be
 * granted to the player based on certain conditions.
 * <p>
 * This interface allows for the creation of multiple verification checks to grant
 * or deny access to a drawer for a player. These checks can be based on various
 * criteria, such as player permissions, the player's location relative to the drawer,
 * the state of the drawer, or any other custom conditions defined in the implementation.
 * <p>
 * Implementing classes are responsible for defining the logic within the {@code hasAccess}
 * method to evaluate these conditions and return a boolean value indicating whether
 * the player is allowed to access the drawer.
 *
 * @apiNote This interface is useful in scenarios where drawers have restricted access,
 * and not every player is allowed to interact with every drawer. For example, in a game
 * or a mod where players own private drawers or where certain drawers are locked and require
 * specific keys or permissions to open.
 */
public interface DrawerAccess {

    /**
     * Checks if the given player has access to the specified drawer at the provided location.
     * <p>
     * This method should implement the logic to determine if access should be granted
     * to the player based on the implementation's specific criteria. These criteria
     * could involve checking the player's permissions, the proximity of the player to
     * the drawer, or any other relevant conditions.
     *
     * @param player The player whose access is being checked.
     * @param location The location where the drawer is located.
     * @param drawer The drawer to which access may be granted or denied.
     * @return {@code true} if the player has access to the drawer, {@code false} otherwise.
     */
    boolean hasAccess(Player player, Location location, Drawer drawer);
}

