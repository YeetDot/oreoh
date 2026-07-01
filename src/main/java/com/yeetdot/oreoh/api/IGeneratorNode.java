package com.yeetdot.oreoh.api;

import com.yeetdot.oreoh.energy.ElectricGrid;
import org.jspecify.annotations.Nullable;

public interface IGeneratorNode {
    double getAvailableWatts();
    double getVoltage();
    void updateLinkedGrid(@Nullable ElectricGrid grid);
}
