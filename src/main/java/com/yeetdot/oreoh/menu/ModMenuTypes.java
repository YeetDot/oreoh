package com.yeetdot.oreoh.menu;

import com.yeetdot.oreoh.OreOh;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

@SuppressWarnings("unused")
public class ModMenuTypes {
    public static final MenuType<CrusherMenu> CRUSHER_MENU = registerExtended("crusher_menu", CrusherMenu::new);
    public static final MenuType<CreativeEnergyMenu> CREATIVE_ENERGY_MENU = registerExtended("creative_energy_menu", CreativeEnergyMenu::new);
    
    private static <T extends AbstractContainerMenu> MenuType<T> register(String path, MenuType.MenuSupplier<T> menuSupplier) {
        return Registry.register(
                BuiltInRegistries.MENU,
                OreOh.id(path),
                new MenuType<>(menuSupplier, FeatureFlags.DEFAULT_FLAGS)
        );
    }

    private static <T extends AbstractContainerMenu> ExtendedMenuType<T, BlockPos> registerExtended(String path, ExtendedMenuType.ExtendedFactory<T, BlockPos> menuSupplier) {
        return Registry.register(
                BuiltInRegistries.MENU,
                OreOh.id(path),
                new ExtendedMenuType<>(menuSupplier, BlockPos.STREAM_CODEC)
        );
    }
    
    public static void registerMenus() {
        OreOh.LOGGER.info("Registering Menu Types for " + OreOh.MOD_ID);
    }
}
