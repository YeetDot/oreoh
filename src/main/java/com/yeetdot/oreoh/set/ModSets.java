package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.OreOh;

public class ModSets {
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
    
    public static final AlloySet BRONZE = new AlloySet("bronze", "iron");
    public static final AlloySet BRASS = new AlloySet("brass", "iron");
    public static final AlloySet STEEL = new AlloySet("steel", "iron");
    public static final AlloySet INVAR = new AlloySet("invar", "iron");
    public static final AlloySet ELECTRUM = new AlloySet("electrum", "iron");

    public static final GemSet RUBY = new GemSet("ruby", "stone");
    public static final GemSet PERIDOT = new GemSet("peridot", "stone");
    public static final GemSet SAPPHIRE = new GemSet("sapphire", "stone");
    public static final GemSet CINNABAR = new GemSet("cinnabar", "iron");
    public static final GemSet FLUORITE = new GemSet("fluorite", "iron");

    public static void registerSets() {
        OreOh.LOGGER.info("Registering sets for " + OreOh.MOD_ID);
    }
}
