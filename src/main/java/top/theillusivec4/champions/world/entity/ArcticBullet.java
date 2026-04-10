package top.theillusivec4.champions.world.entity;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.affix.AffixHelper;

public class ArcticBullet extends ShulkerBullet {
  public ArcticBullet(Level level, LivingEntity owner, Entity target, Direction.Axis invalidStartAxis) {
    super(level, owner, target, invalidStartAxis);
  }

  public ArcticBullet(EntityType<? extends ArcticBullet> type, Level level) {
    super(type, level);
  }

  public ArcticBullet(Level level, Entity owner) {
    this(ChampionsEntityTypes.ARCTIC_BULLET.get(), level);
    this.setOwner(owner);
    Vec3 position = owner.getBoundingBox().getCenter();
    this.moveTo(position.x, position.y, position.z, this.getYRot(), this.getXRot());
    this.finalTarget = owner instanceof Mob mob ? mob.getTarget() : null;
    this.currentMoveDirection = Direction.UP;
    this.selectNextMoveDirection(owner.getDirection().getAxis());
  }

  @Override
  protected void onHitEntity(EntityHitResult hitResult) {
    Entity target = hitResult.getEntity();
    Entity owner = this.getOwner();
    LivingEntity livingOwner = owner instanceof LivingEntity ? (LivingEntity) owner : null;
    DamageSource damageSource = this.damageSources().mobProjectile(this, livingOwner);
    boolean wasHurt = target.hurt(damageSource, 0.0F);
    if (wasHurt) {
      if (this.level() instanceof ServerLevel level) {
        EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
        // Affix
        Entity victim = hitResult.getEntity();
	      AffixHelper.doPostAttackEffects(level, victim, damageSource);
      }

      if (target instanceof LivingEntity livingTarget) {
        livingTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
        livingTarget.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2));
      }
    }
  }
}
