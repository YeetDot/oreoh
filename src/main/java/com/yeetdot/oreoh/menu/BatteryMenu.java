package com.yeetdot.oreoh.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BatteryMenu extends AbstractEnergyContainerMenu {
    public BatteryMenu(int containerId, Inventory playerInventory, BlockPos blockPos) {
        this(containerId, playerInventory, new SimpleContainer(2), blockPos);
    }

    public BatteryMenu(int containerId, Inventory playerInventory, Container container, BlockPos pos) {
        super(ModMenuTypes.BATTERY_MENU, containerId, container, pos);
        this.container = container;
        container.startOpen(playerInventory.player);
        checkContainerSize(this.container, 2);
        this.addSlot(new Slot(container, 0, 26, 35));
        this.addSlot(new Slot(container, 1, 134, 35));
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    public  ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack sourceStack = slot.getItem();
            itemStack = sourceStack.copy();

            if (index < 2) {
                if (!this.moveItemStackTo(sourceStack, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(sourceStack, 0, 1, false)) {
                    itemStack = createInventoryQuickMove(2, player, slot, itemStack, index);
                }
            }
        }
        return itemStack;
    }
}
