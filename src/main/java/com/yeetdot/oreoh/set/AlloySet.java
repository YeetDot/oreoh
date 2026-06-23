package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.item.ModItems;
import com.yeetdot.oreoh.set.id.AlloyId;
import com.yeetdot.oreoh.set.tag.AlloyTag;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public final class AlloySet extends MaterialSet {
    public final AlloyId.Set idSet;
    public final AlloyTag.Set tagSet;
    public final Item nugget;

    public static final List<AlloySet> SETS = new ArrayList<>();

    public AlloySet(String name, String hardness) {
        super(name, hardness);
        idSet = new AlloyId.Set(name);
        tagSet = new AlloyTag.Set(name);
        nugget = ModItems.register(idSet.nugget());

        SETS.add(this);
    }

    @Override
    public boolean isIngot() {
        return true;
    }
}
