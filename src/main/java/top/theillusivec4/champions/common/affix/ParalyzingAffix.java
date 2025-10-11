package top.theillusivec4.champions.common.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.CombatAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.registry.ModMobEffects;

public class ParalyzingAffix extends CombatAffix {
	@Override
	public boolean onAttack(
			IChampion champion, LivingEntity target, DamageSource source,
			float amount) {

		if (target.getRandom().nextFloat() < ChampionsConfig.paralyzingChance && !target.hasEffect(
				ModMobEffects.PARALYSIS_EFFECT_TYPE.get())) {
			target.addEffect(new EffectInstance(ModMobEffects.PARALYSIS_EFFECT_TYPE.get(), 60, 0));
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
