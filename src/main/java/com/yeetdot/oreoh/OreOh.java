package com.yeetdot.oreoh;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.item.ModMetals;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.resources.Identifier;

import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OreOh implements ModInitializer {
	public static final String MOD_ID = "oreoh";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
        ModItems.registerItems();
        ModBlocks.registerBlocks();
        ModMetals.registerMetals();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
