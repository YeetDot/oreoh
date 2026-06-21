package com.yeetdot.oreoh.client.datagen;

import com.google.gson.JsonObject;
import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.DecoratedPotSpecialRenderer;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    private final FabricPackOutput dataOutput;

    public ModModelProvider(FabricPackOutput output) {
        super(output);
        this.dataOutput = output;
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        Identifier largeItemId = OreOh.id("item/oversized_flat_item");

        ModelTemplate OversizedFlatItemModel = new ModelTemplate(Optional.of(largeItemId), Optional.empty(), TextureSlot.LAYER0);

        itemModelGenerators.itemModelOutput.accept(ModItems.GOLDEN_CUDGEL, ItemModelUtils.plainModel(OversizedFlatItemModel.create(ModItems.GOLDEN_CUDGEL, TextureMapping.layer0(ModItems.GOLDEN_CUDGEL), itemModelGenerators.modelOutput)), new ClientItem.Properties(false, true, 1));
    }
}
