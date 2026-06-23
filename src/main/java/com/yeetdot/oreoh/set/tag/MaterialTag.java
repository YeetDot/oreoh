package com.yeetdot.oreoh.set.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public sealed interface MaterialTag permits AlloyTag, MaterialTag.Set, NaturalTag {
    String name();
    boolean isIngot();
    default TagKey<Item> primary() {
        String category = isIngot() ? "ingot" : "gems";
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", category + "/" + name()));
    }
    default TagKey<Block> storageBlock() {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "storage_blocks/" + name()));
    }
    default TagKey<Item> storageBlockItem() {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "storage_blocks/" + name()));
    }

    record Set(String name, boolean isIngot) implements MaterialTag {}
}
