package com.yeetdot.oreoh.block.entity;

import com.yeetdot.oreoh.api.IGeneratorNode;
import com.yeetdot.oreoh.energy.ElectricGrid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;

public class CreativeGeneratorBlockEntity extends BlockEntity implements IGeneratorNode {
    private final Direction facing;
    @Nullable private ElectricGrid linkedGrid;
    
    public CreativeGeneratorBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.CREATIVE_GENERATOR_BLOCK_ENTITY, worldPosition, blockState);
        facing = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public double getAvailableWatts() {
        return Double.MAX_VALUE;
    }

    @Override
    public double getVoltage() {
        return 120.0;
    }

    @Override
    public void updateLinkedGrid(@Nullable ElectricGrid grid) {
        this.linkedGrid = grid;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CreativeGeneratorBlockEntity generatorBlockEntity) {
        generatorBlockEntity.tickSimulation(level);
    }
    
    public void tickSimulation(Level level) {
        
    }
}
