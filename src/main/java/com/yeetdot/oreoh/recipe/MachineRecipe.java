package com.yeetdot.oreoh.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public interface MachineRecipe<T extends RecipeInput> extends Recipe<T> {
    List<ItemStack> assembleAll();
    List<OutputWithChance> outputs();
    
    long getRecipeEnergy(T input);
    int getRecipeDuration(T input);
}
