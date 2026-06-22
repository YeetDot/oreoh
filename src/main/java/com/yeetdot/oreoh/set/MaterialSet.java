package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.block.ModBlocks;
import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.set.id.MaterialIdSet;
import com.yeetdot.oreoh.set.tag.MaterialTagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

public abstract class MaterialSet {
    public final Item primary;
    public final Block storageBlock;
    public final MaterialIdSet idSet;
    public final MaterialTagSet tagSet;
    private final String name;
    private final String hardness;

    public static final List<MaterialSet> SETS = new ArrayList<>();

    public MaterialSet(String name, String hardness) {
        idSet = new MaterialIdSet(name);
        tagSet = new MaterialTagSet(name);
        primary = ModItems.register(idSet.primary());
        storageBlock = ModBlocks.registerBlock(idSet.storageBlock(), Block::new, BlockBehaviour.Properties.of().strength(5.0f, 6.0f).sound(SoundType.METAL).requiresCorrectToolForDrops());

        this.name = name;
        this.hardness = hardness;
        SETS.add(this);
    }

    public String name() { return name; }

    public String hardness() { return hardness; }
}
