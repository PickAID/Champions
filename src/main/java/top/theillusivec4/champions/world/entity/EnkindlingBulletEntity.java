package top.theillusivec4.champions.world.entity;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import top.theillusivec4.champions.deprecated.common.damagesource.DamageTypes;

import javax.annotation.Nonnull;

public class EnkindlingBulletEntity extends BaseBulletEntity {

  public EnkindlingBulletEntity(Level level, LivingEntity livingEntity, @Nonnull Entity entity,
                                Direction.Axis axis) {
    super(EntityTypes.ENKINDLING_BULLET.get(), level, livingEntity, entity, axis);
  }

  public EnkindlingBulletEntity(EntityType<? extends EnkindlingBulletEntity> enkindlingBulletEntityEntityType, Level level) {
    super(enkindlingBulletEntityEntityType, level);
  }

  @Override
  protected void bulletEffect(LivingEntity target) {

    if (this.getOwner() != null) {
      target.hurt(DamageTypes.of(DamageTypes.ENKINDLING_BULLET, this, this.getOwner()), 1);
    } else {
      target.hurt(DamageTypes.of(DamageTypes.ENKINDLING_BULLET, this), 1);
    }
    target.setRemainingFireTicks(8 * 20);
  }

  @Override
  protected ParticleOptions getParticle() {
    return ParticleTypes.FLAME;
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {

  }
}
