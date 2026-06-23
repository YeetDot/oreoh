package com.yeetdot.oreoh.client.datagen;

import com.yeetdot.oreoh.set.SetApplier;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public static final TagKey<Block> C_ORES_IN_GROUND_END_STONE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores_in_ground/end_stone"));

    public BlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        SetApplier.applyToMaterials(set -> {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(set.idSet.storageBlock());
            tag(set.tagSet.storageBlock())
                    .add(set.idSet.storageBlock());
            switch (set.hardness()) {
                case "stone":
                    tag(BlockTags.NEEDS_STONE_TOOL)
                            .add(set.idSet.storageBlock());
                    break;
                case "iron":
                    tag(BlockTags.NEEDS_IRON_TOOL)
                            .add(set.idSet.storageBlock());
                    break;
                case "diamond":
                    tag(BlockTags.NEEDS_DIAMOND_TOOL)
                            .add(set.idSet.storageBlock());
                    break;
                default:
                    break;
            }
            tag(ConventionalBlockTags.STORAGE_BLOCKS)
                    .addTag(set.tagSet.storageBlock());
        });
        SetApplier.applyToNaturals(set -> {
            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(set.idSet.oreStone())
                    .add(set.idSet.oreDeepslate())
                    .add(set.idSet.oreNether())
                    .add(set.idSet.oreEnd());
            tag(set.tagSet.oreParent())
                    .add(set.idSet.oreStone())
                    .add(set.idSet.oreDeepslate())
                    .add(set.idSet.oreNether())
                    .add(set.idSet.oreEnd());
            switch (set.hardness()) {
                case "stone":
                    tag(BlockTags.NEEDS_STONE_TOOL)
                            .add(set.idSet.oreStone())
                            .add(set.idSet.oreDeepslate())
                            .add(set.idSet.oreNether())
                            .add(set.idSet.oreEnd());
                    break;
                case "iron":
                    tag(BlockTags.NEEDS_IRON_TOOL)
                            .add(set.idSet.oreStone())
                            .add(set.idSet.oreDeepslate())
                            .add(set.idSet.oreNether())
                            .add(set.idSet.oreEnd());
                    break;
                case "diamond":
                    tag(BlockTags.NEEDS_DIAMOND_TOOL)
                            .add(set.idSet.oreStone())
                            .add(set.idSet.oreDeepslate())
                            .add(set.idSet.oreNether())
                            .add(set.idSet.oreEnd());
                    break;
                default:
                    break;
            }
            tag(ConventionalBlockTags.ORES)
                    .addTag(set.tagSet.oreParent());
            tag(ConventionalBlockTags.ORES_IN_GROUND_STONE)
                    .add(set.idSet.oreStone());
            tag(ConventionalBlockTags.ORES_IN_GROUND_DEEPSLATE)
                    .add(set.idSet.oreDeepslate());
            tag(ConventionalBlockTags.ORES_IN_GROUND_NETHERRACK)
                    .add(set.idSet.oreNether());
            tag(C_ORES_IN_GROUND_END_STONE)
                    .add(set.idSet.oreEnd());
        });
        SetApplier.applyToMetals(set -> {
            tag(set.tagSet.rawBlock())
                    .add(set.idSet.rawBlock());
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(set.idSet.rawBlock());
            switch (set.hardness()) {
                case "stone":
                    tag(BlockTags.NEEDS_STONE_TOOL)
                            .add(set.idSet.rawBlock());
                    break;
                case "iron":
                    tag(BlockTags.NEEDS_IRON_TOOL)
                            .add(set.idSet.rawBlock());
                    break;
                case "diamond":
                    tag(BlockTags.NEEDS_DIAMOND_TOOL)
                            .add(set.idSet.rawBlock());
                    break;
                default:
                    break;
            }
            tag(ConventionalBlockTags.STORAGE_BLOCKS)
                    .addTag(set.tagSet.rawBlock());
        });
    }
}
