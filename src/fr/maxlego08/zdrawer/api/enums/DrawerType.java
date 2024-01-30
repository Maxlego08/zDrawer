package fr.maxlego08.zdrawer.api.enums;

public enum DrawerType {

    SINGLE(1),
    DUO(2),
    TRIO(3),
    QUAD(4),

    ;

    private final int size;

    DrawerType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
