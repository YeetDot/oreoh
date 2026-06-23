package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.set.id.MetalId;
import com.yeetdot.oreoh.set.tag.MetalTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

public final class MetalSet extends NaturalSet {
    public final Item nugget;
    public final Item raw;
    public final Block rawBlock;
    public final MetalTag.Set tagSet;
    public final MetalId.Set idSet;

    public static final List<MetalSet> SETS = new ArrayList<>();

    public MetalSet(String name, String hardness) {
        super(name, hardness);
        idSet = new MetalId.Set(name);
        nugget = ModItems.register(idSet.nugget());
        raw = ModItems.register(idSet.rawItem());
        rawBlock = ModBlocks.registerBlock(idSet.rawBlock(), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(5.0f, 6.0f).requiresCorrectToolForDrops());
        tagSet = new MetalTag.Set(name);

        SETS.add(this);
    }

    @Override
    public boolean isIngot() {
        return true;
    }
}
