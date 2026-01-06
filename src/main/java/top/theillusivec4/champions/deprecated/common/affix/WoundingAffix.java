package top.theillusivec4.champions.deprecated.common.affix;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.api.data.AffixCategory;
import top.theillusivec4.champions.deprecated.api.data.AffixSetting;
import top.theillusivec4.champions.deprecated.common.affix.core.CombatAffix;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.effects.MobEffects;

@Deprecated
public class WoundingAffix extends CombatAffix {

  @SubscribeEvent
  public void onHeal(LivingHealEvent evt) {
    if (evt.getEntity().hasEffect(MobEffects.WOUND_EFFECT_TYPE)) {
      evt.setAmount(evt.getAmount() * 0.5F);
    }
  }

  @SubscribeEvent
  public void onDamage(LivingDamageEvent.Pre evt) {
    if (evt.getEntity().hasEffect(MobEffects.WOUND_EFFECT_TYPE)) {
      evt.setNewDamage(evt.getOriginalDamage() * 1.5F);
    }
  }

  @Override
  public boolean onAttack(IChampion champion, LivingEntity target, DamageSource source,
                          float amount) {
    if (target.getRandom().nextFloat() < ChampionsConfig.woundingChance) {
      target.addEffect(new MobEffectInstance(MobEffects.WOUND_EFFECT_TYPE, 200, 0));
    }
    return true;
  }

  @Override
  public AffixSetting createDefaultSetting() {
    return AffixSetting.builder()
      .withDefault()
      .setCategory(AffixCategory.OFFENSE)
      .setHasSub()
      .build();
  }

}
