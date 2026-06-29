package com.yeetdot.oreoh.world;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.set.SetApplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {

    public static void bootstrap(final BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackReplaceables = new TagMatchTest(BlockTags.BASE_STONE_NETHER);
        RuleTest endStoneReplaceables = new BlockMatchTest(Blocks.END_STONE);

        SetApplier.applyToNaturals(set -> {
            if (set.placeInOverworld) {
                var overworldTargets = List.of(
                        OreConfiguration.target(stoneReplaceables, set.oreStone.defaultBlockState()),
                        OreConfiguration.target(deepslateReplaceables, set.oreDeepslate.defaultBlockState())
                );
                register(context, getOverworldKey(set.name()), Feature.ORE,
                        new OreConfiguration(overworldTargets, set.veinSize));
            }

            if (set.placeInNether) {
                var netherTargets = List.of(
                        OreConfiguration.target(netherrackReplaceables, set.oreNether.defaultBlockState())
                );
                register(context, getNetherKey(set.name()), Feature.ORE,
                        new OreConfiguration(netherTargets, set.veinSize));
            }

            if (set.placeInEnd) {
                var endTargets = List.of(
                        OreConfiguration.target(endStoneReplaceables, set.oreEnd.defaultBlockState())
                );
                register(context, getEndKey(set.name()), Feature.ORE,
                        new OreConfiguration(endTargets, set.veinSize));
            }
        });
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, OreOh.id(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> id, F feature, FC configuration
    ) {
        context.register(id, new ConfiguredFeature<>(feature, configuration));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> getOverworldKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, OreOh.id(name + "_ore"));
    }
    public static ResourceKey<ConfiguredFeature<?, ?>> getNetherKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, OreOh.id("nether_" + name + "_ore"));
    }
    public static ResourceKey<ConfiguredFeature<?, ?>> getEndKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, OreOh.id("end_" + name + "_ore"));
    }
}
