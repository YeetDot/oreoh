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
        SetApplier.applyToMetals(metalSet -> {
            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(metalSet.IDS.storageBlock())
                    .add(metalSet.IDS.rawBlock())
                    .add(metalSet.ORES.IDS.ore())
                    .add(metalSet.ORES.IDS.deepslateOre());
            switch (metalSet.hardness) {
                case "stone":
                    tag(BlockTags.NEEDS_STONE_TOOL)
                            .add(metalSet.IDS.storageBlock())
                            .add(metalSet.IDS.rawBlock())
                            .add(metalSet.ORES.IDS.ore())
                            .add(metalSet.ORES.IDS.deepslateOre());
                    break;
                case "iron":
                    tag(BlockTags.NEEDS_IRON_TOOL)
                            .add(metalSet.IDS.storageBlock())
                            .add(metalSet.IDS.rawBlock())
                            .add(metalSet.ORES.IDS.ore())
                            .add(metalSet.ORES.IDS.deepslateOre());
                    break;
                case "diamond":
                    tag(BlockTags.NEEDS_DIAMOND_TOOL)
                            .add(metalSet.IDS.storageBlock())
                            .add(metalSet.IDS.rawBlock())
                            .add(metalSet.ORES.IDS.ore())
                            .add(metalSet.ORES.IDS.deepslateOre());
                    break;
                case null, default: break;
            }
            tag(metalSet.TAGS.blockTagBlock())
                    .add(metalSet.IDS.storageBlock());
            tag(metalSet.TAGS.blockTagRawBlock())
                    .add(metalSet.IDS.rawBlock());
            tag(metalSet.ORES.TAGS.blockTagOre())
                    .add(metalSet.ORES.IDS.ore())
                    .add(metalSet.ORES.IDS.deepslateOre());
            tag(C_STORAGE_BLOCKS)
                    .addTag(metalSet.TAGS.blockTagBlock())
                    .addTag(metalSet.TAGS.blockTagRawBlock());
            tag(C_ORES)
                    .addTag(metalSet.ORES.TAGS.blockTagOre());
        });
    }
}
