package com.yeetdot.oreoh.energy;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class WireBlockEntity extends BlockEntity {
    private @Nullable ElectricGrid grid;
    private double temperature = 20.0;
    private double ambientTemperature = 20.0; 
    
    private final ElectricalProperties electrical;
    private final ThermalProperties thermal;
    
    
    public WireBlockEntity(BlockPos worldPosition, BlockState blockState, ElectricalProperties electrical, ThermalProperties thermal) {
        super(ModBlockEntities.WIRE_BLOCK_ENTITY, worldPosition, blockState);
        this.electrical = electrical;
        this.thermal = thermal;
    }
    
    public static void serverTick(ServerLevel level, WireBlockEntity wire) {
        wire.tickSimulation(level);
    }
    
    public void tickSimulation(ServerLevel level) {
        if (this.grid == null) {
            cool();
            return;
        }
        applyHeating(this.grid);
        cool();
        checkFailure(level);
        OreOh.LOGGER.info(String.valueOf(this.grid == null));
    }
    
    private void cool() {
        this.temperature -= (temperature - ambientTemperature) * thermal.coolingRate();
        
        if (temperature < ambientTemperature) this.temperature = ambientTemperature;
    }
    
    private void checkFailure(ServerLevel serverLevel) {
        if (temperature >= thermal.insulationFailureTemp()) {
            serverLevel.destroyBlock(worldPosition, false);
        }
    }
    
    private void applyHeating(ElectricGrid grid) {
        double I = grid.getCurrent();
        double R = electrical.resistance();
        
        double powerLoss = I * I * R;
        
        double heatGain = powerLoss / thermal.thermalMass();
        
        this.temperature += heatGain;
    }

    public @Nullable ElectricGrid getGrid() {
        return grid;
    }
    
    public void setGrid(@Nullable ElectricGrid newGrid) {
        ElectricGrid oldGrid = this.grid;
        if (oldGrid != null && oldGrid != newGrid) {
            oldGrid.decrementWireCount();
        }

        this.grid = newGrid;

        if (newGrid != null && oldGrid != newGrid) {
            newGrid.incrementWireCount();
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putDouble("temperature", this.temperature);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.temperature = input.getDoubleOr("temperature", 20.0);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        
        ElectricGrid currentGrid = this.grid;
        if (currentGrid != null) {
            tag.putDouble("grid_voltage", currentGrid.getVoltage());
            tag.putDouble("grid_pf", currentGrid.getCurrentPowerFactor());
        }
        
        return tag;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        ElectricGrid currentGrid = this.getGrid();
        if (currentGrid != null) {
            currentGrid.decrementWireCount();
        }
        super.preRemoveSideEffects(pos, state);
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            WireNetworkHelper.onWireRemoved(serverLevel, pos, this);
        }
    }

    public void setAmbientTemperature(Level level) {
        if (!level.hasBiomes()) return;
        Holder<Biome> biomeHolder = level.getBiomeFabric(this.worldPosition);
        float rawTemperature = biomeHolder.value().getBaseTemperature();
        OreOh.LOGGER.info("Raw temperature at {}: {}", worldPosition, rawTemperature);
        float ambientTemperature = rawTemperature * 20.0f;
        if (biomeHolder.is(BiomeTags.IS_NETHER)) {
            ambientTemperature = 80.0f;
        } else if (biomeHolder.is(BiomeTags.IS_END)) {
            ambientTemperature = -15.0f;
        }
        OreOh.LOGGER.info("Ambient temperature at {}: {}", worldPosition, (double) ambientTemperature);
        this.ambientTemperature = ambientTemperature;
    }
}