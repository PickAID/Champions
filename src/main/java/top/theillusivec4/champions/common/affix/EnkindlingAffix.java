package top.theillusivec4.champions.common.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.world.Difficulty;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.BasicAffix;
import top.theillusivec4.champions.common.affix.core.GoalAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.entity.EnkindlingBulletEntity;

import java.util.Collections;
import java.util.List;

public class EnkindlingAffix extends GoalAffix {

	@Override
	public List<Tuple<Integer, Goal>> getGoals(IChampion champion) {
		return Collections
				.singletonList(new Tuple<>(0, new AttackGoal((MobEntity) champion.getLivingEntity())));
	}

	@Override
	public AffixSetting createDefaultSetting() {
		return AffixSetting.builder()
				.withDefault()
				.setCategory(AffixCategory.OFFENSE)
				.build();
	}

	static class AttackGoal extends Goal {
		private final MobEntity mobEntity;

		private int attackTime;

		public AttackGoal(MobEntity mobEntity) {
			this.mobEntity = mobEntity;
		}

		@Override
		public boolean canUse() {
			LivingEntity livingentity = this.mobEntity.getTarget();

			if (livingentity != null && livingentity.isAlive()) {
				return BasicAffix.canTarget(this.mobEntity, livingentity, true)
						&& this.mobEntity.level.getDifficulty() != Difficulty.PEACEFUL;
			} else {
				return false;
			}
		}

		@Override
		public void start() {
			this.attackTime = ChampionsConfig.enkindlingAttackInterval * 20;
		}

		@Override
		public void tick() {

			if (this.mobEntity.level.getDifficulty() != Difficulty.PEACEFUL) {
				--this.attackTime;
				LivingEntity livingentity = this.mobEntity.getTarget();

				if (livingentity != null) {
					this.mobEntity.getLookControl().setLookAt(livingentity, 180.0F, 180.0F);
					double sqrDistance = this.mobEntity.distanceToSqr(livingentity);

					if (sqrDistance < 400.0D) {
						if (this.attackTime <= 0) {
							this.attackTime = ChampionsConfig.enkindlingAttackInterval * 20
									+ this.mobEntity.getRandom().nextInt(10) * 20 / 2;
							this.mobEntity.level.addFreshEntity(
									new EnkindlingBulletEntity(this.mobEntity.level, this.mobEntity,
											livingentity, this.mobEntity.getMotionDirection().getAxis()));
							this.mobEntity.playSound(SoundEvents.SHULKER_SHOOT,
									2.0F, (this.mobEntity.getRandom().nextFloat() -
											this.mobEntity.getRandom().nextFloat()) * 0.2F + 1.0F);
						}
					} else {
						this.mobEntity.setTarget(null);
					}
				}
				super.tick();
			}
		}
	}
}
