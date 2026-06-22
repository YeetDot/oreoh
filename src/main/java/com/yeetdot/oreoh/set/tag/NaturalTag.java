package com.yeetdot.oreoh.set.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface NaturalTag extends MaterialTag{
    default TagKey<Block> oreParent() {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores/" + name()));
    }
    default TagKey<Item> oreParentItem() {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ores/" + name()));
    }
}
