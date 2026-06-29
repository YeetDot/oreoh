package com.yeetdot.oreoh.block.entity;

import com.mojang.serialization.Codec;
import com.yeetdot.oreoh.menu.EnergyMenu;
import com.yeetdot.oreoh.network.EnergySyncPayload;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.EnumMap;
import java.util.Objects;

public abstract class AbstractEnergyContainerBlockEntity extends BaseContainerBlockEntity implements ExtendedMenuProvider<BlockPos> {
    protected final SimpleEnergyStorage energyStorage;
    protected long lastSyncedEnergyAmount = -1;
    protected long lastSyncedEnergyCapacity = -1;
    private EnumMap<Direction, SideEnergyMode> sideEnergyModes = new EnumMap<>(Direction.class);
    public static final Codec<EnumMap<Direction, SideEnergyMode>> SIDE_ENERGY_MAP_CODEC =
            Codec.unboundedMap(Direction.CODEC, SideEnergyMode.CODEC).xmap(
                    map -> {
                        EnumMap<Direction, SideEnergyMode> enumMap = new EnumMap<>(Direction.class);
                        enumMap.putAll(map);
                        return enumMap;
                    },
                    map -> map
            );
    
    protected AbstractEnergyContainerBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, long capacity, long insertPerTick, long extractPerTick) {
        super(type, worldPosition, blockState);
        energyStorage = new SimpleEnergyStorage(capacity, insertPerTick, extractPerTick);
        for (Direction direction : Direction.values()) {
            sideEnergyModes.put(direction, SideEnergyMode.IN_OUT);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        energyStorage.amount = input.getLongOr("energy", 0L);
        sideEnergyModes = input.read("side_energy_modes", SIDE_ENERGY_MAP_CODEC).orElseGet(() -> {
            EnumMap<Direction, SideEnergyMode> enumMap = new EnumMap<>(Direction.class);
            for (Direction direction : Direction.values()) {
                enumMap.put(direction, SideEnergyMode.IN_OUT);
            }
            return enumMap;
        });
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putLong("energy", energyStorage.getAmount());
        output.store("energy_side_modes", SIDE_ENERGY_MAP_CODEC, this.sideEnergyModes);
    }

    @Override
    public  BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.getBlockPos();
    }
    
    public EnergyStorage getEnergyStorage() {
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

    public boolean canInsertEnergy(Direction direction) {
        return switch(sideEnergyModes.get(direction)) {
            case IN, IN_OUT -> true;
            case NONE, OUT -> false;
        };
    }
    
    public boolean canExtractEnergy(Direction direction) {
        return switch(sideEnergyModes.get(direction)) {
            case OUT, IN_OUT -> true;
            case NONE, IN -> false;
        };
    }
    
    public SideEnergyMode getSideEnergyMode(Direction direction) {
        return sideEnergyModes.get(direction);
    }
    
    public void changeSideEnergyMode(Direction direction, SideEnergyMode sideEnergyMode) {
        this.sideEnergyModes.put(direction, sideEnergyMode);
        setChanged();
    }
}
