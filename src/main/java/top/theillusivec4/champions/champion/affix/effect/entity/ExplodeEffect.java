package top.theillusivec4.champions.champion.affix.effect.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.champion.affix.lootcontextbasedvalue.LootContextBasedValue;

import java.util.Optional;

public record ExplodeEffect(
  boolean attributeToUser,
  Optional<Holder<DamageType>> damageType,
  Optional<LootContextBasedValue> knockbackMultiplier,
  Optional<HolderSet<Block>> immuneBlocks,
  Vec3 offset,
  LootContextBasedValue radius,
  boolean createFire,
  Level.ExplosionInteraction blockInteraction,
  ParticleOptions smallParticle,
  ParticleOptions largeParticle,
  WeightedList<ExplosionParticleInfo> blockParticles,
  Holder<SoundEvent> sound
) implements AffixEntityEffect {
  public static final MapCodec<ExplodeEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.BOOL.optionalFieldOf("attribute_to_user", false).forGetter(ExplodeEffect::attributeToUser),
    DamageType.CODEC.optionalFieldOf("damage_type").forGetter(ExplodeEffect::damageType),
    LootContextBasedValue.CODEC.optionalFieldOf("knockback_multiplier").forGetter(ExplodeEffect::knockbackMultiplier),
    RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("immune_blocks").forGetter(ExplodeEffect::immuneBlocks),
    Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(ExplodeEffect::offset),
    LootContextBasedValue.CODEC.fieldOf("radius").forGetter(ExplodeEffect::radius),
    Codec.BOOL.optionalFieldOf("create_fire", false).forGetter(ExplodeEffect::createFire),
    Level.ExplosionInteraction.CODEC.fieldOf("block_interaction").forGetter(ExplodeEffect::blockInteraction),
    ParticleTypes.CODEC.fieldOf("small_particle").forGetter(ExplodeEffect::smallParticle),
    ParticleTypes.CODEC.fieldOf("large_particle").forGetter(ExplodeEffect::largeParticle),
    WeightedList.codec(ExplosionParticleInfo.CODEC).optionalFieldOf("block_particles", WeightedList.of()).forGetter(ExplodeEffect::blockParticles),
    SoundEvent.CODEC.fieldOf("sound").forGetter(ExplodeEffect::sound)
  ).apply(instance, ExplodeEffect::new));

  @Override
  public void apply(LootContext context, int level, Entity entity, Vec3 origin) {
    ServerLevel serverLevel = context.getLevel();
    Vec3 pos = origin.add(this.offset);
    serverLevel.explode(
      this.attributeToUser ? entity : null,
      this.getDamageSource(entity, pos),
      new SimpleExplosionDamageCalculator(
        this.blockInteraction != Level.ExplosionInteraction.NONE,
        this.damageType.isPresent(),
        this.knockbackMultiplier.map(value -> value.calculate(context, level)),
        this.immuneBlocks
      ),
      pos.x(),
      pos.y(),
      pos.z(),
      Math.max(this.radius.calculate(context, level), 0.0F),
      this.createFire,
      this.blockInteraction,
      this.smallParticle,
      this.largeParticle,
      this.blockParticles,
      this.sound
    );
  }

  @Override
  public MapCodec<? extends AffixEntityEffect> codec() {
    return null;
  }

  @Override
  public void validate(ValidationContext context) {
    AffixEntityEffect.super.validate(context);
    this.knockbackMultiplier.ifPresent(lootContextBasedValue -> Validatable.validate(context, "knockbackMultiplier", lootContextBasedValue));
    Validatable.validate(context, "radius", this.radius);
  }

  private @Nullable DamageSource getDamageSource(Entity entity, Vec3 position) {
    return this.damageType.map(damageTypeHolder -> this.attributeToUser ? new DamageSource(damageTypeHolder, entity) : new DamageSource(damageTypeHolder, position)).orElse(null);
  }
}
