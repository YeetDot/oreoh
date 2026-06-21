package com.yeetdot.oreoh.item.set;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

public class MetalSet extends Set {
    public final Item INGOT;
    public final Item NUGGET;
    public final Block BLOCK;
    public final Block RAW_BLOCK;
    public final OreSet ORES;

    public static final List<MetalSet> METAL_SETS = new ArrayList<>();

    public MetalSet(String name, String hardness) {
        INGOT = item(String.format("%s_ingot", name));
        NUGGET = item(String.format("%s_nugget", name));
        BLOCK = block(String.format("%s_block", name), BlockBehaviour.Properties.of().strength(3.0f, 3.0f).sound(SoundType.METAL));
        RAW_BLOCK = block(String.format("raw_%s_block", name), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(3.0f, 3.0f));
        ORES = new OreSet(name, hardness);

        METAL_SETS.add(this);
    }
}
