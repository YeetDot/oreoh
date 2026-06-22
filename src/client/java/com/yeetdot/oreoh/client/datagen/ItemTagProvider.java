package com.yeetdot.oreoh.client.datagen;

import com.yeetdot.oreoh.set.SetApplier;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagsProvider.ItemTagsProvider {


    public ItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture, FabricTagsProvider.BlockTagsProvider blockTagsProvider) {
        super(output, registryLookupFuture, blockTagsProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        SetApplier.applyToMetals(metalSet -> {
            copy(metalSet.TAGS.blockTagBlock(), metalSet.TAGS.itemTagBlock());
            copy(metalSet.TAGS.blockTagRawBlock(), metalSet.TAGS.itemTagRawBlock());
            copy(metalSet.ORES.TAGS.blockTagOre(), metalSet.ORES.TAGS.itemTagOre());

            this.tag(metalSet.TAGS.itemTagIngot())
                    .add(metalSet.IDS.ingot());
            this.tag(metalSet.TAGS.itemTagNugget())
                    .add(metalSet.IDS.nugget());
            this.tag(metalSet.TAGS.itemTagRaw())
                    .add(metalSet.IDS.raw());
        });
    }
}
