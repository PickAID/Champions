package top.theillusivec4.champions.world.entity;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.champion.ChampionHelper;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;

public class ArcticBullet extends ShulkerBullet {
  public ArcticBullet(Level level, LivingEntity owner, Entity target, Direction.Axis invalidStartAxis) {
    super(level, owner, target, invalidStartAxis);
  }

  public ArcticBullet(EntityType<? extends ArcticBullet> type, Level level) {
    super(type, level);
  }

  public ArcticBullet(Level level, Entity owner) {
    this(EntityTypes.ARCTIC_BULLET.get(), level);
    this.setOwner(owner);
    Vec3 position = owner.getBoundingBox().getCenter();
    this.snapTo(position.x, position.y, position.z, this.getYRot(), this.getXRot());
    Entity target = owner instanceof Mob mob ? mob.getTarget() : null;
    this.finalTarget = EntityReference.of(target);
    this.currentMoveDirection = Direction.UP;
    this.selectNextMoveDirection(owner.getDirection().getAxis(), target);
  }

  @Override
  protected void onHitEntity(EntityHitResult hitResult) {
    Entity target = hitResult.getEntity();
    Entity owner = this.getOwner();
    LivingEntity livingOwner = owner instanceof LivingEntity ? (LivingEntity) owner : null;
    DamageSource damageSource = this.damageSources().mobProjectile(this, livingOwner);
    @SuppressWarnings("deprecation")
    boolean wasHurt = target.hurtOrSimulate(damageSource, 0.0F);
    if (wasHurt) {
      if (this.level() instanceof ServerLevel level) {
        EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
        // Affix
        Entity victim = hitResult.getEntity();
	      ChampionHelper.doPostAttackEffects(level, victim, damageSource);
//        ChampionUtil.getHandler(victim).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.VICTIM, victim, damageSource));
//        Entity attacker = this.getOwner();
//        if (attacker != null) {
//          ChampionUtil.getHandler(attacker).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.ATTACKER, victim, damageSource));
//        }
//        ChampionUtil.getHandler(this).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.DAMAGING_ENTITY, victim, damageSource));
      }

      if (target instanceof LivingEntity livingTarget) {
        livingTarget.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 2));
        livingTarget.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 100, 2));
      }
    }
  }
}
