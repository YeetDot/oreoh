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

@SuppressWarnings("unused")
public class ModBlocks {
    public static final Block ABLOCK = registerBlock("ablock", Block::new, BlockBehaviour.Properties.of());
    public static final Block CRUSHER = registerBlock("crusher", CrusherBlock::new, BlockBehaviour.Properties.of());
    public static final Block CREATIVE_ENERGY_SOURCE = registerBlock("creative_energy_source", CreativeEnergyBlock::new, BlockBehaviour.Properties.of());
    public static final Block BASIC_BATTERY = registerBlock("basic_battery", properties -> new BatteryBlock(properties, new EnergyStorageStats(100000, 2000, 2000)), BlockBehaviour.Properties.of());
    
    public static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        return registerBlock(name, function, BlockBehaviour.Properties.of());
    }

    public static Block registerBlock(ResourceKey<Block> blockKey, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
        Block block = function.apply(properties.setId(blockKey));

        String name = blockKey.identifier().getPath();
        registerBlockItem(name, block);

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    public static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, OreOh.id(name));
        Block block = function.apply(properties.setId(blockKey));

        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, OreOh.id(name),
                new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, OreOh.id(name)))));
    }

    public static void registerBlocks() {
        OreOh.LOGGER.info("Registering Blocks for " + OreOh.MOD_ID);
    }
}
