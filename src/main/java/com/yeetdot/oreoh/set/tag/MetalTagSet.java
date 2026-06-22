package com.yeetdot.oreoh.set.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public record MetalTagSet(
        TagKey<Item> itemTagIngot,
        TagKey<Item> itemTagNugget,
        TagKey<Item> itemTagRaw,
        TagKey<Block> blockTagBlock,
        TagKey<Block> blockTagRawBlock,
        TagKey<Item> itemTagBlock,
        TagKey<Item> itemTagRawBlock
) {
    // This compact constructor automatically instantiates all your header fields!
    public MetalTagSet(String name) {
        this(
                TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", String.format("ingots/%s", name))),
                TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", String.format("nuggets/%s", name))),
                TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", String.format("raw_materials/%s", name))),
                TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", String.format("storage_blocks/%s", name))),
                TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", String.format("storage_blocks/raw_%s", name))),
                TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", String.format("storage_blocks/%s", name))),
                TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", String.format("storage_blocks/raw_%s", name)))
        );
    }
}
