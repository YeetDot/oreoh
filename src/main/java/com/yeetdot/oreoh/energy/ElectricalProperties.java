package com.yeetdot.oreoh.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ElectricalProperties(double resistance, double maxApparentPower) {
    public static final Codec<ElectricalProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.DOUBLE.fieldOf("resistance").forGetter(ElectricalProperties::resistance),
                    Codec.DOUBLE.fieldOf("maxApparentPower").forGetter(ElectricalProperties::maxApparentPower)
            ).apply(instance, ElectricalProperties::new)
    );
}
