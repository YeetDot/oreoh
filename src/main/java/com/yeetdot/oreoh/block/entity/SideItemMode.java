package com.yeetdot.oreoh.block.entity;

public enum SideItemMode {
    NONE,
    INPUT,
    OUTPUT,
    IN_OUT,
    CATALYST,
    ALL;

    public SideItemMode next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
