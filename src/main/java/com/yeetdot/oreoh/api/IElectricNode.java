package com.yeetdot.oreoh.api;

public interface IElectricNode {
    double getRequestedWatts();
    double getBaseInductance();
    boolean isMachineActive();
}
