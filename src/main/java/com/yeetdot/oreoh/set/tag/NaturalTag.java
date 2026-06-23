package com.yeetdot.oreoh.set.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public sealed interface NaturalTag extends MaterialTag permits MetalTag, GemTag, NaturalTag.Set {
    default TagKey<Block> oreParent() {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores/" + name()));
    }
    default TagKey<Item> oreParentItem() {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ores/" + name()));
    }

    record Set(String name, boolean isIngot) implements NaturalTag {}
}
