package com.yeetdot.oreoh.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ThermalProperties(double insulationFailureTemp, double thermalMass, double coolingRate) {
    public static final Codec<ThermalProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.DOUBLE.fieldOf("insulationFailureTemp").forGetter(ThermalProperties::insulationFailureTemp),
                    Codec.DOUBLE.fieldOf("thermalMass").forGetter(ThermalProperties::thermalMass),
                    Codec.DOUBLE.fieldOf("coolingRate").forGetter(ThermalProperties::coolingRate)
            ).apply(instance, ThermalProperties::new)
    );
}
