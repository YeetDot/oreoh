package com.yeetdot.oreoh.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public record CrusherRecipe(
        CountedIngredient ingredient,
        List<OutputWithChance> outputs,
        long energyCost,
        int processTime
) implements MachineRecipe<MachineRecipeInput> {

    @Override
    public boolean matches(@NonNull MachineRecipeInput input, @NonNull Level level) {
        return false;
    }

    /**
     * @deprecated Use {@link CrusherRecipe#assembleAll()} instead
     */
    @Deprecated
    @Override
    public @NonNull ItemStack assemble(@NonNull MachineRecipeInput input) {
        return outputs.getFirst().stack();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NonNull String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<MachineRecipeInput>> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<? extends Recipe<MachineRecipeInput>> getType() {
        return null;
    }

    @Override
    public PlacementInfo placementInfo() {
        return null;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    @Override
    public List<ItemStack> assembleAll() {
        List<ItemStack> list = new ArrayList<>();
        outputs.forEach(itemStack -> list.add(itemStack.stack()));
        return list;
    }

    @Override
    public long getRecipeEnergy(MachineRecipeInput input) {
        return energyCost();
    }

    @Override
    public int getRecipeDuration(MachineRecipeInput input) {
        return processTime();
    }
}
