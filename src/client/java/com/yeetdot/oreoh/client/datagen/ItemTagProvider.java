package com.yeetdot.oreoh.client.datagen;

import com.yeetdot.oreoh.set.SetApplier;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagsProvider.ItemTagsProvider {


    public ItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture, FabricTagsProvider.BlockTagsProvider blockTagsProvider) {
        super(output, registryLookupFuture, blockTagsProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        SetApplier.applyToMaterials(set -> {
            copy(set.tagSet.storageBlock(), set.tagSet.storageBlockItem());
            this.tag(set.tagSet.primary())
                    .add(set.idSet.primary());
        });
        SetApplier.applyToNaturals(set -> {
            copy(set.tagSet.oreParent(), set.tagSet.oreParentItem());
            var oresAppender = this.tag(ConventionalItemTags.ORES);
            var groundAppender = this.tag(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ores_in_ground")));
            var stoneGroundAppender = this.tag(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ores_in_ground/stone")));
            var deepslateGroundAppender = this.tag(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ores_in_ground/deepslate")));
            var netherGroundAppender = this.tag(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ores_in_ground/netherrack")));
            var endGroundAppender = this.tag(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ores_in_ground/end_stone")));

            var stoneItemKey = ResourceKey.create(Registries.ITEM, set.idSet.oreStone().identifier());
            oresAppender.add(stoneItemKey);
            groundAppender.add(stoneItemKey);
            stoneGroundAppender.add(stoneItemKey);

            var deepslateItemKey = ResourceKey.create(Registries.ITEM, set.idSet.oreDeepslate().identifier());
            oresAppender.add(deepslateItemKey);
            groundAppender.add(deepslateItemKey);
            deepslateGroundAppender.add(deepslateItemKey);

            var netherItemKey = ResourceKey.create(Registries.ITEM, set.idSet.oreNether().identifier());
            oresAppender.add(netherItemKey);
            groundAppender.add(netherItemKey);
            netherGroundAppender.add(netherItemKey);

            var endItemKey = ResourceKey.create(Registries.ITEM, set.idSet.oreEnd().identifier());
            oresAppender.add(endItemKey);
            groundAppender.add(endItemKey);
            endGroundAppender.add(endItemKey);
        });
        SetApplier.applyToMetals(set -> {
            copy(set.tagSet.rawBlock(), set.tagSet.rawBlockItem());
            this.tag(set.tagSet.nugget())
                    .add(set.idSet.nugget());
            this.tag(set.tagSet.rawItem())
                    .add(set.idSet.rawItem());
            this.tag(ConventionalItemTags.INGOTS)
                    .addTag(set.tagSet.primary());
            this.tag(ConventionalItemTags.NUGGETS)
                    .addTag(set.tagSet.nugget());
            this.tag(ConventionalItemTags.RAW_MATERIALS)
                    .addTag(set.tagSet.rawItem());
            this.tag(ConventionalItemTags.STORAGE_BLOCKS)
                    .addTag(set.tagSet.rawBlockItem());
        });
        SetApplier.applyToAlloys(set -> {
            this.tag(set.tagSet.nugget())
                    .add(set.idSet.nugget());
            this.tag(ConventionalItemTags.INGOTS)
                    .addTag(set.tagSet.primary());
            this.tag(ConventionalItemTags.NUGGETS)
                    .addTag(set.tagSet.nugget());
        });
        SetApplier.applyToGems(set -> this.tag(ConventionalItemTags.GEMS)
                .addTag(set.tagSet.primary()));
    }
}
