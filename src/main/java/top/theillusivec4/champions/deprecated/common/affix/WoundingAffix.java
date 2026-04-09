package top.theillusivec4.champions.deprecated.common.affix;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import top.theillusivec4.champions.deprecated.api.AffixCategory;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.common.affix.core.BasicAffix;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.deprecated.common.registry.ModMobEffects;

public class WoundingAffix extends BasicAffix {
  public WoundingAffix() {
    super("wounding", AffixCategory.OFFENSE, true);
  }

  @SubscribeEvent
  public void onHeal(LivingHealEvent evt) {
    if (evt.getEntity().hasEffect(ModMobEffects.WOUND_EFFECT_TYPE)) {
      evt.setAmount(evt.getAmount() * 0.5F);
    }
  }

  @SubscribeEvent
  public void onDamage(LivingDamageEvent.Pre evt) {
    if (evt.getEntity().hasEffect(ModMobEffects.WOUND_EFFECT_TYPE)) {
      evt.setNewDamage(evt.getOriginalDamage() * 1.5F);
    }
  }

  @Override
  public boolean onAttack(IChampion champion, LivingEntity target, DamageSource source,
                          float amount) {
    if (target.getRandom().nextFloat() < ChampionsConfig.woundingChance) {
      target.addEffect(new MobEffectInstance(ModMobEffects.WOUND_EFFECT_TYPE, 200, 0));
    }
    return true;
  }
}
