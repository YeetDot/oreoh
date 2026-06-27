package com.yeetdot.oreoh.block.entity;

import com.yeetdot.oreoh.menu.CreativeEnergyMenu;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

public class CreativeEnergyBlockEntity extends AbstractEnergyContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    
    public CreativeEnergyBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.CREATIVE_ENERGY_BLOCK_ENTITY, worldPosition, blockState, Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE);
    }
    
    public static void serverTick(ServerLevel level, BlockPos pos, CreativeEnergyBlockEntity entity) {
        entity.syncEnergyToOpenMenus(level);
        for (Direction direction : Direction.values()) {
            BlockPos blockPos = pos.relative(direction);
            EnergyStorage maybeStorage = EnergyStorage.SIDED.find(level, blockPos, direction);
            if (maybeStorage != null) {
                EnergyStorageUtil.move(entity.energyStorage, maybeStorage, Long.MAX_VALUE, null);
            }
        }
        try (Transaction transaction = Transaction.openOuter()) {
            entity.energyStorage.insert(Long.MAX_VALUE/100, transaction);
            transaction.commit();
        }
    }

    @Override
    protected @NonNull Component getDefaultName() {
        return Component.translatable("container.oreoh.creative_energy_block_entity");
    }

    @Override
    protected @NonNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(@NonNull NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected @NonNull AbstractContainerMenu createMenu(int containerId, @NonNull Inventory inventory) {
        return new CreativeEnergyMenu(containerId, inventory, this, this.getBlockPos());
    }

    @Override
    public int getContainerSize() {
        return 2;
    }
}
