package com.yeetdot.oreoh.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record OutputWithChance(ItemStack stack, float chance) {
    public static final Codec<OutputWithChance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("stack").forGetter(OutputWithChance::stack),
            Codec.FLOAT.fieldOf("chance").forGetter(OutputWithChance::chance)
    ).apply(instance, OutputWithChance::new));
}
