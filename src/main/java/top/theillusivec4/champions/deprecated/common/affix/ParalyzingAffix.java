package top.theillusivec4.champions.deprecated.common.affix;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.api.data.AffixSetting;
import top.theillusivec4.champions.deprecated.common.affix.core.CombatAffix;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.world.effect.MobEffects;

@Deprecated
public class ParalyzingAffix extends CombatAffix {

  @Override
  public boolean onAttack(
    IChampion champion, LivingEntity target, DamageSource source,
    float amount) {

    if (target.getRandom().nextFloat() < ChampionsConfig.paralyzingChance && !target.hasEffect(MobEffects.PARALYSIS_EFFECT_TYPE)) {
      target.addEffect(new MobEffectInstance(MobEffects.PARALYSIS_EFFECT_TYPE, 60, 0));
    }
    return true;
  }


  @Override
  public AffixSetting createDefaultSetting() {
    return AffixSetting.builder()
      .withDefault()
      .build();
  }

}
