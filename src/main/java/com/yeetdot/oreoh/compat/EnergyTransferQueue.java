package com.yeetdot.oreoh.compat;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

import java.util.ArrayList;
import java.util.List;

public final class EnergyTransferQueue {
    private final List<PendingTransfer> transfers = new ArrayList<>();

    public void add(EnergyStorage source, EnergyStorage target, long maxAmount) {
        transfers.add(new PendingTransfer(source, target, maxAmount));
    }

    public void flush() {
        for (PendingTransfer transfer : transfers) {
            try (Transaction tx = Transaction.openOuter()) {
                EnergyStorageUtil.move(
                        transfer.source(),
                        transfer.target(),
                        transfer.amount(),
                        tx
                );
                tx.commit();
            }
        }
        transfers.clear();
    }
}