package com.yeetdot.oreoh.menu;

import com.yeetdot.oreoh.block.entity.AbstractEnergyContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class AbstractEnergyContainerMenu extends AbstractContainerMenu implements EnergyMenu {
    Container container;
    private long clientEnergyAmount = 0;
    private long clientEnergyCapacity = 0;
    private final BlockPos blockPos;
    
    protected AbstractEnergyContainerMenu(@Nullable MenuType<?> menuType, int containerId, Container container, BlockPos blockPos) {
        super(menuType, containerId);
        this.blockPos = blockPos;
        this.container = container;
    }

    @Override
    public void setEnergyValues(long energyAmount, long energyCapacity) {
        this.clientEnergyAmount = energyAmount;
        this.clientEnergyCapacity = energyCapacity;
    }

    @Override
    public long getClientEnergyAmount() {
        return this.clientEnergyAmount;
    }

    @Override
    public long getClientEnergyCapacity() {
        return this.clientEnergyCapacity;
    }

    @Override
    public BlockPos getBlockPos() {
        return blockPos;
    }
    
    public ItemStack createInventoryQuickMove(int slotOffset, final Player player, final Slot slot, final ItemStack itemStack, final int slotIndex) {
        ItemStack itemStack1 = itemStack.copy();
        if (slot.hasItem()) {
            ItemStack sourceStack = slot.getItem();
            itemStack1 = sourceStack.copy();
            if (slotIndex < 27 + slotOffset) {
                if (!this.moveItemStackTo(sourceStack, 27 + slotOffset, 36 + slotOffset, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex < 36 + slotOffset) {
                if (!this.moveItemStackTo(sourceStack, slotOffset, 27 + slotOffset, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (sourceStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (sourceStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, sourceStack);
        }

        return itemStack1;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return this.container.stillValid(player);
    }
    
    protected AbstractEnergyContainerBlockEntity getEnergyContainingBlockEntity() {
        return container instanceof AbstractEnergyContainerBlockEntity entity ? entity : null;
    }

    @Override
    public void sendAllDataToRemote() {
        super.sendAllDataToRemote();

        if (getEnergyContainingBlockEntity() != null) {
            getEnergyContainingBlockEntity().setLastSyncedEnergyToInvalid();
        }
    }
}
