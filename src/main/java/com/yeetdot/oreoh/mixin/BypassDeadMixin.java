package com.yeetdot.oreoh.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.yeetdot.oreoh.item.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.AttackRange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(AttackRange.class)
public class BypassDeadMixin {
    @ModifyVariable(
            method = "getClosesetHit",
            at = @At("HEAD"),
            argsOnly = true, name = "matching")
    private Predicate<Entity> purgeDeadFromLoopList(Predicate<Entity> matching, @Local(name = "attacker", argsOnly = true) Entity attacker) {
        // 1. Isolate the target loop strictly to a player holding your 4x giant weapon
        if (attacker instanceof Player player && player.getMainHandItem().is(ModItems.GOLDEN_CUDGEL)) {

            // 2. Wrap the filter to bypass dead enemies for your weapon exclusively
            return entity -> matching.test(entity) &&
                    (!(entity instanceof LivingEntity living) || (living.isAlive() && living.deathTime <= 0));
        }

        // Returns standard vanilla scanning behaviors if holding any other item/axe/sword
        return matching;
    }
}
