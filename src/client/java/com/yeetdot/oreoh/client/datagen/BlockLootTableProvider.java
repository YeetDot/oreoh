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
        SetApplier.applyToMaterials(set -> dropSelf(set.storageBlock));
        SetApplier.applyToMetals(set -> {
            dropSelf(set.rawBlock);

            int minDrops = 1;
            int maxDrops = 1;
            boolean isMultiDrop = false;

            switch (set.name()) {
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
                add(set.oreStone, createMultipleOreDrops(set.oreStone, set.raw, minDrops, maxDrops));
                add(set.oreDeepslate, createMultipleOreDrops(set.oreDeepslate, set.raw, minDrops, maxDrops));
                add(set.oreNether, createMultipleOreDrops(set.oreNether, set.raw, minDrops, maxDrops));
                add(set.oreEnd, createMultipleOreDrops(set.oreEnd, set.raw, minDrops, maxDrops));
            } else {
                add(set.oreStone, createOreDrop(set.oreStone, set.raw));
                add(set.oreDeepslate, createOreDrop(set.oreDeepslate, set.raw));
                add(set.oreNether, createOreDrop(set.oreNether, set.raw));
                add(set.oreEnd, createOreDrop(set.oreEnd, set.raw));
            }
        });
        SetApplier.applyToGems(set -> {

            // Determine drop counts based on the metal name
            int minDrops = 1;
            int maxDrops = 1;
            boolean isMultiDrop = false;

            switch (set.name()) {
                case "cinnabar" -> {
                    maxDrops = 3;
                    isMultiDrop = true;
                }
                case "fluorite" -> {
                    minDrops = 2;
                    maxDrops = 4;
                    isMultiDrop = true;
                }
                default -> {}
            }

            if (isMultiDrop) {
                add(set.oreStone, createMultipleOreDrops(set.oreStone, set.primary, minDrops, maxDrops));
                add(set.oreDeepslate, createMultipleOreDrops(set.oreDeepslate, set.primary, minDrops, maxDrops));
                add(set.oreNether, createMultipleOreDrops(set.oreNether, set.primary, minDrops, maxDrops));
                add(set.oreEnd, createMultipleOreDrops(set.oreEnd, set.primary, minDrops, maxDrops));
            } else {
                add(set.oreStone, createOreDrop(set.oreStone, set.primary));
                add(set.oreDeepslate, createOreDrop(set.oreDeepslate, set.primary));
                add(set.oreNether, createOreDrop(set.oreNether, set.primary));
                add(set.oreEnd, createOreDrop(set.oreEnd, set.primary));
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
