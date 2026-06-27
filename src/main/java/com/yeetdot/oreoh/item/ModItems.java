package com.yeetdot.oreoh.item;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;

import java.util.function.Function;

public class ModItems {
    public static final Item GOLDEN_CUDGEL = register("golden_cudgel", GoldenCudgelItem::new);
    public static final Item WRENCH = register("wrench", WrenchItem::new);

    public static Item register(String name) {
        return register(name, Item::new);
    }

    public static Item register(ResourceKey<Item> id) {
        return register(id, Item::new);
    }

    private static Item register(String name, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, OreOh.id(name),
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, OreOh.id(name)))));
    }

    private static Item register(ResourceKey<Item> id, Function<Item.Properties, Item> function) {
        return Registry.register(BuiltInRegistries.ITEM, id, function.apply(new Item.Properties().setId(id)));
    }

    public static void registerItems() {
        OreOh.LOGGER.info("Registering Items for " + OreOh.MOD_ID);

    }
}
