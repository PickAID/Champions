package top.theillusivec4.champions.world.entity.projectile;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.champions.world.entity.ChampionsEntityTypes;

@Deprecated
public class ArcticBulletEntity extends BaseBulletEntity {

  public ArcticBulletEntity(Level level, LivingEntity owner, Entity target, Direction.Axis axis) {
    super(ChampionsEntityTypes.ARCTIC_BULLET.get(), level, owner, target, axis);
  }

  public ArcticBulletEntity(Level level, double x, double y, double z, Entity target, Direction.Axis axis) {
    super(ChampionsEntityTypes.ARCTIC_BULLET.get(), level);
    this.setPos(x, y, z);
  }

  protected ArcticBulletEntity(EntityType<? extends ArcticBulletEntity> entityEntityType, Level level) {
    super(entityEntityType, level);
  }

  @Override
  protected void bulletEffect(LivingEntity target) {
    target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 2));
    target.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 100, 2));
  }

  @Override
  protected ParticleOptions getParticle() {
    return ParticleTypes.ITEM_SNOWBALL;
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.@NotNull Builder pBuilder) {

  }
}
