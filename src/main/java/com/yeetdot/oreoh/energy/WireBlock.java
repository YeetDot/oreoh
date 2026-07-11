package com.yeetdot.oreoh.energy;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yeetdot.oreoh.block.entity.ModBlockEntities;
import com.yeetdot.oreoh.data.ElectricGridRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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
        
        if (level instanceof ServerLevel serverLevel) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof WireBlockEntity wireBlockEntity) {
                ElectricGridRegistry.get(serverLevel).registerPlacement(serverLevel, pos);
                wireBlockEntity.setAmbientTemperature(level);
            }
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel 
                ? createTickerHelper(type, ModBlockEntities.WIRE_BLOCK_ENTITY, ((_, pos, _, entity) -> WireBlockEntity.serverTick(serverLevel, pos, entity)))
                : null;
    }
}
