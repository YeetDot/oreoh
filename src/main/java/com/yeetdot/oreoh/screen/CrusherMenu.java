package com.yeetdot.oreoh.screen;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class CrusherMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData crusherData;
    
    public CrusherMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(3), new SimpleContainerData(6));
    }
    
    public CrusherMenu(int containerId, Inventory playerInventory, Container container, ContainerData crusherData) {
        super(ModMenuTypes.CRUSHER_MENU, containerId);
        checkContainerSize(container, 3);
        checkContainerDataCount(crusherData, 6);
        this.container = container;
        this.crusherData = crusherData;
        container.startOpen(playerInventory.player);
        this.addSlot(new Slot(container, 0, 56, 35));
        this.addSlot(new MachineResultSlot(container, 1, 116, 21));
        this.addSlot(new MachineResultSlot(container, 2, 116, 53));
        this.addDataSlots(crusherData);
        this.addStandardInventorySlots(container, 8, 84);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack sourceStack = slot.getItem();
            itemStack = sourceStack.copy();

            // 1. Clicked inside the MACHINE (Slots 0, 1, or 2)
            if (index < 3) {
                // Try to move the item out into the player's entire inventory (Main + Hotbar)
                if (!this.moveItemStackTo(sourceStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                // Triggers recipe/advancement rewards if pulled from output 1 or output 2
                if (index == 1 || index == 2) {
                    slot.onQuickCraft(sourceStack, itemStack);
                }
            }
            // 2. Clicked inside the PLAYER INVENTORY (Slots 3 to 38)
            else {
                // Try to move it strictly into the Input Slot (Slot 0 only! Notice the 0, 1 bounds)
                if (!this.moveItemStackTo(sourceStack, 0, 1, false)) {

                    // If the input slot is full, transfer the item between Hotbar and Main Inventory instead
                    if (index < 30) { // Clicked in Main Inventory -> Move to Hotbar
                        if (!this.moveItemStackTo(sourceStack, 30, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index < 39) { // Clicked in Hotbar -> Move to Main Inventory
                        if (!this.moveItemStackTo(sourceStack, 3, 30, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            // Standard vanilla cleanup and syncing
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

        return itemStack;
    }


    @Override
    public boolean stillValid(@NonNull Player player) {
        return this.container.stillValid(player);
    }

    public int getProgress() {
        return crusherData.get(0);
    }

    public int getMaxProgress() {
        return crusherData.get(1);
    }
    
    public long getEnergyAmount() {
        return ((long) crusherData.get(2) << 32 | (crusherData.get(3) & 0xFFFFFFFFL));
    }

    public long getEnergyCapacity() {
        return ((long) crusherData.get(4) << 32 | (crusherData.get(5) & 0xFFFFFFFFL));
    }

    public int getScaledProgress(int arrowPixelWidth) {
        int progress = getProgress();
        int maxProgress = getMaxProgress();
        if (maxProgress == 0 || progress == 0) return 0;
        return (progress * arrowPixelWidth) / maxProgress;
    }
    
    public int getScaledEnergy(int capacityPixelHeight) {
        long amount = getEnergyAmount();
        long capacity = getEnergyCapacity();
        if (amount == 0 || capacity == 0) return 0;
        return (int) (amount * capacityPixelHeight / capacity);
    }

}
