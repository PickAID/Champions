package top.theillusivec4.champions.world.entity;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
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
import top.theillusivec4.champions.world.damage.ChampionsDamageTypes;

public class EnkindlingBullet extends ShulkerBullet {
  public EnkindlingBullet(Level level, LivingEntity owner, Entity target, Direction.Axis invalidStartAxis) {
    super(level, owner, target, invalidStartAxis);
  }

  public EnkindlingBullet(EntityType<? extends EnkindlingBullet> type, Level level) {
    super(type, level);
  }

  public EnkindlingBullet(Level level, Entity owner) {
    super(ChampionsEntityTypes.ENKINDLING_BULLET.get(), level);
    this.setOwner(owner);
    Vec3 position = owner.getBoundingBox().getCenter();
    this.moveTo(position.x, position.y, position.z, this.getYRot(), this.getXRot());
    Entity target = owner instanceof Mob mob ? mob.getTarget() : null;
    this.finalTarget = target;
    this.currentMoveDirection = Direction.UP;
    this.selectNextMoveDirection(owner.getDirection().getAxis());
  }

  @Override
  protected void onHitEntity(EntityHitResult hitResult) {
    Entity target = hitResult.getEntity();
    Entity owner = this.getOwner();
    LivingEntity livingOwner = owner instanceof LivingEntity ? (LivingEntity) owner : null;
    DamageSource damageSource = new DamageSource(this.level().registryAccess().holderOrThrow(ChampionsDamageTypes.ENKINDLING_BULLET), this.getOwner(), this);
    boolean wasHurt = target.hurt(damageSource, 1.0F);
    if (wasHurt) {
      if (this.level() instanceof ServerLevel level) {
        EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
        // Affix
        Entity victim = hitResult.getEntity();
        AffixHelper.doPostAttackEffects(level, victim, damageSource);

        target.setRemainingFireTicks(8 * 20);
      }
    }
  }
}
