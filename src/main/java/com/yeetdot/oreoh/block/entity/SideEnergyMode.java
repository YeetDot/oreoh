package com.yeetdot.oreoh.block.entity;

import com.mojang.serialization.Codec;

public enum SideEnergyMode {
    NONE,
    IN,
    OUT,
    IN_OUT;

    public SideEnergyMode next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
    public static final Codec<SideEnergyMode> CODEC = Codec.STRING.xmap(SideEnergyMode::valueOf, SideEnergyMode::name);
}
