package com.yeetdot.oreoh.creativetab;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.set.SetApplier;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

public class ModCreativeModeTabs {
    public static final CreativeModeTab OREOH_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, OreOh.id("oreoh_tab"), FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.GOLDEN_CUDGEL)).title(Component.translatable("itemGroup.oreoh.tab")).displayItems(((parameters, output) -> {
        SetApplier.applyToMaterials(materialSet -> output.accept(materialSet.primary));
        SetApplier.applyToMaterials(materialSet -> output.accept(materialSet.storageBlock));
        SetApplier.applyToNaturals(naturalSet -> output.accept(naturalSet.oreStone));
        SetApplier.applyToNaturals(naturalSet -> output.accept(naturalSet.oreDeepslate));
        SetApplier.applyToNaturals(naturalSet -> output.accept(naturalSet.oreNether));
        SetApplier.applyToNaturals(naturalSet -> output.accept(naturalSet.oreEnd));
        SetApplier.applyToMetals(metalSet -> output.accept(metalSet.rawBlock));
        SetApplier.applyToMetals(metalSet -> output.accept(metalSet.nugget));
        SetApplier.applyToMetals(metalSet -> output.accept(metalSet.raw));
    })).build());

    private static void addItemToTab(ResourceKey<@NotNull CreativeModeTab> tab, Item item, Item beforeItem) {
        CreativeModeTabEvents.modifyOutputEvent(tab).register(output -> output.insertAfter(beforeItem, item));
    }

    public static void registerCreativeModeTabs() {
        OreOh.LOGGER.info("Registering creative mode tabs for " + OreOh.MOD_ID);

        addItemToTab(CreativeModeTabs.COMBAT, ModItems.GOLDEN_CUDGEL, Items.MACE);
    }
}
