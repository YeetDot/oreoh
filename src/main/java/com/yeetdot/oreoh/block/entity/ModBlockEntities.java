package com.yeetdot.oreoh.block.entity;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.block.BatteryBlock;
import com.yeetdot.oreoh.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<CrusherBlockEntity> CRUSHER_BLOCK_ENTITY = register(
            "crusher_block_entity", CrusherBlockEntity::new, ModBlocks.CRUSHER
    );
    public static final BlockEntityType<CreativeEnergyBlockEntity> CREATIVE_ENERGY_BLOCK_ENTITY = register(
            "creative_energy_block_entity", CreativeEnergyBlockEntity::new, ModBlocks.CREATIVE_ENERGY_SOURCE
    );
    public static final BlockEntityType<BatteryBlockEntity> BATTERY_BLOCK_ENTITY = register(
            "battery_block_entity", (pos, state) -> {
                if (!(state.getBlock() instanceof BatteryBlock batteryBlock)) {
                    throw new IllegalStateException("Non-BatteryBlock used BatteryBlockEntity");
                }
                return new BatteryBlockEntity(pos, state, batteryBlock.stats);
            }, 
            ModBlocks.BASIC_BATTERY
    );

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name, FabricBlockEntityTypeBuilder.Factory<T> supplier, Block... blocks
    ) {
        return Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                OreOh.id(name),
                FabricBlockEntityTypeBuilder.create(supplier, blocks).build()
                );
    }
    
    public static void registerBlockEntities() {
        OreOh.LOGGER.info("Registering Block Entities for " + OreOh.MOD_ID);
    }
}
