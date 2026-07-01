package com.yeetdot.oreoh;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.block.entity.ModBlockEntities;
import com.yeetdot.oreoh.creativetab.ModCreativeModeTabs;
import com.yeetdot.oreoh.energy.ElectricGrid;
import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.menu.ModMenuTypes;
import com.yeetdot.oreoh.recipe.ModRecipes;
import com.yeetdot.oreoh.set.ModSets;
import com.yeetdot.oreoh.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.Identifier;
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

//		EnergyStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> {
//			if (blockEntity instanceof AbstractEnergyContainerBlockEntity entity && direction != null) {
//				EnergyStorage storage = entity.getEnergyStorage();
//				return new EnergyStorage() {
//					@Override
//					public long insert(long maxAmount, TransactionContext transaction) {
//						if (!entity.canInsertEnergy(direction)) return 0;
//						return storage.insert(maxAmount, transaction);
//					}
//
//					@Override
//					public long extract(long maxAmount, TransactionContext transaction) {
//						if (!entity.canExtractEnergy(direction)) return 0;
//						return storage.extract(maxAmount, transaction);
//					}
//
//					@Override
//					public long getAmount() {
//						return storage.getAmount();
//					}
//
//					@Override
//					public long getCapacity() {
//						return storage.getCapacity();
//					}
//				};
//			}
//			return null;
//			}
//		);

//		ServerTickEvents.END_LEVEL_TICK.register(EnergyTransferManager::flush);
		
		ServerTickEvents.END_SERVER_TICK.register(_ -> ElectricGrid.tickAllGrids());
//
//		PayloadTypeRegistry.clientboundPlay().register(EnergySyncPayload.TYPE, EnergySyncPayload.CODEC);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
