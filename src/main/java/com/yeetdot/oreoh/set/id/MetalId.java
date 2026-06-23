package com.yeetdot.oreoh.set.id;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public sealed interface MetalId extends NaturalId {
    default ResourceKey<Item> nugget() {
        return ResourceKey.create(Registries.ITEM, OreOh.id(name() + "_nugget"));
    }
    default ResourceKey<Item> rawItem() {
        return ResourceKey.create(Registries.ITEM, OreOh.id("raw_" + name()));
    }
    default ResourceKey<Block> rawBlock() {
        return ResourceKey.create(Registries.BLOCK, OreOh.id("raw_" + name() + "_block"));
    }

    record Set(String name) implements MetalId {
        @Override
        public boolean isIngot() {
            return true;
        }
    }
}
