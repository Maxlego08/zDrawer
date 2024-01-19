package fr.maxlego08.zdrawer.api.storage;

public interface DrawerStorage extends Savable, NoReloadable {

    void setStorage(IStorage storage);

    IStorage getStorage();

}
