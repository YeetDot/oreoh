package com.yeetdot.oreoh.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record MachineRecipeInput(List<ItemStack> inputs) implements RecipeInput {
    @Override
    public @NonNull ItemStack getItem(int index) {
        return inputs.get(index);
    }

    @Override
    public int size() {
        return inputs.size();
    }
}
