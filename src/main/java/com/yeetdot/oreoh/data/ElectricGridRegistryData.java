package com.yeetdot.oreoh.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.energy.ElectricGrid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;

public class ElectricGridRegistryData extends SavedData {
    private record WireMapping(BlockPos pos, UUID gridId) {
        public static final Codec<WireMapping> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        BlockPos.CODEC.fieldOf("pos").forGetter(WireMapping::pos),
                        UUIDUtil.CODEC.fieldOf("grid_id").forGetter(WireMapping::gridId)
                ).apply(instance, WireMapping::new)
        );
    }

    public static final Codec<ElectricGridRegistryData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    WireMapping.CODEC.listOf().fieldOf("wire_mappings").forGetter(data -> {
                        List<WireMapping> mappings = new ArrayList<>();
                        data.registry.getBlockPositions().forEach((pos, uuid) -> mappings.add(new WireMapping(pos, uuid)));
                        return mappings;
                    })
            ).apply(instance, mappings -> {
                ElectricGridRegistryData data = new ElectricGridRegistryData();

                Map<UUID, Set<BlockPos>> gridPositionsMap = new HashMap<>();
                for (WireMapping mapping : mappings) {
                    gridPositionsMap.computeIfAbsent(mapping.gridId(), id -> new HashSet<>())
                            .add(mapping.pos());
                }
                for (WireMapping mapping : mappings) {
                    UUID networkId = mapping.gridId();
                    Set<BlockPos> positions = gridPositionsMap.get(networkId);

                    ElectricGrid grid = new ElectricGrid(positions);

                    data.registry.loadStoredMapping(mapping.pos(), networkId, grid);
                }

                return data;
            })
    );

    public static final SavedDataType<ElectricGridRegistryData> TYPE = new SavedDataType<>(
            OreOh.id("electric_networks"),
            ElectricGridRegistryData::new,
            CODEC,
            DataFixTypes.LEVEL
    );

    private final ElectricGridRegistry registry;

    public ElectricGridRegistryData() {
        this.registry = new ElectricGridRegistry();
    }

    public ElectricGridRegistry getRegistry() {
        return this.registry;
    }

    public void markNetworkChanged() {
        this.setDirty(true);
    }

    public static ElectricGridRegistry getRegistryFor(ServerLevel level) {
        ElectricGridRegistryData data = level.getDataStorage().computeIfAbsent(TYPE);
        return data.getRegistry();
    }
}
