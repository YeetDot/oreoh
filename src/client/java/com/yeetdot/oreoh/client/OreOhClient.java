package com.yeetdot.oreoh.client;

import net.fabricmc.api.ClientModInitializer;

public class OreOhClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
//		MenuScreens.register(ModMenuTypes.CRUSHER_MENU, CrusherScreen::new);
//		MenuScreens.register(ModMenuTypes.BATTERY_MENU, BatteryScreen::new);
//		ClientPlayNetworking.registerGlobalReceiver(EnergySyncPayload.TYPE, (payload, context) -> {
//			if (context.client().player != null) {
//				var openMenu = Objects.requireNonNull(context.client().player).containerMenu;
//				
//				if (openMenu instanceof EnergyMenu energyMenu) {
//					if (energyMenu.getBlockPos().equals(payload.pos())) {
//						energyMenu.setEnergyValues(payload.energyAmount(), payload.energyCapacity());
//					}
//				}
//			}
//		});
	}
}