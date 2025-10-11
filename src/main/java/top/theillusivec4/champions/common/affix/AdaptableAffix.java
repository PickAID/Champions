package top.theillusivec4.champions.common.affix;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.AffixData;
import top.theillusivec4.champions.common.affix.core.CombatAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.config.ConfigEnums;

public class AdaptableAffix extends CombatAffix {
	@Override
	public float onHurt(IChampion champion, DamageSource source, float amount, float newAmount) {
		String type = source.getMsgId();
		DamageData damageData = AffixData.getData(champion, this.toString(), DamageData.class);

		if (damageData.name.equalsIgnoreCase(type)) {
			newAmount -= (float) (amount * ChampionsConfig.adaptableDamageReductionIncrement * damageData.count);
			damageData.count++;
		} else {
			damageData.name = type;
			damageData.count = 0;
		}
		damageData.saveData();
		return Math.max(amount * (float) (1.0f - ChampionsConfig.adaptableMaxDamageReduction),
				newAmount);
	}

	@Override
	public AffixSetting createDefaultSetting() {
		return AffixSetting.builder()
				.withDefault()
				.setCategory(AffixCategory.DEFENSE)
				.setHasSub(false)
				.setMobList(Lists.newArrayList(
						ResourceLocation.tryParse("pig"),
						ResourceLocation.tryParse("creeper")
				))
				.setMobPermission(ConfigEnums.Permission.BLACKLIST)
				.build();
	}

	public static class DamageData extends AffixData {
		String name;
		int count;

		@Override
		public void readFromNBT(CompoundNBT tag) {
			name = tag.getString("name");
			count = tag.getInt("count");
		}

		@Override
		public CompoundNBT writeToNBT() {
			CompoundNBT compound = new CompoundNBT();
			compound.putString("name", name);
			compound.putInt("count", count);
			return compound;
		}
	}
}
