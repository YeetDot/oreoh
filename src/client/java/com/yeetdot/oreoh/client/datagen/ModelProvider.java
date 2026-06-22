package com.yeetdot.oreoh.client.datagen;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.set.SetApplier;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class ModelProvider extends FabricModelProvider {
    private final FabricPackOutput dataOutput;

    public ModelProvider(FabricPackOutput output) {
        super(output);
        this.dataOutput = output;
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        SetApplier.applyToMetals(metalSet -> {
            blockModelGenerators.createTrivialCube(metalSet.BLOCK);
            blockModelGenerators.createTrivialCube(metalSet.RAW_BLOCK);
        });
        SetApplier.applyToOres(oreSet -> {
            blockModelGenerators.createTrivialCube(oreSet.ORE);
            blockModelGenerators.createTrivialCube(oreSet.DEEPSLATE_ORE);
        });
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        Identifier largeItemId = OreOh.id("item/oversized_flat_item");
        SetApplier.applyToMetals(metalSet -> {
            itemModelGenerators.generateFlatItem(metalSet.INGOT, ModelTemplates.FLAT_ITEM);
            itemModelGenerators.generateFlatItem(metalSet.NUGGET, ModelTemplates.FLAT_ITEM);
        });

        ModelTemplate OversizedFlatItemModel = new ModelTemplate(Optional.of(largeItemId), Optional.empty(), TextureSlot.LAYER0);

        itemModelGenerators.itemModelOutput.accept(ModItems.GOLDEN_CUDGEL, ItemModelUtils.plainModel(OversizedFlatItemModel.create(ModItems.GOLDEN_CUDGEL, TextureMapping.layer0(ModItems.GOLDEN_CUDGEL), itemModelGenerators.modelOutput)), new ClientItem.Properties(false, true, 1));
    }
}
