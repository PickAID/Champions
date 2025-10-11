package top.theillusivec4.champions.common.affix;

import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.CombatAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;

public class DampeningAffix extends CombatAffix {
	@Override
	public float onHurt(IChampion champion, DamageSource source, float amount, float newAmount) {
		return source instanceof IndirectEntityDamageSource ? newAmount * (float) (1.0F
				- ChampionsConfig.dampenedDamageReduction) : newAmount;
	}

	@Override
	public AffixSetting createDefaultSetting() {
		return AffixSetting.builder()
				.withDefault()
				.setCategory(AffixCategory.DEFENSE)
				.build();
	}
}
