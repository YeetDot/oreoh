package com.yeetdot.oreoh.item.set;

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
        ORE = block(name, BlockBehaviour.Properties.of());
        DEEPSLATE_ORE = block(name, BlockBehaviour.Properties.of());
        this.hardness = hardness;

        ORE_SETS.add(this);
    }
}
