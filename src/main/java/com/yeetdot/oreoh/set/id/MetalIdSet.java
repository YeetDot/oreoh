package com.yeetdot.oreoh.set.id;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public record MetalIdSet(
        ResourceKey<Item> ingot,
        ResourceKey<Item> nugget,
        ResourceKey<Item> raw,
        ResourceKey<Block> storageBlock,
        ResourceKey<Block> rawBlock,
        ResourceKey<Item> storageBlockItem,
        ResourceKey<Item> rawBlockItem
) {
    public MetalIdSet(String name) {
        this(
                item(String.format("%s_ingot", name)),
                item(String.format("%s_nugget", name)),
                item(String.format("raw_%s", name)),
                block(String.format("%s_block", name)),
                block(String.format("raw_%s_block", name)),
                item(String.format("%s_block", name)),
                item(String.format("raw_%s_block", name))
        );
    }

    private static ResourceKey<Item> item(String name) {
        return ResourceKey.create(Registries.ITEM, OreOh.id(name));
    }

    private static ResourceKey<Block> block(String name) {
        return ResourceKey.create(Registries.BLOCK, OreOh.id(name));
    }
}
