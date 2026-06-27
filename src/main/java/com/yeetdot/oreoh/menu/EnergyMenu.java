package com.yeetdot.oreoh.menu;

import net.minecraft.core.BlockPos;

public interface EnergyMenu {
    void setEnergyValues(long energyAmount, long energyCapacity);
    long getClientEnergyAmount();
    long getClientEnergyCapacity();
    default int getScaledEnergy(int capacityPixelHeight) {
        long energyAmount = getClientEnergyAmount();
        long energyCapacity = getClientEnergyCapacity();

        if (energyCapacity <= 0) return 0;
        double ratio = (double) energyAmount / (double) energyCapacity;
        return (int) (ratio * capacityPixelHeight);
    }
    BlockPos getBlockPos();
}
