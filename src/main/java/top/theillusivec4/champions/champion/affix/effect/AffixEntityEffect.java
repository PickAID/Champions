package top.theillusivec4.champions.champion.affix.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.champion.value.based.lootcontext.LevelBasedValue;
import top.theillusivec4.champions.registry.BuiltInRegistries;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface AffixEntityEffect extends AffixLocationBasedEffect {
  Codec<AffixEntityEffect> CODEC = BuiltInRegistries.AFFIX_ENTITY_EFFECT_TYPE.byNameCodec().dispatch(AffixEntityEffect::codec, Function.identity());

  static AffixLocationBasedEffect attribute(Identifier id, Holder<Attribute> attribute, LevelBasedValue amount, AttributeModifier.Operation operation) {
    return new AffixAttributeEffect(id, attribute, amount, operation);
  }

  static AffixEntityEffect allOf(AffixEntityEffect... effects) {
    return new AllOf.EntityEffects(Arrays.stream(effects).toList());
  }

  static AffixEntityEffect applyMobEffect(HolderSet<MobEffect> mobEffect, LevelBasedValue minDuration, LevelBasedValue maxDuration, LevelBasedValue minAmplifier, LevelBasedValue maxAmplifier) {
    return new AffixEntityEffects.ApplyMobEffect(mobEffect, minDuration, maxDuration, minAmplifier, maxAmplifier);
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

  static AffixEntityEffect explode(boolean attributeToUser, @Nullable Holder<DamageType> damageType, @Nullable LevelBasedValue knockbackMultiplier, @Nullable HolderSet<Block> immuneBlocks, Vec3 offset, LevelBasedValue radius, boolean createFire, Level.ExplosionInteraction blockInteraction, ParticleOptions smallParticle, ParticleOptions largeParticle, WeightedList<ExplosionParticleInfo> blockParticles, Holder<SoundEvent> sound) {
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
      blockParticles,
      sound
    );
  }

  static AffixEntityEffect iterationEntity(double horizontalScale, double verticalScale, @Nullable EntityPredicate.Builder predicate, AffixEntityEffect effect) {
    return new AffixEntityEffects.IterationEntity(horizontalScale, verticalScale, predicate != null ? Optional.of(predicate.build()) : Optional.empty(), effect);
  }

  static AffixEntityEffect playSound(List<Holder<SoundEvent>> soundEvents, FloatProvider volume, FloatProvider pitch) {
    return new AffixEntityEffects.PlaySound(soundEvents, volume, pitch);
  }

  void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 origin);

  @Override
  default void onChangedBlock(ServerLevel level, int affixLevel, Entity source, Vec3 origin, boolean becameActive) {
    this.apply(level, affixLevel, source, source, origin);
  }

  MapCodec<? extends AffixEntityEffect> codec();
}
