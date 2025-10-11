package top.theillusivec4.champions.common.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.CombatAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.registry.ModMobEffects;

public class WoundingAffix extends CombatAffix {

	@SubscribeEvent
	public void onHeal(LivingHealEvent evt) {
		if (evt.getEntityLiving().hasEffect(ModMobEffects.WOUND_EFFECT_TYPE.get())) {
			evt.setAmount(evt.getAmount() * 0.5F);
		}
	}

	@SubscribeEvent
	public void onDamage(LivingDamageEvent evt) {
		if (evt.getEntityLiving().hasEffect(ModMobEffects.WOUND_EFFECT_TYPE.get())) {
			evt.setAmount(evt.getAmount() * 1.5F);
		}
	}

	@Override
	public boolean onAttack(IChampion champion, LivingEntity target, DamageSource source,
	                        float amount) {
		if (target.getRandom().nextFloat() < ChampionsConfig.woundingChance) {
			target.addEffect(new EffectInstance(ModMobEffects.WOUND_EFFECT_TYPE.get(), 200, 0));
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
