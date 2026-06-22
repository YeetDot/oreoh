package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.OreOh;

public class ModMetals {
    public static final MetalSet ALUMINUM = new MetalSet("aluminum", "stone");
    public static final MetalSet NICKEL = new MetalSet("nickel", "iron");
    public static final MetalSet ZINC = new MetalSet("zinc",  "stone");
    public static final MetalSet SILVER = new MetalSet("silver", "stone");
    public static final MetalSet TIN = new MetalSet("tin", "stone");
    public static final MetalSet OSMIUM = new MetalSet("osmium", "iron");
    public static final MetalSet IRIDIUM = new MetalSet("iridium", "diamond");
    public static final MetalSet PLATINUM = new MetalSet("platinum", "iron");
    public static final MetalSet LEAD = new MetalSet("lead", "stone");
    public static final MetalSet URANIUM = new MetalSet("uranium",  "diamond");

    public static void registerMetals() {
        OreOh.LOGGER.info("Registering metals for " + OreOh.MOD_ID);
    }
}
