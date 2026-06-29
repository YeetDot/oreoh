package com.yeetdot.oreoh.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yeetdot.oreoh.block.entity.BatteryBlockEntity;
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
import org.jspecify.annotations.Nullable;

public class BatteryBlock extends BaseEntityBlock {
    public static final MapCodec<BatteryBlock> CODEC = RecordCodecBuilder.mapCodec(
        instance -> 
            instance.group(
                    propertiesCodec(),
                    EnergyStorageStats.CODEC.fieldOf("battery_stats").forGetter(block -> block.stats)
            ).apply(instance, BatteryBlock::new)
    );
    public final EnergyStorageStats stats;
    
    public BatteryBlock(Properties properties, EnergyStorageStats stats) {
        super(properties);
        this.stats = stats;
    }

    @Override
    protected  MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new BatteryBlockEntity(worldPosition, blockState, this.stats);
    }
    
    public void openContainer(Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof BatteryBlockEntity batteryBlock) {
            player.openMenu(batteryBlock);
        }
    }

    @Override
    protected  InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide() && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            this.openContainer(level, pos, player);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected  InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (itemStack.is(ModItems.WRENCH)) {
            return InteractionResult.PASS;
        } else if (!level.isClientSide()) {
            this.openContainer(level, pos, player);
        }
        
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level,  BlockState blockState,  BlockEntityType<T> type) {
        return level instanceof ServerLevel serverLevel 
                ? createTickerHelper(type, ModBlockEntities.BATTERY_BLOCK_ENTITY, (_, blockPos, _, entity) -> BatteryBlockEntity.serverTick(serverLevel, blockPos, entity)) 
                : null;
    }

}
