package com.yeetdot.oreoh.recipe;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {
    public static final RecipeType<CrusherRecipe> CRUSHER_TYPE = register("crushing");
    
    private static <T extends Recipe<?>> RecipeType<T> register(String path) {
        return Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                OreOh.id(path),
                new RecipeType<T>() {
                    @Override
                    public String toString() {
                        return path;
                    }
                }
        );
    }
    
    public static void registerRecipes() {
        OreOh.LOGGER.info("Registering recipes for " + OreOh.MOD_ID);
    }
}
