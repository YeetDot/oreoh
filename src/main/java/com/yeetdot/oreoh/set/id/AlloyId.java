package com.yeetdot.oreoh.set.id;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public sealed interface AlloyId extends MaterialId {
    default ResourceKey<Item> nugget() {
        return ResourceKey.create(Registries.ITEM, OreOh.id(name() + "_nugget"));
    }

    record Set(String name) implements AlloyId {
        @Override
        public boolean isIngot() {
            return true;
        }
    }
}
