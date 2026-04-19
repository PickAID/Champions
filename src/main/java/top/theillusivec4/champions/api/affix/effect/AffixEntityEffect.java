package top.theillusivec4.champions.api.affix.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.api.affix.ProjectileTemplate;
import top.theillusivec4.champions.core.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.world.entity.affix.effects.AffixEntityEffects;
import top.theillusivec4.champions.world.entity.affix.effects.AllOf;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public interface AffixEntityEffect extends AffixLocationBasedEffect {
  Codec<AffixEntityEffect> CODEC = Codec.lazyInitialized(() -> ChampionsBuiltInRegistries.AFFIX_ENTITY_EFFECT_TYPE.byNameCodec().dispatch(AffixEntityEffect::codec, Function.identity()));

  static AffixEntityEffect allOf(AffixEntityEffect... effects) {
    return new AllOf.EntityEffects(Arrays.stream(effects).toList());
  }

  static AffixEntityEffect applyMobEffect(HolderSet<MobEffect> toApply, LevelBasedValue minDuration, LevelBasedValue maxDuration, LevelBasedValue minAmplifier, LevelBasedValue maxAmplifier) {
    return new AffixEntityEffects.ApplyMobEffect(toApply, minDuration, maxDuration, minAmplifier, maxAmplifier);
  }

  static AffixEntityEffect ignite(LevelBasedValue duration) {
    return new AffixEntityEffects.Ignite(duration);
  }

  static AffixEntityEffect damageEntity(LevelBasedValue minDamage, LevelBasedValue maxDamage, Holder<DamageType> damageType) {
    return new AffixEntityEffects.DamageEntity(minDamage, maxDamage, damageType);
  }

  static AffixEntityEffect spawnParticles(ParticleOptions particle, int count, AffixEntityEffects.SpawnParticlesEffect.PositionSource horizontalPosition, AffixEntityEffects.SpawnParticlesEffect.PositionSource verticalPosition, AffixEntityEffects.SpawnParticlesEffect.VelocitySource horizontalVelocity, AffixEntityEffects.SpawnParticlesEffect.VelocitySource verticalVelocity, FloatProvider speed) {
    return new AffixEntityEffects.SpawnParticlesEffect(particle, count, horizontalPosition, verticalPosition, horizontalVelocity, verticalVelocity, speed);
  }

  static AffixEntityEffect explode(boolean attributeToUser, @Nullable Holder<DamageType> damageType, @Nullable LevelBasedValue knockbackMultiplier, @Nullable HolderSet<Block> immuneBlocks, Vec3 offset, LevelBasedValue radius, boolean createFire, Level.ExplosionInteraction blockInteraction, ParticleOptions smallParticle, ParticleOptions largeParticle, Holder<SoundEvent> sound) {
    return new AffixEntityEffects.ExplodeEffect(
      attributeToUser,
      Optional.ofNullable(damageType),
      Optional.ofNullable(knockbackMultiplier),
      Optional.ofNullable(immuneBlocks),
      offset,
      radius,
      createFire,
      blockInteraction,
      smallParticle,
      largeParticle,
      sound
    );
  }

  static AffixEntityEffect iterationEntity(double horizontalScale, double verticalScale, @Nullable EntityPredicate.Builder predicate, AffixEntityEffect effect) {
    return new AffixEntityEffects.IterationEntity(horizontalScale, verticalScale, predicate != null ? Optional.of(predicate.build()) : Optional.empty(), effect);
  }

  static AffixEntityEffect playSound(Holder<SoundEvent> sound, FloatProvider volume, FloatProvider pitch) {
    return new AffixEntityEffects.PlaySound(sound, volume, pitch);
  }

  static AffixEntityEffect projection(ProjectileTemplate projectile, ItemStack projectileItem, LevelBasedValue power, LevelBasedValue uncertainty, Holder<SoundEvent> sound) {
    return new AffixEntityEffects.ProjectionEffect(projectile, projectileItem, power, uncertainty, sound);
  }

  static AffixEntityEffect summonEntity(HolderSet<EntityType<?>> entityTypes) {
    return new AffixEntityEffects.SummonEntity(entityTypes);
  }

  static AffixEntityEffect movement(double speed) {
    return new AffixEntityEffects.MovementEffect(speed);
  }

  void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position);

  @Override
  default void onChangedBlock(ServerLevel level, int affixLevel, Entity source, Vec3 origin, boolean becameActive) {
    this.apply(level, affixLevel, source, source, origin);
  }

  MapCodec<? extends AffixEntityEffect> codec();
}
