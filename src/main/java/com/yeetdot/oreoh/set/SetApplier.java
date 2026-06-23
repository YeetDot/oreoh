package com.yeetdot.oreoh.set;

import java.util.function.Consumer;

public class SetApplier {
    public static void applyToMaterials(Consumer<MaterialSet> consumer) {
        MaterialSet.SETS.forEach(consumer);
    }

    public static void applyToNaturals(Consumer<NaturalSet> consumer) {
        NaturalSet.SETS.forEach(consumer);
    }

    public static void applyToMetals(Consumer<MetalSet> consumer) {
        MetalSet.SETS.forEach(consumer);
    }

    public static void applyToAlloys(Consumer<AlloySet> consumer) {
        AlloySet.SETS.forEach(consumer);
    }

    public static void applyToGems(Consumer<GemSet> consumer) {
        GemSet.SETS.forEach(consumer);
    }

}
