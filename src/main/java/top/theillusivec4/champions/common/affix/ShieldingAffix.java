package top.theillusivec4.champions.common.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.AffixData;
import top.theillusivec4.champions.common.affix.core.CombatLifeCycleAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;

import java.util.Random;

public class ShieldingAffix extends CombatLifeCycleAffix {

	@Override
	public void onClientUpdate(IChampion champion) {
		LivingEntity livingEntity = champion.getLivingEntity();
		AffixData.BooleanData shielding =
				AffixData.getData(champion, this.toString(), AffixData.BooleanData.class);
		Random random = livingEntity.getRandom();

		if (shielding.mode) {
			livingEntity.level.addParticle(ParticleTypes.ENTITY_EFFECT,
					livingEntity.position().x + (random.nextFloat() - 0.5D) * livingEntity.getBbWidth(),
					livingEntity.position().y + random.nextFloat() * livingEntity.getBbHeight(),
					livingEntity.position().z + (random.nextFloat() - 0.5D) * livingEntity.getBbWidth(),
					1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public void onServerUpdate(IChampion champion) {
		LivingEntity livingEntity = champion.getLivingEntity();

		if (livingEntity.tickCount % 40 == 0 && livingEntity.getRandom().nextDouble() < ChampionsConfig.shieldingChance) {
			AffixData.BooleanData shielding =
					AffixData.getData(champion, this.toString(), AffixData.BooleanData.class);
			shielding.mode = !shielding.mode;
			shielding.saveData();
			this.sync(champion);
		}
	}

	@Override
	public void readSyncTag(IChampion champion, CompoundNBT tag) {
		AffixData.BooleanData shielding =
				AffixData.getData(champion, this.toString(), AffixData.BooleanData.class);
		shielding.readFromNBT(tag);
		shielding.saveData();
	}

	@Override
	public CompoundNBT writeSyncTag(IChampion champion) {
		return AffixData.getData(champion, this.toString(), AffixData.BooleanData.class)
				.writeToNBT();
	}

	@Override
	public boolean onAttacked(IChampion champion, DamageSource source, float amount) {
		if (source == DamageSource.OUT_OF_WORLD) {
			return true;
		}
		AffixData.BooleanData shielding =
				AffixData.getData(champion, this.toString(), AffixData.BooleanData.class);

		if (shielding.mode) {
			champion.getLivingEntity().playSound(SoundEvents.PLAYER_ATTACK_NODAMAGE, 1.0F, 1.0F);
			return false;
		}
		return true;
	}

	@Override
	public AffixSetting createDefaultSetting() {
		return AffixSetting.builder()
				.withDefault()
				.setCategory(AffixCategory.DEFENSE)
				.build();
	}
}
