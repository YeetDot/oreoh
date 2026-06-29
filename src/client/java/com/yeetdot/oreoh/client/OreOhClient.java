package com.yeetdot.oreoh.client;

import com.yeetdot.oreoh.client.screen.BatteryScreen;
import com.yeetdot.oreoh.client.screen.CrusherScreen;
import com.yeetdot.oreoh.menu.EnergyMenu;
import com.yeetdot.oreoh.menu.ModMenuTypes;
import com.yeetdot.oreoh.network.EnergySyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;

import java.util.Objects;

public class OreOhClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenuTypes.CRUSHER_MENU, CrusherScreen::new);
		MenuScreens.register(ModMenuTypes.BATTERY_MENU, BatteryScreen::new);
		ClientPlayNetworking.registerGlobalReceiver(EnergySyncPayload.TYPE, (payload, context) -> {
			if (context.client().player != null) {
				var openMenu = Objects.requireNonNull(context.client().player).containerMenu;
				
				if (openMenu instanceof EnergyMenu energyMenu) {
					if (energyMenu.getBlockPos().equals(payload.pos())) {
						energyMenu.setEnergyValues(payload.energyAmount(), payload.energyCapacity());
					}
				}
			}
		});
	}
}