package com.yeetdot.oreoh.set;

import java.util.function.Consumer;

public class SetApplier {
    public static void applyToMetals(Consumer<MetalSet> consumer) {
        MetalSet.METAL_SETS.forEach(consumer);
    }

    public static void applyToOres (Consumer<OreSet> consumer) {
        OreSet.ORE_SETS.forEach(consumer);
    }
}
