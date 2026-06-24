package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.set.id.NaturalId;
import com.yeetdot.oreoh.set.tag.NaturalTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

public abstract sealed class NaturalSet extends MaterialSet permits MetalSet, GemSet {
    public final Block oreStone;
    public final Block oreDeepslate;
    public final Block oreNether;
    public final Block oreEnd;
    public final NaturalId.Set idSet;
    public final NaturalTag.Set tagSet;
    public final int veinSize;
    public final int frequency;
    public final int minY;
    public final int maxY;
    public final boolean placeInOverworld;
    public final boolean placeInNether;
    public final boolean placeInEnd;
    public final boolean isTrapezoidal;

    public static final List<NaturalSet> SETS = new ArrayList<>();

    public NaturalSet(String name, String hardness, int veinSize, int frequency, int minY, int maxY, boolean placeInOverworld, boolean placeInNether, boolean placeInEnd, boolean isTrapezoidal) {
        super(name, hardness);

        idSet = new NaturalId.Set(name, isIngot());
        tagSet = new NaturalTag.Set(name, isIngot());

        this.veinSize = veinSize;
        this.frequency = frequency;
        this.minY = minY;
        this.maxY = maxY;
        this.placeInOverworld = placeInOverworld;
        this.placeInNether = placeInNether;
        this.placeInEnd = placeInEnd;
        this.isTrapezoidal = isTrapezoidal;

        oreStone = ModBlocks.registerBlock(idSet.oreStone(), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE));
        oreDeepslate = ModBlocks.registerBlock(idSet.oreDeepslate(), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
        oreNether = ModBlocks.registerBlock(idSet.oreNether(), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE));
        oreEnd = ModBlocks.registerBlock(idSet.oreEnd(), Block::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE).strength(3.0F, 9.0F));

        SETS.add(this);
    }
}
