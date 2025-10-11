package top.theillusivec4.champions.common.affix.core;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Tuple;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffixLifecycle;

import java.util.List;

public abstract class GoalAffix extends BasicAffix implements IAffixLifecycle {

	@Override
	public void onSpawn(IChampion champion) {
		LivingEntity livingEntity = champion.getLivingEntity();

		if (livingEntity instanceof MobEntity) {
			MobEntity mobEntity = (MobEntity) livingEntity;
			this.getGoals(champion)
					.forEach(goal -> mobEntity.goalSelector.addGoal(goal.getA(), goal.getB()));
		}
	}

	@Override
	public boolean canApply(IChampion champion) {
		LivingEntity livingEntity = champion.getLivingEntity();
		return livingEntity instanceof MobEntity && super.canApply(champion);
	}

	public abstract List<Tuple<Integer, Goal>> getGoals(IChampion champion);
}
