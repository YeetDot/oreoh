package com.yeetdot.oreoh.client.datagen;

import com.yeetdot.oreoh.set.SetApplier;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.concurrent.CompletableFuture;

public class BlockLootTableProvider extends FabricBlockLootSubProvider {
    public BlockLootTableProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        SetApplier.applyToMetals(metalSet -> {
            dropSelf(metalSet.BLOCK);
            dropSelf(metalSet.RAW_BLOCK);

            // Determine drop counts based on the metal name
            int minDrops = 1;
            int maxDrops = 1;
            boolean isMultiDrop = false;

            switch (metalSet.name()) {
                case "aluminum" -> {
                    minDrops = 2;
                    maxDrops = 5;
                    isMultiDrop = true;
                }
                case "zinc" -> {
                    minDrops = 2;
                    maxDrops = 4;
                    isMultiDrop = true;
                }
                case "lead" -> {
                    minDrops = 2;
                    maxDrops = 3;
                    isMultiDrop = true;
                }
                default -> {}
            }

            if (isMultiDrop) {
                add(metalSet.ORES.ORE, createMultipleOreDrops(metalSet.ORES.ORE, metalSet.RAW, minDrops, maxDrops));
                add(metalSet.ORES.DEEPSLATE_ORE, createMultipleOreDrops(metalSet.ORES.DEEPSLATE_ORE, metalSet.RAW, minDrops, maxDrops));
            } else {
                add(metalSet.ORES.ORE, createOreDrop(metalSet.ORES.ORE, metalSet.RAW));
                add(metalSet.ORES.DEEPSLATE_ORE, createOreDrop(metalSet.ORES.DEEPSLATE_ORE, metalSet.RAW));
            }
        });
    }

    public LootTable.Builder createMultipleOreDrops(Block block, Item drop, float minValue, float maxValue) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(
                block,
                this.applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(drop)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minValue, maxValue)))
                                .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
                )
        );
    }
}
