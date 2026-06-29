package com.yeetdot.oreoh.item;

import com.yeetdot.oreoh.block.entity.AbstractEnergyContainerBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class WrenchItem extends Item {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (!(level.getBlockEntity(context.getClickedPos()) instanceof AbstractEnergyContainerBlockEntity entity)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            handleServer(context, entity, player);
        }

        return InteractionResult.CONSUME;
    }

    private void handleServer(UseOnContext context,
                              AbstractEnergyContainerBlockEntity entity,
                              @Nullable Player player) {

        Direction face = context.getClickedFace();

        boolean secondary = context.isSecondaryUseActive();

        var oldMode = entity.getSideEnergyMode(face);

        if (secondary) {
            var newMode = oldMode.next();
            entity.changeSideEnergyMode(face, newMode);

            sendMessage(player, Component.translatable(
                    "item.oreoh.wrench.change_mode",
                    String.valueOf(newMode)
            ));
        } else {
            entity.forceZeroEnergy();

            sendMessage(player, Component.translatable(
                    "item.oreoh.wrench.zero_message",
                    String.format("0 / %,d", entity.getEnergyStorage().getCapacity())
            ));
        }
        
        if (player != null) {
            player.swing(context.getHand());
        }
    }

    private void sendMessage(@Nullable Player player, Component msg) {
        if (player != null) {
            player.sendOverlayMessage(msg);
        }
    }
}
