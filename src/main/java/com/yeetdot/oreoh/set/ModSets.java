package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.OreOh;

@SuppressWarnings("unused")
public final class ModSets {
    public static final MetalSet ALUMINUM = new MetalSet("aluminum", "stone", 10, 8, -16, 112, true, false, false, false);
    public static final MetalSet NICKEL = new MetalSet("nickel", "iron", 6, 5, -40, 60, true, false, false, false);
    public static final MetalSet ZINC = new MetalSet("zinc", "stone", 8, 10, 0, 160, true, false, false, false);
    public static final MetalSet SILVER = new MetalSet("silver", "iron", 7, 5, -64, 16, true, true, false, true);
    public static final MetalSet TIN = new MetalSet("tin", "stone", 9, 10, -32, 72, true, false, false, false);
    public static final MetalSet OSMIUM = new MetalSet("osmium", "iron", 4, 2, -64, -8, true, false, false, true); 
    public static final MetalSet PLATINUM = new MetalSet("platinum", "iron", 6, 3, 10, 70, false, false, true, true); 
    public static final MetalSet LEAD = new MetalSet("lead", "stone", 8, 6, -64, 32, true, false, false, false);
    public static final MetalSet URANIUM = new MetalSet("uranium", "diamond", 4, 3, -64, -16, true, false, false, true); 
    public static final MetalSet IRIDIUM = new MetalSet("iridium", "diamond", 3, 2, 5, 65, false, false, true, true);

    public static final AlloySet BRONZE = new AlloySet("bronze", "iron");
    public static final AlloySet BRASS = new AlloySet("brass", "iron");
    public static final AlloySet STEEL = new AlloySet("steel", "iron");
    public static final AlloySet INVAR = new AlloySet("invar", "iron");
    public static final AlloySet ELECTRUM = new AlloySet("electrum", "iron");

    public static final GemSet RUBY = new GemSet("ruby", "stone", 5, 6, 32, 128, true, false, false, false);
    public static final GemSet PERIDOT = new GemSet("peridot", "stone", 4, 5, -16, 80, true, false, false, false);
    public static final GemSet SAPPHIRE = new GemSet("sapphire", "stone", 5, 6, -64, 16, true, false, false, true);
    public static final GemSet CINNABAR = new GemSet("cinnabar", "iron", 8, 8, 10, 110, false, true, false, false);
    public static final GemSet FLUORITE = new GemSet("fluorite", "iron", 6, 6, -64, 32, true, false, false, false);

    public static void registerSets() {
        OreOh.LOGGER.info("Registering sets for " + OreOh.MOD_ID);
    }
}
