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
        SetApplier.applyToMaterials(materialSet -> dropSelf(materialSet.storageBlock));
        SetApplier.applyToMetals(metalSet -> {
            dropSelf(metalSet.storageBlock);
            dropSelf(metalSet.rawBlock);

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
                add(metalSet.oreStone, createMultipleOreDrops(metalSet.oreStone, metalSet.raw, minDrops, maxDrops));
                add(metalSet.oreDeepslate, createMultipleOreDrops(metalSet.oreDeepslate, metalSet.raw, minDrops, maxDrops));
                add(metalSet.oreNether, createMultipleOreDrops(metalSet.oreNether, metalSet.raw, minDrops, maxDrops));
                add(metalSet.oreEnd, createMultipleOreDrops(metalSet.oreEnd, metalSet.raw, minDrops, maxDrops));
            } else {
                add(metalSet.oreStone, createOreDrop(metalSet.oreStone, metalSet.raw));
                add(metalSet.oreDeepslate, createOreDrop(metalSet.oreDeepslate, metalSet.raw));
                add(metalSet.oreNether, createOreDrop(metalSet.oreNether, metalSet.raw));
                add(metalSet.oreEnd, createOreDrop(metalSet.oreEnd, metalSet.raw));
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
