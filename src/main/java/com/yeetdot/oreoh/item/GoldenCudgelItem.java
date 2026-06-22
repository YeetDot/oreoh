package com.yeetdot.oreoh.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.phys.Vec3;

public class GoldenCudgelItem extends Item {
    public GoldenCudgelItem(Properties properties) {
        super(properties
                .sword(ToolMaterial.GOLD, 887, -2)
                .component(DataComponents.ATTACK_RANGE, new AttackRange(0, 666, 0, 666, 10, 1)));
    }

    @Override
    public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
        super.hurtEnemy(itemStack, mob, attacker);
        if (attacker instanceof Player player && !attacker.level().isClientSide()) {
            ServerLevel world = (ServerLevel) attacker.level();
            world.sendParticles(ParticleTypes.GLOW, mob.getX(), mob.getY(), mob.getZ(), (int) Math.ceil(mob.getMaxHealth())*10, 0.5, 1, 0.5, 1.5);
            Vec3 start = player.getEyePosition().subtract(0, 0.3, 0);
            Vec3 end = mob.getBoundingBox().getCenter();
            Vec3 direction = end.subtract(start);
            double distance = direction.length();
            Vec3 normalizedDirection = direction.normalize();
            double stepSize = 0.5;
            for (double currentStep = 0.0; currentStep <= distance; currentStep += stepSize) {
                Vec3 particlePos = start.add(normalizedDirection.scale(currentStep));
                world.sendParticles(
                        ParticleTypes.END_ROD,
                        particlePos.x,
                        particlePos.y,
                        particlePos.z,
                        0,
                        normalizedDirection.x,
                        normalizedDirection.y,
                        normalizedDirection.z,
                        0
                );
            }
        }
    }
}
