package com.yeetdot.oreoh.client.datagen.lang;

import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.set.SetApplier;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class EN_USProvider extends FabricLanguageProvider {

    public EN_USProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, "en_us", registryLookup);
    }

    public static String capitalizeFirst(String input) {
        if (input == null || input.isEmpty()) return input;
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider provider, TranslationBuilder translationBuilder) {
        SetApplier.applyToMaterials(set -> {
            String name = capitalizeFirst(set.name());
            translationBuilder.add(set.primary, name + (set.isIngot() ? " Ingot" : ""));
            translationBuilder.add(set.storageBlock, "Block of " + name);
        });
        SetApplier.applyToNaturals(set -> {
            String name = capitalizeFirst(set.name());
            translationBuilder.add(set.oreStone, name + " Ore");
            translationBuilder.add(set.oreDeepslate, "Deepslate " + name + " Ore");
            translationBuilder.add(set.oreNether, "Nether " + name + " Ore");
            translationBuilder.add(set.oreEnd, "End " + name + " Ore");
        });
        SetApplier.applyToMetals(set -> {
            String name = capitalizeFirst(set.name());
            translationBuilder.add(set.nugget, name + " Nugget");
            translationBuilder.add(set.raw, "Raw " + name);
            translationBuilder.add(set.rawBlock, "Block of Raw " + name);
        });
        SetApplier.applyToAlloys(set -> {
            String name = capitalizeFirst(set.name());
            translationBuilder.add(set.nugget, name + " Nugget");
        });

        translationBuilder.add(ModItems.GOLDEN_CUDGEL, "The Compliant Golden-Hooped Rod");
        translationBuilder.add("itemGroup.oreoh.tab", "OreOh!");
        translationBuilder.add("container.oreoh.crusher", "Crusher");
    }
}
