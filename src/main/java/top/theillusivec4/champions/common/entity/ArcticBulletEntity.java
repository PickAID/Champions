package top.theillusivec4.champions.common.entity;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import top.theillusivec4.champions.common.registry.ModEntityTypes;

import javax.annotation.Nonnull;

public class ArcticBulletEntity extends BaseBulletEntity {

    public ArcticBulletEntity(Level level) {
        super(ModEntityTypes.ARCTIC_BULLET.get(), level);
    }

    public ArcticBulletEntity(Level level, LivingEntity livingEntity, @Nonnull Entity entity, Direction.Axis axis) {
        super(ModEntityTypes.ARCTIC_BULLET.get(), level, livingEntity, entity, axis);
    }

    public ArcticBulletEntity(EntityType<? extends ArcticBulletEntity> entityEntityType, Level level) {
        super(entityEntityType, level);
    }

    @Override
    protected void bulletEffect(LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
        target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2));
    }

    @Override
    protected ParticleOptions getParticle() {
        return ParticleTypes.ITEM_SNOWBALL;
    }
}
