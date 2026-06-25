package com.yeetdot.oreoh.screen;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class MachineResultSlot extends Slot {
    public MachineResultSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(@NonNull ItemStack itemStack) {
        return false;
    }
}
