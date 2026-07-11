package com.yeetdot.oreoh;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.block.entity.ModBlockEntities;
import com.yeetdot.oreoh.creativetab.ModCreativeModeTabs;
import com.yeetdot.oreoh.data.ElectricGridRegistry;
import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.menu.ModMenuTypes;
import com.yeetdot.oreoh.recipe.ModRecipes;
import com.yeetdot.oreoh.set.ModSets;
import com.yeetdot.oreoh.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OreOh implements ModInitializer {
	public static final String MOD_ID = "oreoh";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
        ModCreativeModeTabs.registerCreativeModeTabs();
        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModSets.registerSets();
		ModWorldGeneration.generateWorldGen();
		ModBlockEntities.registerBlockEntities();
		ModMenuTypes.registerMenus();
		ModRecipes.registerRecipes();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerLevel level : server.getAllLevels()) {
				ElectricGridRegistry registry = ElectricGridRegistry.get(level);
				registry.tickAllGrids(level);
			}
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
