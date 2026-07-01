package com.yeetdot.oreoh.api;

import com.yeetdot.oreoh.energy.ElectricGrid;
import org.jspecify.annotations.Nullable;

public interface IElectricNode {
    double getRequestedWatts();
    double getBaseInductance();
    boolean isMachineActive();
    void updateLinkedGrid(@Nullable ElectricGrid grid);
}
