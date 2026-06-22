package com.yeetdot.oreoh.set.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface MaterialTag {
    String name();
    default TagKey<Item> primary(String category) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", category + "/" + name()));
    }
    default TagKey<Block> storageBlock() {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "storage_blocks/" + name()));
    }
    default TagKey<Item> storageBlockItem() {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "storage_blocks/" + name()));
    }
}
