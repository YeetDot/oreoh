package com.yeetdot.oreoh.set.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public record OreTagSet(
        TagKey<Block> blockTagOre,
        TagKey<Item> itemTagOre
) {
    public OreTagSet(String name) {
        this(
                TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", String.format("ores/%s", name))),
                TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", String.format("ores/%s", name)))
        );
    }
}