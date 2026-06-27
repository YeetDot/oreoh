package com.yeetdot.oreoh.block.entity;

import com.yeetdot.oreoh.menu.EnergyMenu;
import com.yeetdot.oreoh.network.EnergySyncPayload;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Objects;

public abstract class AbstractEnergyContainerBlockEntity extends BaseContainerBlockEntity implements ExtendedMenuProvider<BlockPos> {
    protected SimpleEnergyStorage energyStorage;
    protected long lastSyncedEnergyAmount = -1;
    protected long lastSyncedEnergyCapacity = -1;
    
    
    protected AbstractEnergyContainerBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, long capacity, long insertPerTick, long extractPerTick) {
        super(type, worldPosition, blockState);
        energyStorage = new SimpleEnergyStorage(capacity, insertPerTick, extractPerTick);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        energyStorage.amount = input.getLongOr("energy", 0L);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.putLong("energy", energyStorage.getAmount());
    }

    @Override
    public @NonNull BlockPos getScreenOpeningData(@NonNull ServerPlayer player) {
        return this.getBlockPos();
    }
    
    public SimpleEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public void setLastSyncedEnergyToInvalid() {
        this.lastSyncedEnergyAmount = -1;
        this.lastSyncedEnergyCapacity = -1;
    }

    public void syncEnergyToOpenMenus(Level level) {
        if (level.isClientSide()) return;

        long currentAmount = this.energyStorage.getAmount();
        long currentCapacity = this.energyStorage.getCapacity();

        for (var player : Objects.requireNonNull(level.getServer()).getPlayerList().getPlayers()) {
            if (player.containerMenu instanceof EnergyMenu energyMenu) {
                if (energyMenu.getBlockPos().equals(this.getBlockPos())) {
                    if (currentAmount != this.lastSyncedEnergyAmount || currentCapacity != this.lastSyncedEnergyCapacity) {
                        ServerPlayNetworking.send(
                                player,
                                new EnergySyncPayload(this.getBlockPos(), currentAmount, currentCapacity)
                        );
                    }
                }
            }
        }
        this.lastSyncedEnergyAmount = currentAmount;
        this.lastSyncedEnergyCapacity = currentCapacity;
    }
    
    public void forceZeroEnergy() {
        this.energyStorage.amount = 0;
        setChanged();
    }

}
