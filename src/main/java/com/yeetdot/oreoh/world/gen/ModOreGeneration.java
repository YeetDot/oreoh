package com.yeetdot.oreoh.world.gen;

import com.yeetdot.oreoh.set.SetApplier;
import com.yeetdot.oreoh.world.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModOreGeneration {
    public static void generateOres() {
        SetApplier.applyToNaturals(set -> {
            if (set.placeInOverworld) {
                var biomeSelection = BiomeSelectors.foundInOverworld();

                switch (set.name()) {
                    case "ruby" -> biomeSelection = BiomeSelectors.tag(BiomeTags.IS_MOUNTAIN);
                    case "sapphire" -> biomeSelection = BiomeSelectors.tag(BiomeTags.IS_OCEAN).and(BiomeSelectors.tag(BiomeTags.IS_RIVER));
                }

                BiomeModifications.addFeature(biomeSelection, GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.getOverworldKey(set.name()));
            }
            if (set.placeInNether) {
                BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.getNetherKey(set.name()));
            }
            if (set.placeInEnd) {
                BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.getEndKey(set.name()));
            }
        });
    }
}
