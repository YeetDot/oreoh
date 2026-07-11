package com.yeetdot.oreoh.energy;

import com.yeetdot.oreoh.api.ICapacitorNode;
import com.yeetdot.oreoh.api.IElectricNode;
import com.yeetdot.oreoh.api.IGeneratorNode;
import net.minecraft.core.BlockPos;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ElectricGrid {
    private final Set<BlockPos> wirePositions;
    private final Set<BlockPos> scannedWirePositions = ConcurrentHashMap.newKeySet();
    private final Set<IElectricNode> connectedConsumers = ConcurrentHashMap.newKeySet();
    private final Set<IGeneratorNode> connectedGenerators = ConcurrentHashMap.newKeySet();
    private final Set<ICapacitorNode> connectedCapacitors = ConcurrentHashMap.newKeySet();
    private double currentPowerFactor = 1.0;
    private double totalApparentPowerLoad = 0.0;
    private double voltage = 0.0;

    public ElectricGrid(Set<BlockPos> wirePositions) {
        this.wirePositions = wirePositions;
    }

    public boolean hasScannedWire(BlockPos pos) {
        return this.scannedWirePositions.contains(pos);
    }

    public void markWireAsScanned(BlockPos pos) {
        this.scannedWirePositions.add(pos);
    }
    
    public void dispose() {
        this.clearNetwork();
    }

    public void tick() {
        double totalRealPower = 0;
        double rawReactivePower = 0;

        for (IElectricNode node : connectedConsumers) {
            if (node.isMachineActive()) {
                double watts = node.getRequestedWatts();
                totalRealPower += watts;
                rawReactivePower += watts * node.getBaseInductance();
            }
        }
        
        double totalCapacitance = 0;
        for (ICapacitorNode capacitor : connectedCapacitors) {
            totalCapacitance += capacitor.getCapacitanceRating();
        }
        
        double netReactivePower = Math.max(0.0, rawReactivePower - totalCapacitance);
        if (totalRealPower >= Double.MAX_VALUE || netReactivePower >= Double.MAX_VALUE) {
            this.totalApparentPowerLoad = Double.MAX_VALUE;
        } else {
            this.totalApparentPowerLoad = Math.sqrt((totalRealPower * totalRealPower) + (netReactivePower * netReactivePower));
        }
        this.currentPowerFactor = (totalApparentPowerLoad > 0) ? (totalRealPower / totalApparentPowerLoad) : 1.0;
        double maxVoltage = 0.0;
        for (IGeneratorNode generator : connectedGenerators) {
            maxVoltage = Math.max(maxVoltage, generator.getVoltage());
        }
        this.voltage = Math.max(maxVoltage, 0.0);
    }

    public void registerConsumer(IElectricNode node) {
        connectedConsumers.add(node);
    }

    public void unregisterConsumer(IElectricNode node) { 
        connectedConsumers.remove(node);
    }

    public void registerGenerator(IGeneratorNode node) { 
        connectedGenerators.add(node);
    }

    public void unregisterGenerator(IGeneratorNode node) { 
        connectedGenerators.remove(node);
    }

    public void registerCapacitor(ICapacitorNode node) { 
        connectedCapacitors.add(node);
    }

    public void unregisterCapacitor(ICapacitorNode node) { 
        connectedCapacitors.remove(node);
    }

    public void clearNetwork() {
        connectedConsumers.clear();
        connectedGenerators.clear();
        connectedCapacitors.clear();
        scannedWirePositions.clear();
    }

    public Set<BlockPos> getWirePositions() {
        return Collections.unmodifiableSet(wirePositions);
    }

    public double getCurrentPowerFactor() { return currentPowerFactor; }
    public double getTotalApparentPowerLoad() { return totalApparentPowerLoad; }
    public double getVoltage() { return voltage; }
    
    public double getCurrent() { return voltage <= 0.0 ? 0.0 : totalApparentPowerLoad / voltage; }
}
