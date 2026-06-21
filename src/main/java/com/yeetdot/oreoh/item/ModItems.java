package com.yeetdot.oreoh.item;

import com.yeetdot.oreoh.OreOh;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ModItems {
    public static final Item GOLDEN_CUDGEL = registerItem("golden_cudgel", properties -> new Item(properties.sword(ToolMaterial.GOLD, 887, 164).component(DataComponents.ATTACK_RANGE, new AttackRange(0, 666, 0, 666, 10, 1))));

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, OreOh.id(name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, OreOh.id(name)))));
    }

    private static void addItemToTab(ResourceKey<@NotNull CreativeModeTab> tab, Item item) {
        CreativeModeTabEvents.modifyOutputEvent(tab).register(output -> output.accept(item));
    }

    private static void addItemToTab(ResourceKey<@NotNull CreativeModeTab> tab, Item item, Item beforeItem) {
        CreativeModeTabEvents.modifyOutputEvent(tab).register(output -> output.insertAfter(beforeItem, item));
    }

    public static void registerItems() {
        OreOh.LOGGER.info("Registering Items for " + OreOh.MOD_ID);

        addItemToTab(CreativeModeTabs.COMBAT, ModItems.GOLDEN_CUDGEL, Items.MACE);
    }
}
