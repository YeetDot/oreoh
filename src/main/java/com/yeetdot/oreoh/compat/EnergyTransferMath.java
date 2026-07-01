package com.yeetdot.oreoh.compat;

public final class EnergyTransferMath {
    private static final double SMOOTHING = 0.3;
    
    public static long computeSmoothTransfer(long source, long target, long maxTransfer) {
        long diff = source - target;
        if (diff <= 1) return 0;

        long amount = Math.min(
                maxTransfer,
                diff / 2
        );
        
        return Math.min(amount, maxTransfer);
    }
}
