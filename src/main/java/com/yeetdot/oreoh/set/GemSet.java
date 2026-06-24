package com.yeetdot.oreoh.set;

import com.yeetdot.oreoh.set.id.GemId;
import com.yeetdot.oreoh.set.tag.GemTag;

import java.util.ArrayList;
import java.util.List;

public final class GemSet extends NaturalSet{
    public final GemId idSet;
    public final GemTag tagSet;

    public static final List<GemSet> SETS =  new ArrayList<>();

    public GemSet(String name, String hardness, int veinSize, int frequency, int minY, int maxY, boolean placeInOverworld, boolean placeInNether, boolean placeInEnd, boolean isTrapezoidal) {
        super(name, hardness, veinSize, frequency, minY, maxY, placeInOverworld, placeInNether, placeInEnd, isTrapezoidal);

        idSet = new GemId.Set(name);
        tagSet = new GemTag.Set(name);

        SETS.add(this);
    }

    @Override
    public boolean isIngot() {
        return false;
    }
}
