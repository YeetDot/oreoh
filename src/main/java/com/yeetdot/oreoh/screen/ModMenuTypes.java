package com.yeetdot.oreoh.screen;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {
    public static final MenuType<CrusherMenu> CRUSHER_MENU = register("crusher_menu", CrusherMenu::new);
    
    private static <T extends AbstractContainerMenu> MenuType<T> register(String path, MenuType.MenuSupplier<T> supplier) {
        return Registry.register(
                BuiltInRegistries.MENU,
                OreOh.id(path),
                new MenuType<>(supplier, FeatureFlags.DEFAULT_FLAGS)
        );
    }
    
    public static void registerMenus() {
        OreOh.LOGGER.info("Registering Menu Types for " + OreOh.MOD_ID);
    }
}
