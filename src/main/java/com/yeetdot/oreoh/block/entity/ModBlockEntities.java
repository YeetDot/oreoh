package com.yeetdot.oreoh.block.entity;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.energy.WireBlock;
import com.yeetdot.oreoh.energy.WireBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final BlockEntityType<WireBlockEntity> WIRE_BLOCK_ENTITY = register(
            "wire_block_entity", (pos, state) -> {
                if (!(state.getBlock() instanceof WireBlock wireBlock)) {
                    throw new IllegalStateException("Non-WireBlock used WireBlockEntity");
                }
                return new WireBlockEntity(pos, state, wireBlock.electrical, wireBlock.thermal);
            }, ModBlocks.COPPER_WIRE
    );
    public static final BlockEntityType<CreativeGeneratorBlockEntity> CREATIVE_GENERATOR_BLOCK_ENTITY = register(
            "creative_generator_block_entity", CreativeGeneratorBlockEntity::new, ModBlocks.CREATIVE_GENERATOR
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
