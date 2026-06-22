package com.yeetdot.oreoh.set.id;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface MaterialId {
    String name();
    default ResourceKey<Item> primary() {
        return ResourceKey.create(Registries.ITEM, OreOh.id(name()));
    }
    default ResourceKey<Block> storageBlock() {
        return ResourceKey.create(Registries.BLOCK, OreOh.id(name() + "_block"));
    }
}
