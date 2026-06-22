package com.yeetdot.oreoh.client;

import com.yeetdot.oreoh.client.datagen.BlockLootTableProvider;
import com.yeetdot.oreoh.client.datagen.BlockTagProvider;
import com.yeetdot.oreoh.client.datagen.ItemTagProvider;
import com.yeetdot.oreoh.client.datagen.ModelProvider;
import com.yeetdot.oreoh.client.datagen.lang.EN_USProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class OreOhDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();

        pack.addProvider(ModelProvider::new);
        pack.addProvider(EN_USProvider::new);
        BlockTagProvider blockTagProvider = pack.addProvider(BlockTagProvider::new);
        pack.addProvider((output, registriesFuture) -> new ItemTagProvider(output, registriesFuture, blockTagProvider));
        pack.addProvider(BlockLootTableProvider::new);
	}
}
