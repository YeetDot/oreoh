package com.yeetdot.oreoh.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public record CountedIngredient(Ingredient ingredient, int count) {
    public static final Codec<CountedIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(CountedIngredient::ingredient),
            Codec.INT.fieldOf("count").forGetter(CountedIngredient::count)).apply(instance, CountedIngredient::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CountedIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, CountedIngredient::ingredient,
            ByteBufCodecs.INT, CountedIngredient::count,
            CountedIngredient::new
    );

    public static CountedIngredient ofItemAndCount(ItemLike item, int count) {
        return new CountedIngredient(Ingredient.of(item), count);
    }
}
