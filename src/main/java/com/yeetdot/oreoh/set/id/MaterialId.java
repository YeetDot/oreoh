package com.yeetdot.oreoh.set.id;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public sealed interface MaterialId permits AlloyId, MaterialId.Set, NaturalId {
    String name();
    boolean isIngot();
    default ResourceKey<Item> primary() {
        return ResourceKey.create(Registries.ITEM, OreOh.id(name() + (isIngot() ? "_ingot" : "")));
    }
    default ResourceKey<Block> storageBlock() {
        return ResourceKey.create(Registries.BLOCK, OreOh.id(name() + "_block"));
    }

    record Set(String name, boolean isIngot) implements MaterialId {}
}
