package top.theillusivec4.champions.api;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.LatestDamage;
import top.theillusivec4.champions.api.affix.effect.AffixTarget;

import java.util.function.Consumer;

public interface IChampionHandler {

  void addAttributeModifier();

  void removeAttributeModifier();

  float getDamageProtection(ServerLevel level, DamageSource source);

  float modifyKnockback(ServerLevel level, DamageSource source, float knockback);

  void doPostAttackEffects(ServerLevel level, AffixTarget target, Entity victim, DamageSource source);

  void doSpawnEffects(ServerLevel serverLevel);

  void tickEffects(ServerLevel level);

  void runIteration(Consumer<Holder<Affix>> consumer);

  void updateAffixes(Consumer<EntityAffixes.Mutable> consumer);

  void updateLatestDamage(Consumer<LatestDamage.Mutable> consumer);

  int getChampionLevel();

  void setChampionLevel(int level);
}
