package com.yeetdot.oreoh.energy;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.api.ICapacitorNode;
import com.yeetdot.oreoh.api.IElectricNode;
import com.yeetdot.oreoh.api.IGeneratorNode;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class ElectricGrid {
    private static final Set<ElectricGrid> ACTIVE_GRIDS = ConcurrentHashMap.newKeySet();
    private static final AtomicInteger TOTAL_INSTANCES_IN_RAM = new AtomicInteger(0);
    
    private final Set<IElectricNode> connectedConsumers = ConcurrentHashMap.newKeySet();
    private final Set<IGeneratorNode> connectedGenerators = ConcurrentHashMap.newKeySet();
    private final Set<ICapacitorNode> connectedCapacitors = ConcurrentHashMap.newKeySet();
    private double currentPowerFactor = 1.0;
    private double totalApparentPowerLoad = 0.0;
    private double voltage = 0.0;
    private int connectedWireCount = 0;

    public ElectricGrid() {
        ACTIVE_GRIDS.add(this);
        TOTAL_INSTANCES_IN_RAM.incrementAndGet();
    }
    
    public void dispose() {
        TOTAL_INSTANCES_IN_RAM.decrementAndGet();
        ACTIVE_GRIDS.remove(this);
        this.clearNetwork();
    }
    
    public static int getTotalInstancesInRAM() {
        return TOTAL_INSTANCES_IN_RAM.get();
    }
    
    public static void tickAllGrids() {
        OreOh.LOGGER.info("Set<ElectricGrid> count: {}, Real count: {}",  ACTIVE_GRIDS.size(), getTotalInstancesInRAM());
        int i = 0;
        for (ElectricGrid grid : ACTIVE_GRIDS) {
            grid.tick();
            i++;
//            OreOh.LOGGER.info("Grid #{} connected wires: {}, connected consumers: {}, connected generators: {}, connected capacitors: {}", i, grid.connectedWireCount, grid.connectedConsumers, grid.connectedGenerators, grid.connectedCapacitors);
        }
    }

    public void incrementWireCount() {
        this.connectedWireCount++;
    }

    public void decrementWireCount() {
        this.connectedWireCount--;
        if (this.connectedWireCount == 0) {
            this.dispose();
        }
    }

    // TODO: find a way to not compute stream every tick
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
        node.updateLinkedGrid(this);
    }
    public void unregisterConsumer(IElectricNode node) { 
        connectedConsumers.remove(node);
        node.updateLinkedGrid(null);
    }
    public void registerGenerator(IGeneratorNode node) { 
        connectedGenerators.add(node);
        node.updateLinkedGrid(this);
    }
    public void unregisterGenerator(IGeneratorNode node) { 
        connectedGenerators.remove(node);
        node.updateLinkedGrid(null);
    }
    public void registerCapacitor(ICapacitorNode node) { 
        connectedCapacitors.add(node);
        node.updateLinkedGrid(this);
    }
    public void unregisterCapacitor(ICapacitorNode node) { 
        connectedCapacitors.remove(node);
        node.updateLinkedGrid(null);
    }
    public void clearNetwork() {
        connectedConsumers.forEach(node -> node.updateLinkedGrid(null));
        connectedGenerators.forEach(node -> node.updateLinkedGrid(null));
        connectedCapacitors.forEach(node -> node.updateLinkedGrid(null));

        connectedConsumers.clear();
        connectedGenerators.clear();
        connectedCapacitors.clear();
    }
    
    public double getCurrentPowerFactor() { return currentPowerFactor; }
    public double getTotalApparentPowerLoad() { return totalApparentPowerLoad; }
    public double getVoltage() { return voltage; }
    
    public double getCurrent() { return voltage <= 0.0 ? 0.0 : totalApparentPowerLoad / voltage; }
}
