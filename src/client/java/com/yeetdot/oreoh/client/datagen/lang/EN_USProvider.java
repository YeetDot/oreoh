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
        SetApplier.applyToMaterials(materialSet -> {
            String name = capitalizeFirst(materialSet.name());
            translationBuilder.add(materialSet.storageBlock, "Block of " + name);
        });
        SetApplier.applyToNaturals(naturalSet -> {
            String name = capitalizeFirst(naturalSet.name());
            translationBuilder.add(naturalSet.oreStone, name + " Ore");
            translationBuilder.add(naturalSet.oreDeepslate, "Deepslate " + name + " Ore");
            translationBuilder.add(naturalSet.oreNether, "Nether " + name + " Ore");
            translationBuilder.add(naturalSet.oreEnd, "End " + name + " Ore");
        });
        SetApplier.applyToMetals(metalSet -> {
            String name = capitalizeFirst(metalSet.name());
            translationBuilder.add(metalSet.primary, name + " Ingot");
            translationBuilder.add(metalSet.nugget, name + " Nugget");
            translationBuilder.add(metalSet.raw, "Raw " + name);
            translationBuilder.add(metalSet.rawBlock, "Block of Raw " + name);
        });

        translationBuilder.add(ModItems.GOLDEN_CUDGEL, "The Compliant Golden-Hooped Rod");
        translationBuilder.add("itemGroup.oreoh.tab", "OreOh!");
    }
}
