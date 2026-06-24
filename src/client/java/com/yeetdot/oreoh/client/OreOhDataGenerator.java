package com.yeetdot.oreoh.client;

import com.yeetdot.oreoh.client.datagen.*;
import com.yeetdot.oreoh.client.datagen.lang.EN_USProvider;
import com.yeetdot.oreoh.world.ModConfiguredFeatures;
import com.yeetdot.oreoh.world.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class OreOhDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();

        pack.addProvider(ModelProvider::new);
        pack.addProvider(EN_USProvider::new);
        BlockTagProvider blockTagProvider = pack.addProvider(BlockTagProvider::new);
        pack.addProvider((output, registriesFuture) -> new ItemTagProvider(output, registriesFuture, blockTagProvider));
        pack.addProvider(BlockLootTableProvider::new);
        pack.addProvider(RecipeProvider::new);
        pack.addProvider(RegistryDataGenerator::new);
	}

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        registryBuilder.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
    }
}
