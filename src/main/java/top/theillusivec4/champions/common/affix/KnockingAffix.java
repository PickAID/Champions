package top.theillusivec4.champions.common.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.CombatAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;

public class KnockingAffix extends CombatAffix {
	@Override
	public boolean onAttack(IChampion champion, LivingEntity target, DamageSource source,
	                        float amount) {
		target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 2));
		LivingEntity livingEntity = champion.getLivingEntity();
		target.knockback((float) ChampionsConfig.knockingMultiplier,
				MathHelper.sin(livingEntity.yRot * ((float) Math.PI / 180F)),
				(-MathHelper.cos(livingEntity.yRot * ((float) Math.PI / 180F))));
		return true;
	}

	@Override
	public AffixSetting createDefaultSetting() {
		return AffixSetting.builder()
				.withDefault()
				.build();
	}
}
