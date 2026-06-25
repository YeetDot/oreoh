package com.yeetdot.oreoh.block;

import com.mojang.serialization.MapCodec;
import com.yeetdot.oreoh.block.entity.CrusherBlockEntity;
import com.yeetdot.oreoh.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CrusherBlock extends AbstractMachineBlock{
    public static final MapCodec<CrusherBlock> CODEC = simpleCodec(CrusherBlock::new);
    
    public CrusherBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NonNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void openContainer(Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof CrusherBlockEntity crusher) {
            player.openMenu(crusher);
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> type) {
        return createMachineTicker(level, type, ModBlockEntities.CRUSHER_BLOCK_ENTITY);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
        return new CrusherBlockEntity(worldPosition, blockState);
    }
}
