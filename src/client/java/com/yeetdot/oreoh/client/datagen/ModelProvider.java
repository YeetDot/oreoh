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

    public ModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        SetApplier.applyToMaterials(set -> {
            blockModelGenerators.createTrivialCube(set.storageBlock);
        });
        SetApplier.applyToMetals(set -> {
            blockModelGenerators.createTrivialCube(set.rawBlock);
        });
        SetApplier.applyToNaturals(set -> {
            blockModelGenerators.createTrivialCube(set.oreStone);
            blockModelGenerators.createTrivialCube(set.oreDeepslate);
            blockModelGenerators.createTrivialCube(set.oreNether);
            blockModelGenerators.createTrivialCube(set.oreEnd);
        });
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        Identifier largeItemId = OreOh.id("item/oversized_flat_item");
        SetApplier.applyToMaterials(set -> {
            itemModelGenerators.generateFlatItem(set.primary, ModelTemplates.FLAT_ITEM);
        });
        SetApplier.applyToMetals(set -> {
            itemModelGenerators.generateFlatItem(set.nugget, ModelTemplates.FLAT_ITEM);
            itemModelGenerators.generateFlatItem(set.raw, ModelTemplates.FLAT_ITEM);
        });
        SetApplier.applyToAlloys(set -> {
            itemModelGenerators.generateFlatItem(set.nugget, ModelTemplates.FLAT_ITEM);
        });

        ModelTemplate OversizedFlatItemModel = new ModelTemplate(Optional.of(largeItemId), Optional.empty(), TextureSlot.LAYER0);

        itemModelGenerators.itemModelOutput.accept(ModItems.GOLDEN_CUDGEL, ItemModelUtils.plainModel(OversizedFlatItemModel.create(ModItems.GOLDEN_CUDGEL, TextureMapping.layer0(ModItems.GOLDEN_CUDGEL), itemModelGenerators.modelOutput)), new ClientItem.Properties(false, true, 1));
    }
}
