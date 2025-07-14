package top.theillusivec4.champions.common.affix;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.AbstractBasicAffix;
import top.theillusivec4.champions.common.affix.core.CombatLifeCycleAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;

import java.util.List;

public class PlaguedAffix extends CombatLifeCycleAffix {

  @Override
  public void onClientUpdate(IChampion champion) {
    LivingEntity livingEntity = champion.getLivingEntity();
    float radius = ChampionsConfig.plaguedRange;
    float circle = (float) Math.PI * radius * radius;

    for (int circleParticles = 0; (float) circleParticles < circle; ++circleParticles) {
      float f6 = livingEntity.getRandom().nextFloat() * ((float) Math.PI * 2F);
      float randomRadiusSection = Mth.sqrt(livingEntity.getRandom().nextFloat()) * radius;
      float f8 = Mth.cos(f6) * randomRadiusSection;
      float f9 = Mth.sin(f6) * randomRadiusSection;
      int l1 = ChampionsConfig.plaguedEffect.getEffect().value().getColor();
      int red = l1 >> 16 & 255;
      int green = l1 >> 8 & 255;
      int blue = l1 & 255;
      livingEntity.level()
        .addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, FastColor.ARGB32.color(125, red, green, blue)), livingEntity.position().x + (double) f8,
          livingEntity.position().y, livingEntity.position().z + (double) f9,
          (double) red / 255, (double) green / 255, (double) blue / 255);
    }
  }

  @Override
  public void onServerUpdate(IChampion champion) {
    LivingEntity livingEntity = champion.getLivingEntity();

    if (livingEntity.tickCount % 10 == 0) {
      List<Entity> list = livingEntity.level().getEntities(livingEntity,
        livingEntity.getBoundingBox().inflate(ChampionsConfig.plaguedRange),
        entity -> entity instanceof LivingEntity && AbstractBasicAffix
          .canTarget(livingEntity, (LivingEntity) entity, true));
      list.forEach(entity -> {

        if (entity instanceof LivingEntity) {
          ((LivingEntity) entity).addEffect(
            new MobEffectInstance(ChampionsConfig.plaguedEffect.getEffect(),
              ChampionsConfig.plaguedEffect.getDuration(),
              ChampionsConfig.plaguedEffect.getAmplifier()));
        }
      });
      livingEntity.removeEffect(ChampionsConfig.plaguedEffect.getEffect());
    }
  }

  @Override
  public boolean onAttack(IChampion champion, LivingEntity target, DamageSource source,
                          float amount) {
    target.addEffect(new MobEffectInstance(ChampionsConfig.plaguedEffect.getEffect(),
      ChampionsConfig.plaguedEffect.getDuration(), ChampionsConfig.plaguedEffect.getAmplifier()));
    return true;
  }

  @Override
  public AffixSetting createDefaultSetting() {
    return AffixSetting.builder()
      .withDefault()
      .build();
  }

}
