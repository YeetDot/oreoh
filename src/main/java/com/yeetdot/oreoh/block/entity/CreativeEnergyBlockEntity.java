package com.yeetdot.oreoh.block.entity;

import com.yeetdot.oreoh.block.EnergyStorageStats;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeEnergyBlockEntity extends BatteryBlockEntity {
    public CreativeEnergyBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntities.CREATIVE_ENERGY_BLOCK_ENTITY, worldPosition, blockState, new EnergyStorageStats(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE));
    }

    public static void serverTick(ServerLevel level, BlockPos pos, CreativeEnergyBlockEntity entity) {
        BatteryBlockEntity.serverTick(level, pos, entity);
        try (Transaction transaction = Transaction.openOuter()) {
            entity.energyStorage.insert(Long.MAX_VALUE/100, transaction);
            transaction.commit();
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.oreoh.creative_energy_block_entity");
    }
}
