package com.yeetdot.oreoh;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.block.entity.AbstractMachineBlockEntity;
import com.yeetdot.oreoh.block.entity.ModBlockEntities;
import com.yeetdot.oreoh.creativetab.ModCreativeModeTabs;
import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.recipe.ModRecipes;
import com.yeetdot.oreoh.screen.ModMenuTypes;
import com.yeetdot.oreoh.set.ModSets;
import com.yeetdot.oreoh.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.reborn.energy.api.EnergyStorage;

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

		EnergyStorage.SIDED.registerForBlockEntities((blockEntity, _) -> {
			if (blockEntity instanceof AbstractMachineBlockEntity<?, ?> machine) {
				return machine.getEnergyStorage();
			}
			return null;
			}, 
				ModBlockEntities.CRUSHER_BLOCK_ENTITY
		);
		
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
