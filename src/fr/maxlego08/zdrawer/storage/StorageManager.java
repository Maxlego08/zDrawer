package fr.maxlego08.zdrawer.storage;

import fr.maxlego08.zdrawer.api.enums.StorageType;
import fr.maxlego08.zdrawer.api.storage.DrawerStorage;
import fr.maxlego08.zdrawer.api.storage.IStorage;
import fr.maxlego08.zdrawer.storage.storages.JsonStorage;
import fr.maxlego08.zdrawer.zcore.utils.storage.Persist;

public class StorageManager implements DrawerStorage {

    private final StorageType storageType = StorageType.JSON;
    private IStorage storage = new JsonStorage();

    @Override
    public void save(Persist persist) {

        this.storage.save();

        switch (this.storageType) {
            case JSON:
                persist.save(storage, "drawers");
                break;
        }
    }

    @Override
    public void load(Persist persist) {

        switch (this.storageType) {
            case JSON:
                this.storage = persist.loadOrSaveDefault((JsonStorage) this.storage, JsonStorage.class, "drawers");
                break;
        }

        this.storage.load();
    }

    @Override
    public IStorage getStorage() {
        return this.storage;
    }

    @Override
    public void setStorage(IStorage storage) {
        this.storage = storage;
    }
}