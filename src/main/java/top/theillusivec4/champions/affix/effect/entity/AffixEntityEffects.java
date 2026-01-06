package top.theillusivec4.champions.affix.effect.entity;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.affix.effect.AllOf;
import top.theillusivec4.champions.affix.lootcontextbasedvalue.LootContextBasedValue;
import top.theillusivec4.champions.deprecated.common.registries.Registries;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public final class AffixEntityEffects {
  private static final DeferredRegister<MapCodec<? extends AffixEntityEffect>> DEFERRED_REGISTER = DeferredRegister.create(Registries.AFFIX_ENTITY_EFFECT_TYPE, Champions.MODID);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<DamageEntity>> DAMAGE_ENTITY = register("damage_entity", DamageEntity.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<AllOf.EntityEffects>> ALL_OF = register("all_of", AllOf.EntityEffects.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<ApplyMobEffect>> APPLY_MOB_EFFECT = register("apply_mob_effect", ApplyMobEffect.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<Ignite>> IGNITE = register("ignite", Ignite.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<SpawnParticlesEffect>> SPAWN_PARTICLES = register("spawn_particles", SpawnParticlesEffect.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<IterationEntity>> ITERATION_ENTITY = register("iteration_entity", IterationEntity.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<ExplodeEffect>> EXPLODE = register("explode", ExplodeEffect.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<PlaySound>> PLAY_SOUND = register("play_sound", PlaySound.MAP_CODEC);

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  public static AffixEntityEffect allOf(AffixEntityEffect... effects) {
    return new AllOf.EntityEffects(Arrays.stream(effects).toList());
  }

  public static AffixEntityEffect applyMobEffect(Holder<MobEffect> mobEffect, LootContextBasedValue duration, LootContextBasedValue amplifier) {
    return applyMobEffect(mobEffect, duration, amplifier, false);
  }

  public static AffixEntityEffect applyMobEffect(Holder<MobEffect> mobEffect, LootContextBasedValue duration, LootContextBasedValue amplifier, boolean infinite) {
    return new ApplyMobEffect(mobEffect, duration, amplifier, infinite);
  }

  public static AffixEntityEffect ignite(LootContextBasedValue value) {
    return new Ignite(value);
  }

  public static AffixEntityEffect damageEntity(LootContextBasedValue minDamage, LootContextBasedValue maxDamage, Holder<DamageType> damageType) {
    return new DamageEntity(minDamage, maxDamage, damageType);
  }

  public static AffixEntityEffect spawnParticles(ParticleOptions particle, int count, SpawnParticlesEffect.PositionSource horizontalPosition, SpawnParticlesEffect.PositionSource verticalPosition, SpawnParticlesEffect.VelocitySource horizontalVelocity, SpawnParticlesEffect.VelocitySource verticalVelocity, FloatProvider speed) {
    return new SpawnParticlesEffect(particle, count, horizontalPosition, verticalPosition, horizontalVelocity, verticalVelocity, speed);
  }

  public static AffixEntityEffect explode(boolean attributeToUser, @Nullable Holder<DamageType> damageType, @Nullable LootContextBasedValue knockbackMultiplier, @Nullable HolderSet<Block> immuneBlocks, Vec3 offset, LootContextBasedValue radius, boolean createFire, Level.ExplosionInteraction blockInteraction, ParticleOptions smallParticle, ParticleOptions largeParticle, WeightedList<ExplosionParticleInfo> blockParticles, Holder<SoundEvent> sound) {
    return new ExplodeEffect(
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

  public static AffixEntityEffect iterationEntity(double horizontalScale, double verticalScale, AffixEntityEffect effect) {
    return new IterationEntity(horizontalScale, verticalScale, Optional.empty(), effect);
  }

  public static AffixEntityEffect iterationEntity(double horizontalScale, double verticalScale, EntityPredicate.Builder predicate, AffixEntityEffect effect) {
    return new IterationEntity(horizontalScale, verticalScale, Optional.of(predicate.build()), effect);
  }

  public static AffixEntityEffect playSound(List<Holder<SoundEvent>> soundEvents, FloatProvider volume, FloatProvider pitch) {
    return new PlaySound(soundEvents, volume, pitch);
  }

  private static <T extends AffixEntityEffect> DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
    return register(name, () -> mapCodec);
  }

  private static <T extends AffixEntityEffect> DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<T>> register(String name, Supplier<MapCodec<T>> mapCodecSupplier) {
    return DEFERRED_REGISTER.register(name, mapCodecSupplier);
  }

  private AffixEntityEffects() {
  }
}
