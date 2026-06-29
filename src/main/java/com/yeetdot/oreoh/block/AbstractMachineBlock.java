package com.yeetdot.oreoh.block;

import com.mojang.serialization.MapCodec;
import com.yeetdot.oreoh.block.entity.AbstractMachineBlockEntity;
import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.recipe.MachineRecipe;
import com.yeetdot.oreoh.recipe.MachineRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import org.jspecify.annotations.Nullable;

public abstract class AbstractMachineBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public AbstractMachineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected abstract  MapCodec<? extends BaseEntityBlock> codec();

    protected abstract void openContainer(Level level, BlockPos pos, Player player);

    @Override
    protected  InteractionResult useWithoutItem(BlockState state, Level level,  BlockPos pos,  Player player,  BlockHitResult hitResult) {
        if (!level.isClientSide() && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            this.openContainer(level, pos, player);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected  InteractionResult useItemOn(ItemStack itemStack,  BlockState state,  Level level,  BlockPos pos,  Player player,  InteractionHand hand,  BlockHitResult hitResult) {
        if (itemStack.is(ModItems.WRENCH)) {
            return InteractionResult.PASS;
        } else if (!level.isClientSide()) {
            this.openContainer(level, pos, player);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void affectNeighborsAfterRemoval(final  BlockState state, final  ServerLevel level, final  BlockPos pos, final boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    @Override
    protected  BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    protected  BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING, ACTIVE);
    }

    @SuppressWarnings("SameParameterValue")
    protected static @Nullable <T extends BlockEntity> BlockEntityTicker<T> createMachineTicker(
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
