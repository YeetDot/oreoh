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
        SetApplier.applyToMetals(metalSet -> {
            blockModelGenerators.createTrivialCube(metalSet.storageBlock);
            blockModelGenerators.createTrivialCube(metalSet.rawBlock);
        });
        SetApplier.applyToNaturals(naturalSet -> {
            blockModelGenerators.createTrivialCube(naturalSet.oreStone);
            blockModelGenerators.createTrivialCube(naturalSet.oreDeepslate);
            blockModelGenerators.createTrivialCube(naturalSet.oreNether);
            blockModelGenerators.createTrivialCube(naturalSet.oreEnd);
        });
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        Identifier largeItemId = OreOh.id("item/oversized_flat_item");
        SetApplier.applyToMetals(metalSet -> {
            itemModelGenerators.generateFlatItem(metalSet.primary, ModelTemplates.FLAT_ITEM);
            itemModelGenerators.generateFlatItem(metalSet.nugget, ModelTemplates.FLAT_ITEM);
            itemModelGenerators.generateFlatItem(metalSet.raw, ModelTemplates.FLAT_ITEM);
        });

        ModelTemplate OversizedFlatItemModel = new ModelTemplate(Optional.of(largeItemId), Optional.empty(), TextureSlot.LAYER0);

        itemModelGenerators.itemModelOutput.accept(ModItems.GOLDEN_CUDGEL, ItemModelUtils.plainModel(OversizedFlatItemModel.create(ModItems.GOLDEN_CUDGEL, TextureMapping.layer0(ModItems.GOLDEN_CUDGEL), itemModelGenerators.modelOutput)), new ClientItem.Properties(false, true, 1));
    }
}
