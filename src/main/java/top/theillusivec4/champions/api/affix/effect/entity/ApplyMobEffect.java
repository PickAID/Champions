package top.theillusivec4.champions.api.affix.effect.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootContextBasedValue;

import java.util.Optional;

public record ApplyMobEffect(
  HolderSet<MobEffect> toApply,
  LootContextBasedValue minDuration,
  LootContextBasedValue maxDuration,
  LootContextBasedValue minAmplifier,
  LootContextBasedValue maxAmplifier
) implements AffixEntityEffect {
  public static final MapCodec<ApplyMobEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    RegistryCodecs.homogeneousList(Registries.MOB_EFFECT).fieldOf("to_apply").forGetter(ApplyMobEffect::toApply),
    LootContextBasedValue.CODEC.fieldOf("min_duration").forGetter(ApplyMobEffect::minDuration),
    LootContextBasedValue.CODEC.fieldOf("max_duration").forGetter(ApplyMobEffect::maxDuration),
    LootContextBasedValue.CODEC.fieldOf("min_amplifier").forGetter(ApplyMobEffect::minAmplifier),
    LootContextBasedValue.CODEC.fieldOf("max_amplifier").forGetter(ApplyMobEffect::maxAmplifier)
  ).apply(instance, ApplyMobEffect::new));

  @Override
  public void apply(LootContext context, int level, Entity entity, Vec3 position) {
    if (entity instanceof LivingEntity living) {
      RandomSource random = living.getRandom();
      Optional<Holder<MobEffect>> selected = this.toApply.getRandomElement(random);
      if (selected.isPresent()) {
        int ticks = Math.round(
          Mth.randomBetween(random, this.minDuration.calculate(context, level), this.maxDuration.calculate(context, level)) * 20.0F
        );
        int amplifier = Math.max(
          0, Math.round(Mth.randomBetween(random, this.minAmplifier.calculate(context, level), this.maxAmplifier.calculate(context, level)))
        );
        living.addEffect(new MobEffectInstance(selected.get(), ticks, amplifier));
      }
    }
  }

  @Override
  public MapCodec<? extends AffixEntityEffect> codec() {
    return MAP_CODEC;
  }

  @Override
  public void validate(ValidationContext context) {
    AffixEntityEffect.super.validate(context);
    Validatable.validate(context, "min_duration", this.minDuration);
    Validatable.validate(context, "max_duration", this.maxDuration);
    Validatable.validate(context, "min_amplifier", this.minAmplifier);
    Validatable.validate(context, "max_amplifier", this.maxAmplifier);
  }
}
