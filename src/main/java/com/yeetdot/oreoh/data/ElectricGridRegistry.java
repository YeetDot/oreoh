package com.yeetdot.oreoh.data;

import com.yeetdot.oreoh.api.ICapacitorNode;
import com.yeetdot.oreoh.api.IElectricNode;
import com.yeetdot.oreoh.api.IGeneratorNode;
import com.yeetdot.oreoh.energy.ElectricGrid;
import com.yeetdot.oreoh.energy.WireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class ElectricGridRegistry {
    private final Map<UUID, ElectricGrid> electricGrids = new HashMap<>();
    private final Map<BlockPos, UUID> blockPositions = new HashMap<>();

    ElectricGridRegistry() {}

    @Nullable
    public UUID getNetworkIdAt(BlockPos pos) {
        return this.blockPositions.get(pos);
    }

    public void tickAllGrids(ServerLevel level) {
        this.reconstructLoadedMachines(level);

        for (ElectricGrid grid : this.electricGrids.values()) {
            if (grid != null) {
                grid.tick();
            }
        }
    }

    private void reconstructLoadedMachines(ServerLevel level) {
        for (Map.Entry<BlockPos, UUID> entry : this.blockPositions.entrySet()) {
            BlockPos wirePos = entry.getKey();
            UUID gridId = entry.getValue();
            ElectricGrid grid = this.electricGrids.get(gridId);

            if (grid != null) {
                if (grid.hasScannedWire(wirePos)) {
                    continue;
                }

                if (level.isLoaded(wirePos)) {
                    for (Direction dir : Direction.values()) {
                        BlockPos neighborPos = wirePos.relative(dir);

                        if (level.isLoaded(neighborPos)) {
                            BlockEntity be = level.getBlockEntity(neighborPos);
                            if (be != null) {
                                if (be instanceof IElectricNode consumer) grid.registerConsumer(consumer);
                                if (be instanceof IGeneratorNode generator) grid.registerGenerator(generator);
                                if (be instanceof ICapacitorNode capacitor) grid.registerCapacitor(capacitor);
                            }
                        }
                    }
                    grid.markWireAsScanned(wirePos);
                }
            }
        }
    }

    @Nullable
    public ElectricGrid getGrid(@Nullable UUID gridId) {
        return gridId == null ? null : this.electricGrids.get(gridId);
    }

    @Nullable
    public ElectricGrid getGrid(BlockPos pos) {
        UUID gridId = this.getNetworkIdAt(pos);
        return getGrid(gridId);
    }

    public Map<BlockPos, UUID> getBlockPositions() {
        return Collections.unmodifiableMap(this.blockPositions);
    }

    void loadStoredMapping(BlockPos pos, UUID networkId, ElectricGrid grid) {
        this.blockPositions.put(pos, networkId);
        this.electricGrids.put(networkId, grid);
    }

    public static ElectricGridRegistry get(ServerLevel level) {
        return ElectricGridRegistryData.getRegistryFor(level);
    }

    public static void applyToConnectedGrid(Level level, BlockPos pos, Consumer<ElectricGrid> consumer) {
        if (level instanceof ServerLevel serverLevel) {
            Set<UUID> connectedGrids = new HashSet<>();
            ElectricGridRegistry registry = ElectricGridRegistry.get(serverLevel);
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.relative(direction);
                UUID uuid = registry.getNetworkIdAt(neighborPos);
                if (uuid != null) {
                    connectedGrids.add(uuid);
                }
            }
            for (UUID uuid : connectedGrids) {
                ElectricGrid grid = registry.getGrid(uuid);
                if (grid != null) {
                    consumer.accept(grid);
                }
            }
        }
    }

    public void registerPlacement(Level level, BlockPos pos) {
        NetworkScanResult result = floodFillNetwork(level, pos);
        Set<BlockPos> connectedPositions = result.wirePositions();

        for (BlockPos wirePos : connectedPositions) {
            UUID oldId = blockPositions.remove(wirePos);
            if (oldId != null) {
                ElectricGrid oldGrid = electricGrids.remove(oldId);
                if (oldGrid != null) oldGrid.dispose();
            }
        }

        UUID newNetworkId = UUID.randomUUID();
        ElectricGrid newGrid = new ElectricGrid(connectedPositions);

        result.consumers().forEach(newGrid::registerConsumer);
        result.generators().forEach(newGrid::registerGenerator);
        result.capacitors().forEach(newGrid::registerCapacitor);

        electricGrids.put(newNetworkId, newGrid);

        for (BlockPos wirePos : connectedPositions) {
            blockPositions.put(wirePos, newNetworkId);
        }

        this.notifyDataChanged(level);
    }

    public void registerRemoval(Level level, BlockPos pos) {
        UUID brokenNetworkId = this.blockPositions.remove(pos);
        if (brokenNetworkId == null) return;

        ElectricGrid oldGrid = this.electricGrids.remove(brokenNetworkId);
        if (oldGrid != null) oldGrid.dispose();

        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);

            if (!level.isLoaded(neighborPos)) continue;

            if (this.blockPositions.containsKey(neighborPos) && !this.blockPositions.get(neighborPos).equals(brokenNetworkId)) {
                continue;
            }

            if (level.getBlockState(neighborPos).getBlock() instanceof WireBlock) {
                NetworkScanResult result = floodFillNetwork(level, neighborPos);

                UUID newNetworkId = UUID.randomUUID();
                ElectricGrid newGrid = new ElectricGrid(result.wirePositions());

                result.consumers().forEach(newGrid::registerConsumer);
                result.generators().forEach(newGrid::registerGenerator);
                result.capacitors().forEach(newGrid::registerCapacitor);

                this.electricGrids.put(newNetworkId, newGrid);

                for (BlockPos splitPos : result.wirePositions()) {
                    this.blockPositions.put(splitPos, newNetworkId);
                }
            }
        }

        this.notifyDataChanged(level);
    }

    public record NetworkScanResult(
            Set<BlockPos> wirePositions,
            Set<IElectricNode> consumers,
            Set<IGeneratorNode> generators,
            Set<ICapacitorNode> capacitors
    ) {}

    private NetworkScanResult floodFillNetwork(Level level, BlockPos startPos) {
        Set<BlockPos> wires = new HashSet<>();
        Set<IElectricNode> consumers = new HashSet<>();
        Set<IGeneratorNode> generators = new HashSet<>();
        Set<ICapacitorNode> capacitors = new HashSet<>();

        Queue<BlockPos> queue = new LinkedList<>();

        queue.add(startPos);
        wires.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();

            for (Direction dir : Direction.values()) {
                BlockPos next = current.relative(dir);

                if (wires.contains(next)) continue;

                if (!level.isLoaded(next)) {
                    if (this.blockPositions.containsKey(next)) {
                        wires.add(next);
                    }
                    continue;
                }

                if (level.getBlockState(next).getBlock() instanceof WireBlock) {
                    wires.add(next);
                    queue.add(next);
                }
                else if (level.getBlockEntity(next) instanceof BlockEntity be) {
                    if (be instanceof IElectricNode consumer) {
                        consumers.add(consumer);
                    }
                    if (be instanceof IGeneratorNode generator) {
                        generators.add(generator);
                    }
                    if (be instanceof ICapacitorNode capacitor) {
                        capacitors.add(capacitor);
                    }
                }
            }
        }

        return new NetworkScanResult(wires, consumers, generators, capacitors);
    }

    private void notifyDataChanged(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            ElectricGridRegistryData data = serverLevel.getDataStorage().get(ElectricGridRegistryData.TYPE);

            if (data != null) {
                data.markNetworkChanged();
            }
        }
    }
}
