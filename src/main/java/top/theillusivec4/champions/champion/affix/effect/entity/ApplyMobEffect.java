package top.theillusivec4.champions.champion.affix.effect.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.champion.value.based.lootcontext.LootContextBasedValue;

public record ApplyMobEffect(
  Holder<MobEffect> toApply,
  LootContextBasedValue duration,
  LootContextBasedValue amplifier,
  boolean infinite
) implements AffixEntityEffect {
  public static final Codec<ApplyMobEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    MobEffect.CODEC.fieldOf("to_apply").forGetter(ApplyMobEffect::toApply),
    LootContextBasedValue.CODEC.fieldOf("duration").forGetter(ApplyMobEffect::duration),
    LootContextBasedValue.CODEC.fieldOf("amplifier").forGetter(ApplyMobEffect::amplifier),
    Codec.BOOL.optionalFieldOf("infinite", false).forGetter(ApplyMobEffect::infinite)
  ).apply(instance, ApplyMobEffect::new));

  public static final MapCodec<ApplyMobEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    MobEffect.CODEC.fieldOf("to_apply").forGetter(ApplyMobEffect::toApply),
    LootContextBasedValue.CODEC.fieldOf("duration").forGetter(ApplyMobEffect::duration),
    LootContextBasedValue.CODEC.fieldOf("amplifier").forGetter(ApplyMobEffect::amplifier),
    Codec.BOOL.optionalFieldOf("infinite", false).forGetter(ApplyMobEffect::infinite)
  ).apply(instance, ApplyMobEffect::new));

  @Override
  public void apply(LootContext context, int level, Entity entity, Vec3 origin) {
    if (entity instanceof LivingEntity livingEntity) {
      int durationTicks;
      if (this.toApply.value().isInstantenous()) {
        durationTicks = 1;
      } else if (this.infinite) {
        durationTicks = -1;
      } else {
        durationTicks = (int) (this.duration.calculate(context, level) * 20);
      }

      livingEntity.addEffect(new MobEffectInstance(this.toApply, durationTicks, (int) Math.max(0, this.amplifier.calculate(context, level))));
//      RandomSource random = livingEntity.getRandom();
//      Optional<Holder<MobEffect>> selected = this.toApply.getRandomElement(random);
//      if (selected.isPresent()) {
//        int ticks = Math.round(
//          Mth.randomBetween(random, this.duration.calculate(context, level), this.maxDuration.calculate(context, level)) * 20.0F
//        );
//        int amplifier = Math.max(
//          0, Math.round(Mth.randomBetween(random, this.minAmplifier.calculate(context, level), this.amplifier.calculate(context, level)))
//        );
//        livingEntity.addEffect(new MobEffectInstance(selected.get(), ticks, amplifier));
//      }

    }
  }

  @Override
  public MapCodec<? extends AffixEntityEffect> codec() {
    return MAP_CODEC;
  }

  @Override
  public void validate(ValidationContext context) {
    AffixEntityEffect.super.validate(context);
//    Validatable.validate(context, "min_duration", this.duration);
//    Validatable.validate(context, "max_duration", this.maxDuration);
//    Validatable.validate(context, "min_amplifier", this.minAmplifier);
//    Validatable.validate(context, "max_amplifier", this.amplifier);
    Validatable.validate(context, "duration", this.duration);
    Validatable.validate(context, "amplifier", this.amplifier);
  }
}
