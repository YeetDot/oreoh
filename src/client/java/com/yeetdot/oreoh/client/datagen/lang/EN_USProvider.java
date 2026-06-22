package com.yeetdot.oreoh.client.datagen.lang;

import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.set.SetApplier;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

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
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
        SetApplier.applyToMetals(metalSet -> {
            String name = capitalizeFirst(metalSet.name());
            translationBuilder.add(metalSet.BLOCK, String.format("Block of %s", name));
            translationBuilder.add(metalSet.INGOT, String.format("%s Ingot", name));
            translationBuilder.add(metalSet.NUGGET, String.format("%s Nugget", name));
            translationBuilder.add(metalSet.RAW_BLOCK, String.format("Block of Raw %s", name));
            translationBuilder.add(metalSet.ORES.ORE, String.format("%s Ore", name));
            translationBuilder.add(metalSet.ORES.DEEPSLATE_ORE, String.format("Deepslate %s Ore", name));
        });

        translationBuilder.add(ModItems.GOLDEN_CUDGEL, "The Compliant Golden-Hooped Rod");
        translationBuilder.add("itemGroup.oreoh.tab", "OreOh!");
    }
}
