package com.yeetdot.oreoh.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record EnergyStorageStats(long capacity, long insertPerTick, long extractPerTick) {
    public static final Codec<EnergyStorageStats> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.LONG.fieldOf("capacity").forGetter(EnergyStorageStats::capacity),
                    Codec.LONG.fieldOf("insert_per_tick").forGetter(EnergyStorageStats::insertPerTick),
                    Codec.LONG.fieldOf("extract_per_tick").forGetter(EnergyStorageStats::extractPerTick)
            ).apply(instance, EnergyStorageStats::new)
    );
}
