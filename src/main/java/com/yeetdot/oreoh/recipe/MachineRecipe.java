package com.yeetdot.oreoh.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public interface MachineRecipe<Input extends RecipeInput> extends Recipe<Input> {
    default List<ItemStack> assembleAll() {
        return outputs().stream().map(OutputWithChance::stack).toList();
    }
    List<OutputWithChance> outputs();
    
    long getRecipeEnergy(Input input);
    int getRecipeDuration(Input input);
}
