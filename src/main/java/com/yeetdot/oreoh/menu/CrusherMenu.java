package com.yeetdot.oreoh.menu;

import com.yeetdot.oreoh.block.entity.CrusherBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class CrusherMenu extends AbstractEnergyContainerMenu {
    private final Container container;
    private final ContainerData crusherData;
    private long clientEnergyAmount = 0;
    private long clientEnergyCapacity = 0;
    
    public CrusherMenu(int containerId, Inventory playerInventory, BlockPos blockPos) {
        this(containerId, playerInventory, new SimpleContainer(3), new SimpleContainerData(6), blockPos);
    }
    
    public CrusherMenu(int containerId, Inventory playerInventory, Container container, ContainerData crusherData, BlockPos pos) {
        super(ModMenuTypes.CRUSHER_MENU, containerId, container, pos);
        checkContainerSize(container, 3);
        checkContainerDataCount(crusherData, 3);
        this.container = container;
        this.crusherData = crusherData;
        container.startOpen(playerInventory.player);
        this.addSlot(new Slot(container, 0, 56, 35));
        this.addSlot(new MachineResultSlot(container, 1, 116, 21));
        this.addSlot(new MachineResultSlot(container, 2, 116, 53));
        this.addDataSlots(crusherData);
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    public void setEnergyValues(long energyAmount, long energyCapacity) {
        this.clientEnergyAmount = energyAmount;
        this.clientEnergyCapacity = energyCapacity;
    }

    @Override
    public long getClientEnergyAmount() { return this.clientEnergyAmount; }
    
    @Override
    public long getClientEnergyCapacity() { return this.clientEnergyCapacity; }

    @Override
    public void sendAllDataToRemote() {
        super.sendAllDataToRemote();
        
        if (this.container instanceof CrusherBlockEntity crusherBE) {
            crusherBE.setLastSyncedEnergyToInvalid();
        }
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack sourceStack = slot.getItem();
            itemStack = sourceStack.copy();

            if (index < 3) {
                if (!this.moveItemStackTo(sourceStack, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
                if (index == 1 || index == 2) {
                    slot.onQuickCraft(sourceStack, itemStack);
                }
            } else {
                if (!this.moveItemStackTo(sourceStack, 0, 1, false)) {
                    itemStack = createInventoryQuickMove(3, player, slot, itemStack, index);
                }
            }
        }
        return itemStack;
    }

    public int getProgress() {
        return crusherData.get(0);
    }

    public int getMaxProgress() {
        return crusherData.get(1);
    }

    public int getScaledProgress(int arrowPixelWidth) {
        int progress = getProgress();
        int maxProgress = getMaxProgress();
        if (maxProgress == 0 || progress == 0) return 0;
        return (progress * arrowPixelWidth) / maxProgress;
    }
}
