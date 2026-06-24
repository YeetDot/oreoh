package com.yeetdot.oreoh.world;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.set.NaturalSet;
import com.yeetdot.oreoh.set.SetApplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModPlacedFeatures {

    public static void bootstrap(final BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        SetApplier.applyToNaturals(set -> {
            if (set.placeInOverworld) {
                Holder<ConfiguredFeature<?, ?>> overworld = configuredFeatures.getOrThrow(ModConfiguredFeatures.getOverworldKey(set.name()));

                register(context, getOverworldKey(set.name()), overworld, getPlacementModifiers(set));
            }
            if (set.placeInNether) {
                Holder<ConfiguredFeature<?, ?>> nether = configuredFeatures.getOrThrow(ModConfiguredFeatures.getNetherKey(set.name()));

                register(context, getNetherKey(set.name()), nether, getPlacementModifiers(set));
            }
            if (set.placeInEnd) {
                Holder<ConfiguredFeature<?, ?>> end = configuredFeatures.getOrThrow(ModConfiguredFeatures.getEndKey(set.name()));

                register(context, getEndKey(set.name()), end, getPlacementModifiers(set));
            }
        });
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, OreOh.id(name));
    }

    private static void register(
            BootstrapContext<PlacedFeature> context,
            ResourceKey<PlacedFeature> id,
            Holder<ConfiguredFeature<?, ?>> feature,
            List<PlacementModifier> placementModifiers
            ) {
        context.register(id, new PlacedFeature(feature, placementModifiers));
    }

    public static ResourceKey<PlacedFeature> getOverworldKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, OreOh.id(name + "_ore"));
    }

    public static ResourceKey<PlacedFeature> getNetherKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, OreOh.id("nether_" + name + "_ore"));
    }

    public static ResourceKey<PlacedFeature> getEndKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, OreOh.id("end_" + name + "_ore"));
    }

    private static List<PlacementModifier> getPlacementModifiers(NaturalSet set) {
        PlacementModifier heightPlacement = set.isTrapezoidal
                ? HeightRangePlacement.triangle(VerticalAnchor.absolute(set.minY), VerticalAnchor.absolute(set.maxY))
                : HeightRangePlacement.uniform(VerticalAnchor.absolute(set.minY), VerticalAnchor.absolute(set.maxY));
        return ModOrePlacement.commonOrePlacement(set.frequency, heightPlacement);
    }
}
