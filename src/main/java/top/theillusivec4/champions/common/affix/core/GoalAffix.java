package top.theillusivec4.champions.common.affix.core;

import net.minecraft.server.TickTask;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffixLifecycle;

import java.util.List;
import java.util.Optional;

public abstract class GoalAffix extends BasicAffix implements IAffixLifecycle {

	@Override
	public void onSpawn(IChampion champion) {
		LivingEntity livingEntity = champion.getLivingEntity();

		if (livingEntity instanceof Mob mobEntity) {

			// add goal after 10 tick(safely
			var server = Optional.ofNullable(mobEntity.getServer());
			server.ifPresent(mcServer -> mcServer.doRunTask(new TickTask(10, () ->
					this.getGoals(champion)
							.forEach(goal -> mobEntity.goalSelector.addGoal(goal.getA(), goal.getB()))
			)));
		}
	}

	@Override
	public boolean canApply(IChampion champion) {
		LivingEntity livingEntity = champion.getLivingEntity();
		return livingEntity instanceof Mob && super.canApply(champion);
	}

	public abstract List<Tuple<Integer, Goal>> getGoals(IChampion champion);
}
