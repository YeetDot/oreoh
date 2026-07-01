package com.yeetdot.oreoh.energy;

import com.yeetdot.oreoh.api.ICapacitorNode;
import com.yeetdot.oreoh.api.IElectricNode;
import com.yeetdot.oreoh.api.IGeneratorNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public final class WireNetworkHelper {
    public static void onNeighborUpdate(Level level, BlockPos pos, WireBlockEntity wire) {
        ElectricGrid grid = wire.getGrid();
        if (grid == null) return;
        for (Direction direction : Direction.values()) {
            BlockPos nextPos = pos.relative(direction);
            BlockEntity nextEntity = level.getBlockEntity(nextPos);
            if (nextEntity instanceof IElectricNode consumer) {
                grid.registerConsumer(consumer);
                consumer.updateLinkedGrid(grid);
            }
            if (nextEntity instanceof IGeneratorNode generator) {
                grid.registerGenerator(generator);
                generator.updateLinkedGrid(grid);
            }
            if (nextEntity instanceof ICapacitorNode capacitor) {
                grid.registerCapacitor(capacitor);
                capacitor.updateLinkedGrid(grid);
            }
        }
    }
    
    public static void onWirePlaced(Level level, BlockPos placedPos, WireBlockEntity placedWire) {
//        for (Direction direction : Direction.values()) {
//            BlockPos neighborPos = placedPos.relative(direction);
//            BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
//
//            if (neighborEntity instanceof WireBlockEntity neighborWire) {
//                ElectricGrid oldNeighborGrid = neighborWire.getGrid();
//                if (oldNeighborGrid != null) {
//                    oldNeighborGrid.clearNetwork();
//                    ElectricGrid.removeGridFromTicker(oldNeighborGrid);
//                }
//            }
//        }

        Set<BlockPos> globalRescuedSet = new HashSet<>();
        rebuildNetworkFrom(level, placedPos, globalRescuedSet);
    }
    
    public static void onWireRemoved(Level level, BlockPos pos, WireBlockEntity removedWire) {
        if (removedWire.getGrid() != null) {
            removedWire.getGrid().clearNetwork();
            removedWire.getGrid().decrementWireCount();
        }
        Set<BlockPos> globalRescuedSet = new HashSet<>();
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
            
            if (neighborEntity instanceof WireBlockEntity neighborWire) {
                if (globalRescuedSet.contains(neighborPos)) continue;
                
                if (neighborWire.getGrid() != null) {
                    rebuildNetworkFrom(level, neighborPos, globalRescuedSet);
                }
            }
        }
    }

    private static void rebuildNetworkFrom(Level level, BlockPos startPos, Set<BlockPos> globalRescuedSet) {
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        
        ElectricGrid newGrid = new ElectricGrid();
        
        queue.add(startPos);
        visited.add(startPos);
        globalRescuedSet.add(startPos);
        
        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();
            BlockEntity currentEntity = level.getBlockEntity(currentPos);
            
            if (currentEntity instanceof WireBlockEntity wire) {
                wire.setGrid(newGrid);
                
                for (Direction dir : Direction.values()) {
                    BlockPos nextPos = currentPos.relative(dir);
                    if (visited.contains(nextPos)) continue;
                    BlockEntity nextEntity = level.getBlockEntity(nextPos);
                    if (nextEntity instanceof WireBlockEntity) {
                        visited.add(nextPos);
                        globalRescuedSet.add(nextPos);
                        queue.add(nextPos);
                    } 
                    if (nextEntity instanceof IElectricNode consumer) {
                        newGrid.registerConsumer(consumer);
                        consumer.updateLinkedGrid(newGrid);
                        visited.add(nextPos);
                    } 
                    if (nextEntity instanceof IGeneratorNode generator) {
                        newGrid.registerGenerator(generator);
                        generator.updateLinkedGrid(newGrid);
                        visited.add(nextPos);
                    } 
                    if (nextEntity instanceof ICapacitorNode capacitor) {
                        newGrid.registerCapacitor(capacitor);
                        capacitor.updateLinkedGrid(newGrid);
                        visited.add(nextPos);
                    }
                }
            }
        }
    }
}
