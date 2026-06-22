package com.yeetdot.oreoh.client.datagen;

import com.yeetdot.oreoh.set.SetApplier;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public static final TagKey<Block> C_STORAGE_BLOCKS = TagKey.create(
            Registries.BLOCK,
            Identifier.fromNamespaceAndPath("c", "storage_blocks")
    );
    public static final TagKey<Block> C_ORES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores"));

    public BlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        SetApplier.applyToMaterials(materialSet -> {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(materialSet.idSet.storageBlock());
            tag(materialSet.tagSet.storageBlock())
                    .add(materialSet.idSet.storageBlock());
            switch (materialSet.hardness()) {
                case "stone":
                    tag(BlockTags.NEEDS_STONE_TOOL)
                            .add(materialSet.idSet.storageBlock());
                    break;
                case "iron":
                    tag(BlockTags.NEEDS_IRON_TOOL)
                            .add(materialSet.idSet.storageBlock());
                    break;
                case "diamond":
                    tag(BlockTags.NEEDS_DIAMOND_TOOL)
                            .add(materialSet.idSet.storageBlock());
                    break;
                default:
                    break;
            }
            tag(C_STORAGE_BLOCKS)
                    .addTag(materialSet.tagSet.storageBlock());
        });
        SetApplier.applyToNaturals(naturalSet -> {
            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(naturalSet.idSet.oreStone())
                    .add(naturalSet.idSet.oreDeepslate())
                    .add(naturalSet.idSet.oreNether())
                    .add(naturalSet.idSet.oreEnd());
            tag(naturalSet.tagSet.oreParent())
                    .add(naturalSet.idSet.oreStone())
                    .add(naturalSet.idSet.oreDeepslate())
                    .add(naturalSet.idSet.oreNether())
                    .add(naturalSet.idSet.oreEnd());
            switch (naturalSet.hardness()) {
                case "stone":
                    tag(BlockTags.NEEDS_STONE_TOOL)
                            .add(naturalSet.idSet.oreStone())
                            .add(naturalSet.idSet.oreDeepslate())
                            .add(naturalSet.idSet.oreNether())
                            .add(naturalSet.idSet.oreEnd());
                    break;
                case "iron":
                    tag(BlockTags.NEEDS_IRON_TOOL)
                            .add(naturalSet.idSet.oreStone())
                            .add(naturalSet.idSet.oreDeepslate())
                            .add(naturalSet.idSet.oreNether())
                            .add(naturalSet.idSet.oreEnd());
                    break;
                case "diamond":
                    tag(BlockTags.NEEDS_DIAMOND_TOOL)
                            .add(naturalSet.idSet.oreStone())
                            .add(naturalSet.idSet.oreDeepslate())
                            .add(naturalSet.idSet.oreNether())
                            .add(naturalSet.idSet.oreEnd());
                    break;
                default:
                    break;
            }
            tag(C_ORES)
                    .addTag(naturalSet.tagSet.oreParent());
        });
        SetApplier.applyToMetals(metalSet -> {
            tag(metalSet.tagSet.rawBlock())
                    .add(metalSet.idSet.rawBlock());
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(metalSet.idSet.rawBlock());
            switch (metalSet.hardness()) {
                case "stone":
                    tag(BlockTags.NEEDS_STONE_TOOL)
                            .add(metalSet.idSet.rawBlock());
                    break;
                case "iron":
                    tag(BlockTags.NEEDS_IRON_TOOL)
                            .add(metalSet.idSet.rawBlock());
                    break;
                case "diamond":
                    tag(BlockTags.NEEDS_DIAMOND_TOOL)
                            .add(metalSet.idSet.rawBlock());
                    break;
                default:
                    break;
            }
            tag(C_STORAGE_BLOCKS)
                    .addTag(metalSet.tagSet.rawBlock());
        });
    }
}
