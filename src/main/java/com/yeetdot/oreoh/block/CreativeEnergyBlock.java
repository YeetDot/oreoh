package com.yeetdot.oreoh.block;

import com.yeetdot.oreoh.block.entity.CreativeEnergyBlockEntity;
import com.yeetdot.oreoh.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class CreativeEnergyBlock extends BatteryBlock {
    public CreativeEnergyBlock(Properties properties) {
        super(properties, new EnergyStorageStats(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new CreativeEnergyBlockEntity(worldPosition, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
         return level instanceof ServerLevel serverLevel
                ? createTickerHelper(type, ModBlockEntities.CREATIVE_ENERGY_BLOCK_ENTITY, (_, blockPos, _, entity) -> CreativeEnergyBlockEntity.serverTick(serverLevel, blockPos, entity))
                : null;
    }
}
