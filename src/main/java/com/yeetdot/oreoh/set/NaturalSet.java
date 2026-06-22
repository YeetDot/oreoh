package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.set.id.NaturalIdSet;
import com.yeetdot.oreoh.set.tag.NaturalTagSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

public class NaturalSet extends MaterialSet {
    public final Block oreStone;
    public final Block oreDeepslate;
    public final Block oreNether;
    public final Block oreEnd;
    public final NaturalIdSet idSet;
    public final NaturalTagSet tagSet;

    public static final List<NaturalSet> SETS = new ArrayList<>();

    public NaturalSet(String name, String hardness) {
        super(name, hardness);
        idSet = new NaturalIdSet(name);
        tagSet = new NaturalTagSet(name);
        oreStone = ModBlocks.registerBlock(idSet.oreStone(), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
        oreDeepslate = ModBlocks.registerBlock(idSet.oreDeepslate(), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
        oreNether = ModBlocks.registerBlock(idSet.oreNether(), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
        oreEnd = ModBlocks.registerBlock(idSet.oreEnd(), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE).strength(3.0F, 9.0F));

        SETS.add(this);
    }
}
