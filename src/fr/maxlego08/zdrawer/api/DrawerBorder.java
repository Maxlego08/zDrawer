package fr.maxlego08.zdrawer.api;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.zdrawer.api.utils.DisplaySize;

public interface DrawerBorder {

    boolean isEnable();

    MenuItemStack getMenuItemStack();

    DisplaySize getUpScale();
    DisplaySize getDownScale();
    DisplaySize getLeftScale();
    DisplaySize getRightScale();

}
