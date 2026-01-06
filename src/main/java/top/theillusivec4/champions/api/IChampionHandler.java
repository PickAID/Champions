package top.theillusivec4.champions.api;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.LatestDamage;
import top.theillusivec4.champions.api.affix.effect.AffixTarget;

import java.util.function.Consumer;

public interface IChampionHandler {
  void runInitializeEffects(ServerLevel serverLevel, int level, Entity victim, Vec3 origin);

  void stopInitializeEffects(ServerLevel serverLevel, int level, Entity victim, Vec3 origin);

  boolean isImmuneToDamage(ServerLevel serverLevel, DamageSource damageSource);

  float getDamageProtection(ServerLevel level, DamageSource source);

  float modifyKnockback(ServerLevel level, DamageSource source, float knockback);

  float modifyDamage(ServerLevel level, Entity victim, DamageSource damageSource, float amount);

  float modifyHeal(ServerLevel level, float amount);

  void doPostAttackEffects(ServerLevel level, AffixTarget target, Entity victim, DamageSource source);

  void tickEffects(ServerLevel level);

  void runIteration(Consumer<Holder<Affix>> consumer);

  void updateAffixes(Consumer<EntityAffixes.Mutable> consumer);

  void updateLatestDamage(Consumer<LatestDamage.Mutable> consumer);

  int getLevel();

  void setLevel(int level);

}
