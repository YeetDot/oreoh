package com.yeetdot.oreoh.energy;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yeetdot.oreoh.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;

public class WireBlock extends BaseEntityBlock {
    public static final MapCodec<WireBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                propertiesCodec(),
                ElectricalProperties.CODEC.fieldOf("electrical").forGetter(wireBlock -> wireBlock.electrical),
                ThermalProperties.CODEC.fieldOf("thermal").forGetter(wireBlock -> wireBlock.thermal)
        ).apply(instance, WireBlock::new)
    );
    public final ElectricalProperties electrical;
    public final ThermalProperties thermal;
    

    public WireBlock(Properties properties, ElectricalProperties electrical, ThermalProperties thermal) {
        super(properties);
        this.electrical = electrical;
        this.thermal = thermal;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new WireBlockEntity(worldPosition, blockState, electrical, thermal);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof WireBlockEntity wireBlockEntity) {
                WireNetworkHelper.onWirePlaced(level, pos, wireBlockEntity);
                wireBlockEntity.setAmbientTemperature(level);
            }
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof WireBlockEntity wireBlockEntity) {
                WireNetworkHelper.onNeighborUpdate(level, pos, wireBlockEntity);
            }
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel 
                ? createTickerHelper(type, ModBlockEntities.WIRE_BLOCK_ENTITY, ((_, _, _, entity) -> WireBlockEntity.serverTick(serverLevel, entity)))
                : null;
    }
}
