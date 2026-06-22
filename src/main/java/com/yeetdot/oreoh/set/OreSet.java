package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.set.id.OreIdSet;
import com.yeetdot.oreoh.set.tag.OreTagSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

public class OreSet {
    public final Block ORE;
    public final Block DEEPSLATE_ORE;
    public final OreTagSet TAGS;
    public final OreIdSet IDS;
    public final String hardness;

    public static final List<OreSet> ORE_SETS = new ArrayList<>();

    public OreSet(String name, String hardness) {
        ORE = ModBlocks.registerBlock(String.format("%s_ore", name), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
        DEEPSLATE_ORE = ModBlocks.registerBlock(String.format("deepslate_%s_ore", name), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
        TAGS = new OreTagSet(name);
        IDS = new OreIdSet(name);

        this.hardness = hardness;

        ORE_SETS.add(this);
    }
}
