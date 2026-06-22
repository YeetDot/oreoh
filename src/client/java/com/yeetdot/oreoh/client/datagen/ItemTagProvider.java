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
        SetApplier.applyToMaterials(materialSet -> copy(materialSet.tagSet.storageBlock(), materialSet.tagSet.storageBlockItem()));
        SetApplier.applyToNaturals(naturalSet -> copy(naturalSet.tagSet.oreParent(), naturalSet.tagSet.oreParentItem()));
        SetApplier.applyToMetals(metalSet -> {
            copy(metalSet.tagSet.rawBlock(), metalSet.tagSet.rawBlockItem());

            this.tag(metalSet.tagSet.primary("ingots"))
                    .add(metalSet.idSet.primary());
            this.tag(metalSet.tagSet.nugget())
                    .add(metalSet.idSet.nugget());
            this.tag(metalSet.tagSet.rawItem())
                    .add(metalSet.idSet.rawItem());
        });
    }
}
