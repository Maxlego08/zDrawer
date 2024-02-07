package fr.maxlego08.zdrawer.api.storage;

import fr.maxlego08.zdrawer.api.enums.StorageType;

/**
 * Interface defining the storage system for managing drawer data.
 * It extends Savable for persistence capabilities and NoReloadable to indicate it doesn't support reloading.
 */
public interface DrawerStorage extends Savable, NoReloadable {

    /**
     * Sets the storage mechanism used for persisting drawer data.
     *
     * @param storage The storage implementation to be used.
     */
    void setStorage(IStorage storage);

    /**
     * Retrieves the current storage mechanism being used for drawer data.
     *
     * @return The current implementation of IStorage in use.
     */
    IStorage getStorage();

    StorageType getStorageType();

}
