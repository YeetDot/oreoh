package com.yeetdot.oreoh.block.entity;

import com.yeetdot.oreoh.block.EnergyStorageStats;
import com.yeetdot.oreoh.menu.BatteryMenu;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

public class BatteryBlockEntity extends AbstractEnergyContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    
    public BatteryBlockEntity(BlockPos worldPosition, BlockState blockState, EnergyStorageStats stats) {
        this(ModBlockEntities.BATTERY_BLOCK_ENTITY, worldPosition, blockState, stats);
    }
    
    public BatteryBlockEntity(BlockEntityType<? extends BatteryBlockEntity> blockEntityType, BlockPos worldPosition, BlockState blockState, EnergyStorageStats stats) {
        super(blockEntityType, worldPosition, blockState, stats.capacity(), stats.insertPerTick(), stats.extractPerTick());
    }
    
    public static void serverTick(ServerLevel level, BlockPos pos, BatteryBlockEntity entity) {
        entity.syncEnergyToOpenMenus(level);
        for (Direction direction : Direction.values()) {
            if (!entity.canExtractEnergy(direction)) continue;
            BlockPos blockPos = pos.relative(direction);
            EnergyStorage maybeStorage = EnergyStorage.SIDED.find(level, blockPos, direction.getOpposite());
            if (maybeStorage == null) continue;
            if (maybeStorage.getAmount() >= entity.energyStorage.getAmount()) continue;
            try (Transaction transaction = Transaction.openOuter()) {
                EnergyStorageUtil.move(entity.getEnergyStorage(), maybeStorage, Long.MAX_VALUE, transaction);
                transaction.commit();
            }
        }
    }

    @Override
    protected  Component getDefaultName() {
        return Component.translatable("container.oreoh.battery_block_entity");
    }

    @Override
    protected  NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected  AbstractContainerMenu createMenu(int containerId,  Inventory inventory) {
        return new BatteryMenu(containerId, inventory, this, this.getBlockPos());
    }

    @Override
    public int getContainerSize() {
        return 2;
    }
}
