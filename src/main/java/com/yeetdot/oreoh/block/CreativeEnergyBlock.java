package com.yeetdot.oreoh.block;

import com.mojang.serialization.MapCodec;
import com.yeetdot.oreoh.block.entity.CreativeEnergyBlockEntity;
import com.yeetdot.oreoh.block.entity.ModBlockEntities;
import com.yeetdot.oreoh.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CreativeEnergyBlock extends BaseEntityBlock {
    public static final MapCodec<CreativeEnergyBlock> CODEC = simpleCodec(CreativeEnergyBlock::new);
    
    public CreativeEnergyBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NonNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos worldPosition, @NonNull BlockState blockState) {
        return new CreativeEnergyBlockEntity(worldPosition, blockState);
    }
    
    public void openContainer(Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof CreativeEnergyBlockEntity energyBlock) {
            player.openMenu(energyBlock);
        }
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        if (!level.isClientSide() && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            this.openContainer(level, pos, player);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected @NonNull InteractionResult useItemOn(ItemStack itemStack, @NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull InteractionHand hand, @NonNull BlockHitResult hitResult) {
        if (itemStack.is(ModItems.WRENCH)) {
            return InteractionResult.PASS;
        } else if (!level.isClientSide()) {
            this.openContainer(level, pos, player);
        }
        
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel 
                ? createTickerHelper(type, ModBlockEntities.CREATIVE_ENERGY_BLOCK_ENTITY, (_, blockPos, _, entity) -> CreativeEnergyBlockEntity.serverTick(serverLevel, blockPos, entity)) 
                : null;
    }
}
