package com.yeetdot.oreoh.set.id;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public interface NaturalId extends MaterialId {
    default ResourceKey<Block> oreStone() {
        return ResourceKey.create(Registries.BLOCK, OreOh.id(name() + "_ore"));
    }
    default ResourceKey<Block> oreDeepslate() {
        return ResourceKey.create(Registries.BLOCK, OreOh.id("deepslate_" + name() + "_ore"));
    }
    default ResourceKey<Block> oreNether() {
        return ResourceKey.create(Registries.BLOCK, OreOh.id("nether_" + name() + "_ore"));
    }
    default ResourceKey<Block> oreEnd() {
        return ResourceKey.create(Registries.BLOCK, OreOh.id("end_" + name() + "_ore"));
    }
}