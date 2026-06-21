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
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ModBlocks {
    public static final Block ABLOCK = registerBlock("ablock", Block::new);

    public static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        // 1. Create the registry resource key ahead of time
        ResourceKey<@NotNull Block> blockKey = ResourceKey.create(Registries.BLOCK, OreOh.id(name));

        // 2. Generate clean baseline properties and apply the id
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().setId(blockKey);

        // 3. Apply your existing lambda function signature
        Block block = function.apply(properties);

        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, OreOh.id(name), new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, OreOh.id(name)))));
    }

    public static void registerBlocks() {
        OreOh.LOGGER.info("Registering Blocks for " + OreOh.MOD_ID);
    }
}
