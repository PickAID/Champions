package top.theillusivec4.champions.common.affix;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.FleeSunGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.RestrictSunGoal;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.CombatLifeCycleAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class MoltenAffix extends CombatLifeCycleAffix {

	@Override
	public void onSpawn(IChampion champion) {
		LivingEntity livingEntity = champion.getLivingEntity();
		livingEntity.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 40, 0, true, false));

		if (livingEntity instanceof MobEntity) {
			MobEntity mobEntity = (MobEntity) livingEntity;

			mobEntity.setPathfindingMalus(PathNodeType.WATER, -1.0F);
			mobEntity.setPathfindingMalus(PathNodeType.LAVA, 8.0F);
			mobEntity.setPathfindingMalus(PathNodeType.DANGER_FIRE, 0.0F);
			mobEntity.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, 0.0F);

			try {

				Set<PrioritizedGoal> goals = mobEntity.goalSelector.getRunningGoals().collect(Collectors.toSet());
				Iterator<PrioritizedGoal> iter = goals.iterator();

				while (iter.hasNext()) {
					PrioritizedGoal goal = iter.next();
					Goal baseGoal = goal.getGoal();

					if (baseGoal instanceof FleeSunGoal || baseGoal instanceof RestrictSunGoal) {
						iter.remove();
					}
				}
			} catch (Exception e) {
				Champions.LOGGER.error("Error accessing goals!");
			}

			if (mobEntity.getNavigation() instanceof GroundPathNavigator) {
				((GroundPathNavigator) mobEntity.getNavigation()).setAvoidSun(false);
			}
		}
	}

	@Override
	public void onServerUpdate(IChampion champion) {
		LivingEntity livingEntity = champion.getLivingEntity();

		if (livingEntity.tickCount % 20 == 0) {
			//todo: figure out a better way to do this fire effect
			//livingEntity.setFire(10);
			livingEntity.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 40, 0, true, false));

			if (!ChampionsConfig.moltenWaterResistance && livingEntity.isInWaterOrRain()) {
				livingEntity.hurt(DamageSource.DROWN, 1.0F);
			}
		}
	}

	@Override
	public boolean onAttack(IChampion champion, LivingEntity target, DamageSource source,
	                        float amount) {
		target.setSecondsOnFire(10);
		source.setIsFire();
		target.hurt(source, amount);
		return true;
	}

	@Override
	public AffixSetting createDefaultSetting() {
		return AffixSetting.builder()
				.withDefault()
				.setCategory(AffixCategory.OFFENSE)
				.build();
	}

}
