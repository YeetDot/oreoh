package com.yeetdot.oreoh.client.screen;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.menu.BatteryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class BatteryScreen extends EnergyDisplayingScreen<BatteryMenu> {
    public static final Identifier BATTERY_LOCATION = OreOh.id("textures/gui/battery.png");
    public static final Identifier ENERGY_SPRITE = OreOh.id("textures/gui/sprites/large_energy_sprite.png");
    
    public BatteryScreen(BatteryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 60, 15, 56, 56);
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return BATTERY_LOCATION;
    }

    @Override
    protected Identifier getEnergyDisplayTexture() {
        return ENERGY_SPRITE;
    }
}
