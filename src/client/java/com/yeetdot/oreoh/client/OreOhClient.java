package com.yeetdot.oreoh.client;

import com.yeetdot.oreoh.client.screen.CrusherScreen;
import com.yeetdot.oreoh.screen.ModMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class OreOhClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenuTypes.CRUSHER_MENU, CrusherScreen::new);
	}
}