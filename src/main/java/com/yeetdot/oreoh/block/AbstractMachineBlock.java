package com.yeetdot.oreoh.block;

import com.mojang.serialization.MapCodec;
import com.yeetdot.oreoh.block.entity.AbstractMachineBlockEntity;
import com.yeetdot.oreoh.recipe.MachineRecipeInput;
import com.yeetdot.oreoh.recipe.MachineRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;

public abstract class AbstractMachineBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public AbstractMachineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected abstract @NonNull MapCodec<? extends BaseEntityBlock> codec();

    protected abstract void openContainer(Level level, BlockPos pos, Player player);

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            this.openContainer(level, pos, player);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void affectNeighborsAfterRemoval(final @NonNull BlockState state, final @NonNull ServerLevel level, final @NonNull BlockPos pos, final boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    @Override
    protected @NonNull BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    protected @NonNull BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING, ACTIVE);
    }

    protected static <T extends BlockEntity> BlockEntityTicker<T> createMachineTicker(
            Level level,
            BlockEntityType<T> actualType,
            BlockEntityType<? extends AbstractMachineBlockEntity<? extends MachineRecipeInput, ? extends MachineRecipe<? extends MachineRecipeInput>>> expectedType
    ) {
        return level instanceof ServerLevel serverLevel
                ? createTickerHelper(actualType, expectedType, (_, _, _, entity) -> {
            if (entity instanceof AbstractMachineBlockEntity<?, ?> machineEntity) {
                AbstractMachineBlockEntity.serverTick(serverLevel, machineEntity);
            }
        })
                : null;
    }

}
