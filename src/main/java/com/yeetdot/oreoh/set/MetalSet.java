package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.set.id.MetalIdSet;
import com.yeetdot.oreoh.set.tag.MetalTagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

public class MetalSet {
    public final Item INGOT;
    public final Item NUGGET;
    public final Item RAW;
    public final Block BLOCK;
    public final Block RAW_BLOCK;
    public final OreSet ORES;
    public final MetalTagSet TAGS;
    public final MetalIdSet IDS;
    private final String name;
    public final String hardness;

    public static final List<MetalSet> METAL_SETS = new ArrayList<>();

    public MetalSet(String name, String hardness) {
        IDS = new MetalIdSet(name);
        INGOT = ModItems.register(IDS.ingot());
        NUGGET = ModItems.register(IDS.nugget());
        RAW = ModItems.register(IDS.raw());
        BLOCK = ModBlocks.registerBlock(String.format("%s_block", name), Block::new, BlockBehaviour.Properties.of().strength(5.0f, 6.0f).sound(SoundType.METAL).requiresCorrectToolForDrops());
        RAW_BLOCK = ModBlocks.registerBlock(String.format("raw_%s_block", name), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(5.0f, 6.0f).requiresCorrectToolForDrops());
        ORES = new OreSet(name, hardness);
        TAGS = new MetalTagSet(name);

        this.name = name;
        this.hardness = hardness;

        METAL_SETS.add(this);
    }

    public String name() {
        return name;
    }
}
