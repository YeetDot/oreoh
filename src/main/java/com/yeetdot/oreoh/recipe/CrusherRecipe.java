package com.yeetdot.oreoh.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public record CrusherRecipe(
        CountedIngredient ingredient,
        List<OutputWithChance> outputs,
        long energyCost,
        int processTime
) implements MachineRecipe<MachineRecipeInput> {

    @Override
    public boolean matches(MachineRecipeInput input,  Level level) {
        for (ItemStack stack : input.inputs()) {
            if (!ingredient().ingredient().test(stack) || stack.count() < ingredient().count()) return false;
        }
        return true;
    }

    /**
     * @deprecated Use {@link CrusherRecipe#assembleAll()} instead
     */
    @Deprecated
    @Override
    public  ItemStack assemble(MachineRecipeInput input) {
        return outputs.getFirst().stack();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "crusher";
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
        return PlacementInfo.create(ingredient().ingredient());
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
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
