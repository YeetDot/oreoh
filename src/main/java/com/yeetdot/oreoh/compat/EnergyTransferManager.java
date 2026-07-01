package com.yeetdot.oreoh.compat;

import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.WeakHashMap;

public final class EnergyTransferManager {
    private static final Map<ServerLevel, EnergyTransferQueue> QUEUES = new WeakHashMap<>();
    
    public static EnergyTransferQueue get(ServerLevel level) {
        return QUEUES.computeIfAbsent(level, l -> new EnergyTransferQueue());
    }
    
    public static void flush(ServerLevel level) {
        EnergyTransferQueue queue = get(level);
        
        queue.flush();
    }
}