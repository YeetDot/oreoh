package com.yeetdot.oreoh.item.set;

import com.yeetdot.oreoh.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

public class OreSet extends Set {
    public final Block ORE;
    public final Block DEEPSLATE_ORE;
    public final String hardness;

    public static final List<OreSet> ORE_SETS = new ArrayList<>();

    public OreSet(String name, String hardness) {
        ORE = ModBlocks.registerBlock(String.format("%s_ore", name), Block::new);
        DEEPSLATE_ORE = ModBlocks.registerBlock(String.format("deepslate_%s_ore", name), Block::new);
        this.hardness = hardness;

        ORE_SETS.add(this);
    }
}
