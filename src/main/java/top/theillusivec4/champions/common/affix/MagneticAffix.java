package top.theillusivec4.champions.common.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.vector.Vector3d;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.BasicAffix;
import top.theillusivec4.champions.common.affix.core.GoalAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;

import java.util.Collections;
import java.util.List;

public class MagneticAffix extends GoalAffix {

	@Override
	public List<Tuple<Integer, Goal>> getGoals(IChampion champion) {
		return Collections.singletonList(
				new Tuple<>(0, new PullGoal((MobEntity) champion.getLivingEntity())));
	}

	@Override
	public AffixSetting createDefaultSetting() {
		return AffixSetting.builder()
				.withDefault()
				.build();
	}

	public static class PullGoal extends Goal {
		final MobEntity mobEntity;
		LivingEntity target = null;

		public PullGoal(final MobEntity mobEntity) {
			this.mobEntity = mobEntity;
		}

		@Override
		public void start() {
			target = mobEntity.getTarget();
			super.start();
		}

		@Override
		public void stop() {
			target = null;
			super.stop();
		}

		@Override
		public boolean canUse() {
			return mobEntity.getTarget() != null && BasicAffix
					.canTarget(mobEntity, mobEntity.getTarget(), true)
					&& mobEntity.tickCount % 40 == 0 && mobEntity.getRandom().nextDouble() < 0.4D;
		}

		@Override
		public boolean canContinueToUse() {
			return mobEntity.tickCount % 40 != 0 || mobEntity.getRandom().nextDouble() > 0.7D;
		}

		@Override
		public void tick() {
			double x = mobEntity.position().x;
			double y = mobEntity.position().y;
			double z = mobEntity.position().z;
			double strength = ChampionsConfig.magneticStrength;
			Vector3d vec = new Vector3d(x, y, z).subtract(
							new Vector3d(target.position().x, target.position().y, target.position().z)).normalize()
					.scale(strength);
			target.setDeltaMovement(vec);

			if (target instanceof PlayerEntity) {
				target.hurtMarked = true;
			}
		}
	}
}
