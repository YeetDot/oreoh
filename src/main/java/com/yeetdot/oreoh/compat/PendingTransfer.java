package com.yeetdot.oreoh.compat;

import team.reborn.energy.api.EnergyStorage;

public record PendingTransfer(EnergyStorage source, EnergyStorage target, long amount) {
}
