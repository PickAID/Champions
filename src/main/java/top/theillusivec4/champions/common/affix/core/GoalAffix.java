package top.theillusivec4.champions.common.affix.core;

import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import top.theillusivec4.champions.api.IChampion;

import java.util.List;

public abstract class GoalAffix extends BasicAffix {

    @Override
    public void onSpawn(IChampion champion) {
        LivingEntity livingEntity = champion.getLivingEntity();

        if (livingEntity instanceof Mob mobEntity) {
            this.getGoals(champion)
                    .forEach(goal -> mobEntity.goalSelector.addGoal(goal.getA(), goal.getB()));
        }
    }

    @Override
    public boolean canApply(IChampion champion) {
        LivingEntity livingEntity = champion.getLivingEntity();
        return livingEntity instanceof Mob && super.canApply(champion);
    }

    public abstract List<Tuple<Integer, Goal>> getGoals(IChampion champion);
}
