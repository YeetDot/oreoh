package com.yeetdot.oreoh.client.datagen;

import com.yeetdot.oreoh.set.SetApplier;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends FabricRecipeProvider {
    public RecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected net.minecraft.data.recipes.RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new net.minecraft.data.recipes.RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                SetApplier.applyToMaterials(set -> nineBlockStorageRecipesRecipesWithCustomUnpacking(
                        RecipeCategory.MISC, set.primary,
                        RecipeCategory.BUILDING_BLOCKS, set.storageBlock,
                        set.name() + (set.isIngot() ? "_ingot" : "") + "_from_block",
                        set.name()
                ));
                SetApplier.applyToNaturals(set -> {
                    List<ItemLike> smeltable = new ArrayList<>();
                    smeltable.add(set.oreStone);
                    smeltable.add(set.oreDeepslate);
                    smeltable.add(set.oreNether);
                    smeltable.add(set.oreEnd);
                    oreSmelting(smeltable, RecipeCategory.MISC, CookingBookCategory.BLOCKS, set.primary, set.hardness().equals("diamond") ? 1.0F : 0.7F, 200, set.name());
                    oreBlasting(smeltable, RecipeCategory.MISC, CookingBookCategory.BLOCKS, set.primary, set.hardness().equals("diamond") ? 1.0F : 0.7F, 100, set.name());
                });
                SetApplier.applyToMetals(set -> {
                    nineBlockStorageRecipesWithCustomPacking(
                            RecipeCategory.MISC, set.nugget,
                            RecipeCategory.MISC, set.primary,
                            set.name() + "_ingot_from_nugget",
                            set.name());

                    List<ItemLike> smeltable = new ArrayList<>();
                    smeltable.add(set.raw);
                    oreSmelting(smeltable, RecipeCategory.MISC, CookingBookCategory.BLOCKS, set.primary, set.hardness().equals("diamond") ? 1.0F : 0.7F, 200, set.name());
                    oreBlasting(smeltable, RecipeCategory.MISC, CookingBookCategory.BLOCKS, set.primary, set.hardness().equals("diamond") ? 1.0F : 0.7F, 100, set.name());
                });
                SetApplier.applyToAlloys(set -> nineBlockStorageRecipesWithCustomPacking(
                        RecipeCategory.MISC, set.nugget,
                        RecipeCategory.MISC, set.primary,
                        set.name() + "_ingot_from_nugget",
                        set.name()));
            }
        };
    }

    @Override
    public String getName() {
        return "OreOh! recipes";
    }
}
