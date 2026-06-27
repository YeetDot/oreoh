package com.yeetdot.oreoh.item;

import com.yeetdot.oreoh.block.entity.AbstractEnergyContainerBlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class WrenchItem extends Item {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (level.getBlockEntity(context.getClickedPos()) instanceof AbstractEnergyContainerBlockEntity entity) {
            if (!level.isClientSide()) {
                entity.forceZeroEnergy();
            }
            
            if (player != null) {
                player.swing(context.getHand());
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
}
