package com.yeetdot.oreoh.energy;

import com.yeetdot.oreoh.OreOh;
import com.yeetdot.oreoh.block.entity.ModBlockEntities;
import com.yeetdot.oreoh.data.ElectricGridRegistry;
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

public class WireBlockEntity extends BlockEntity {
    private double temperature = 20.0;
    private double ambientTemperature = 20.0; 
    
    private final ElectricalProperties electrical;
    private final ThermalProperties thermal;
    
    
    public WireBlockEntity(BlockPos worldPosition, BlockState blockState, ElectricalProperties electrical, ThermalProperties thermal) {
        super(ModBlockEntities.WIRE_BLOCK_ENTITY, worldPosition, blockState);
        this.electrical = electrical;
        this.thermal = thermal;
    }
    
    public static void serverTick(ServerLevel level, BlockPos pos, WireBlockEntity wire) {
        wire.tickSimulation(level, pos);
    }
    
    public void tickSimulation(ServerLevel level, BlockPos pos) {
        ElectricGrid grid = ElectricGridRegistry.get(level).getGrid(pos);

        if (grid == null) {
            cool();
            return;
        }
        applyHeating(grid);
        cool();
        checkFailure(level);
        OreOh.LOGGER.info("Grid voltage: {}", grid.getVoltage());
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

//        OreOh.LOGGER.info("Power loss: {}, Heat gain: {}", powerLoss, heatGain);
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
        ElectricGridRegistry registry = this.getLevel() instanceof ServerLevel serverLevel ? ElectricGridRegistry.get(serverLevel) : null;
        ElectricGrid grid = null;
        if (registry != null) {
            grid = registry.getGrid(getBlockPos());
        }
        if (grid != null) {
            tag.putDouble("grid_voltage", grid.getVoltage());
            tag.putDouble("grid_pf", grid.getCurrentPowerFactor());
        }
        
        return tag;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);

        if (!level.isClientSide()) {
            setAmbientTemperature(level);
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            ElectricGridRegistry.get(serverLevel).registerRemoval(serverLevel, pos);
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