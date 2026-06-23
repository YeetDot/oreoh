package com.yeetdot.oreoh.set.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public sealed interface AlloyTag extends MaterialTag {
    default TagKey<Item> nugget() {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "nuggets/" + name()));
    }

    record Set(String name) implements AlloyTag {
        @Override
        public boolean isIngot() {
            return true;
        }
    }
}
