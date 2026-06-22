package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.set.id.MetalIdSet;
import com.yeetdot.oreoh.set.tag.MetalTagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

public class MetalSet extends NaturalSet {
    public final Item nugget;
    public final Item raw;
    public final Block rawBlock;
    public final MetalTagSet tagSet;
    public final MetalIdSet idSet;

    public static final List<MetalSet> SETS = new ArrayList<>();

    public MetalSet(String name, String hardness) {
        super(name, hardness);
        idSet = new MetalIdSet(name);
        nugget = ModItems.register(idSet.nugget());
        raw = ModItems.register(idSet.rawItem());
        rawBlock = ModBlocks.registerBlock(idSet.rawBlock(), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(5.0f, 6.0f).requiresCorrectToolForDrops());
        tagSet = new MetalTagSet(name);

        SETS.add(this);
    }
}
