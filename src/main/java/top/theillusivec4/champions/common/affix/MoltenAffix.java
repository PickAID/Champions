package top.theillusivec4.champions.common.affix;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.CombatLifeCycleAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;

import java.util.Iterator;
import java.util.Set;

public class MoltenAffix extends CombatLifeCycleAffix {

	@Override
	public void onSpawn(IChampion champion) {
		LivingEntity livingEntity = champion.getLivingEntity();
		livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 0, true, false));

		if (livingEntity instanceof Mob mobEntity) {

			mobEntity.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
			mobEntity.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
			mobEntity.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
			mobEntity.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);

			try {

				Set<WrappedGoal> goals = mobEntity.goalSelector.getAvailableGoals();
				Iterator<WrappedGoal> iter = goals.iterator();

				while (iter.hasNext()) {
					WrappedGoal goal = iter.next();
					Goal baseGoal = goal.getGoal();

					if (baseGoal instanceof FleeSunGoal || baseGoal instanceof RestrictSunGoal) {
						iter.remove();
					}
				}
			} catch (Exception e) {
				Champions.LOGGER.error("Error accessing goals!");
			}

			if (mobEntity.getNavigation() instanceof GroundPathNavigation) {
				((GroundPathNavigation) mobEntity.getNavigation()).setAvoidSun(false);
			}
		}
	}

	@Override
	public void onServerUpdate(IChampion champion) {
		LivingEntity livingEntity = champion.getLivingEntity();

		if (livingEntity.tickCount % 20 == 0) {
			//todo: figure out a better way to do this fire effect
			//livingEntity.setFire(10);
			livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 0, true, false));

			if (!ChampionsConfig.moltenWaterResistance && livingEntity.isInWaterOrRain()) {
				Holder.Reference<DamageType> drown = livingEntity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.DROWN);
				livingEntity.hurt(new DamageSource(drown), 1.0F);
			}
		}
	}

	@Override
	public boolean onAttack(IChampion champion, LivingEntity target, DamageSource source,
	                        float amount) {
		// Molten attacks set the target on fire and count as fire damage (1.12: target.setFire(10) +
		// source.setFireDamage()). A DamageSource can't be changed on 1.20. Hurting the target again with
		// inFire from here replaced the champion's hit with an inFire hit that has no attacker (the real
		// hit then lands in the target's damage cooldown), and with Fire Resistance the real hit still
		// went through. So the champion's own hit stays as it is and only the fire part is kept:
		target.setSecondsOnFire(10);
		// fire damage doesn't hurt a target with Fire Resistance or one that's immune to fire, so the hit
		// is cancelled for those, like in 1.12
		return !target.fireImmune() && !target.hasEffect(MobEffects.FIRE_RESISTANCE);
	}

	@Override
	public AffixSetting createDefaultSetting() {
		return AffixSetting.builder()
				.withDefault()
				.setCategory(AffixCategory.OFFENSE)
				.build();
	}

}
