package com.yeetdot.oreoh.set.id;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public record OreIdSet(
        ResourceKey<Block> ore,
        ResourceKey<Block> deepslateOre,
        ResourceKey<Item> oreItem,
        ResourceKey<Item> deepslateOreItem
) {
    public OreIdSet(String name) {
        this(
                block(String.format("%s_ore", name)),
                block(String.format("deepslate_%s_ore", name)),
                item(String.format("%s_ore", name)),
                item(String.format("deepslate_%s_ore", name))
        );
    }

    private static ResourceKey<Item> item(String name) {
        return ResourceKey.create(Registries.ITEM, OreOh.id(name));
    }

    private static ResourceKey<Block> block(String name) {
        return ResourceKey.create(Registries.BLOCK, OreOh.id(name));
    }
}
