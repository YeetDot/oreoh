package com.yeetdot.oreoh.set.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public sealed interface MetalTag extends NaturalTag {
    default TagKey<Item> nugget() {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "nuggets/" + name()));
    }
    default TagKey<Item> rawItem() {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "raw_materials/" + name()));
    }
    default TagKey<Block> rawBlock() {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "storage_blocks/raw_" + name()));
    }
    default TagKey<Item> rawBlockItem() {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "storage_blocks/raw_" + name()));
    }

    record Set(String name) implements MetalTag {
        @Override
        public boolean isIngot() {
            return true;
        }
    }
}
