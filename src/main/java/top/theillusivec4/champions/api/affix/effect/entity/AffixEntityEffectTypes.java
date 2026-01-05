package top.theillusivec4.champions.api.affix.effect.entity;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.effect.AllOf;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootContextBasedValue;
import top.theillusivec4.champions.common.registries.Registries;

import java.util.Arrays;
import java.util.function.Supplier;

public final class AffixEntityEffectTypes {
  private static final DeferredRegister<MapCodec<? extends AffixEntityEffect>> DEFERRED_REGISTER = DeferredRegister.create(Registries.AFFIX_ENTITY_EFFECT_TYPE, Champions.MODID);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<DamageEntity>> DAMAGE_ENTITY = register("damage_entity", DamageEntity.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<AllOf.EntityEffects>> ALL_OF = register("all_of", AllOf.EntityEffects.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<ApplyMobEffect>> APPLY_MOB_EFFECT = register("apply_mob_effect", ApplyMobEffect.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<Ignite>> IGNITE = register("ignite", Ignite.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<SpawnParticlesEffect>> SPAWN_PARTICLES = register("spawn_particles", SpawnParticlesEffect.MAP_CODEC);

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  public static AffixEntityEffect allOf(AffixEntityEffect... effects) {
    return new AllOf.EntityEffects(Arrays.stream(effects).toList());
  }

  public static AffixEntityEffect applyMobEffect(HolderSet<MobEffect> mobEffects, LootContextBasedValue minDuration, LootContextBasedValue maxDuration, LootContextBasedValue minAmplifier, LootContextBasedValue maxAmplifier) {
    return new ApplyMobEffect(mobEffects, minDuration, maxDuration, minAmplifier, maxAmplifier);
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

  private static <T extends AffixEntityEffect> DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
    return register(name, () -> mapCodec);
  }

  private static <T extends AffixEntityEffect> DeferredHolder<MapCodec<? extends AffixEntityEffect>, MapCodec<T>> register(String name, Supplier<MapCodec<T>> mapCodecSupplier) {
    return DEFERRED_REGISTER.register(name, mapCodecSupplier);
  }

  private AffixEntityEffectTypes() {
  }
}
