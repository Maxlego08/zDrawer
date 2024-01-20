package fr.maxlego08.zdrawer.api;

import fr.maxlego08.zdrawer.api.craft.Craft;
import fr.maxlego08.zdrawer.api.storage.Savable;

import java.util.Optional;

public interface DrawerManager extends Savable {

    long getDrawerLimit();

    Optional<Craft> getCraft(String craftName);

}
