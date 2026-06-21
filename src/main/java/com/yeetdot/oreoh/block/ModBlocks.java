package com.yeetdot.oreoh.block;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    public static final Block ABLOCK = registerBlock("ablock", Block::new);

    public static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        Block block = function.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, OreOh.id(name))));
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, OreOh.id(name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, OreOh.id(name), new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, OreOh.id(name)))));
    }

    public static void registerBlocks() {
        OreOh.LOGGER.info("Registering Blocks for " + OreOh.MOD_ID);
    }
}
