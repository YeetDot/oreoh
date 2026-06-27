package com.yeetdot.oreoh.network;

import com.yeetdot.oreoh.OreOh;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record EnergySyncPayload(BlockPos pos, long energyAmount, long energyCapacity) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<EnergySyncPayload> TYPE = new CustomPacketPayload.Type<>(OreOh.id("energy_sync"));
    
    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, EnergySyncPayload> CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC, EnergySyncPayload::pos,
                    ByteBufCodecs.VAR_LONG, EnergySyncPayload::energyAmount,
                    ByteBufCodecs.VAR_LONG, EnergySyncPayload::energyCapacity,
                    EnergySyncPayload::new
            );
}
