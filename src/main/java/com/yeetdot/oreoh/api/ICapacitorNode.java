package com.yeetdot.oreoh.api;

import com.yeetdot.oreoh.energy.ElectricGrid;
import org.jspecify.annotations.Nullable;

public interface ICapacitorNode {
    double getCapacitanceRating();
    void updateLinkedGrid(@Nullable ElectricGrid grid);
}
