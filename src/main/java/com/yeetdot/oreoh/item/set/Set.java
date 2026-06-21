package com.yeetdot.oreoh.item.set;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class Set {
    static Item item(String name) {
        return ModItems.register(name);
    }

    static Block block(String name, BlockBehaviour.Properties copyProperties) {
        return ModBlocks.registerBlock(name, _ -> new Block(copyProperties));
    }
}
