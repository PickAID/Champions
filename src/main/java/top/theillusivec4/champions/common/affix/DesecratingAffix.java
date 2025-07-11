package top.theillusivec4.champions.common.affix;

import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.BasicAffix;
import top.theillusivec4.champions.common.affix.core.GoalCombatAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;

import java.util.Collections;
import java.util.List;

public class DesecratingAffix extends GoalCombatAffix {
	@Override
	public boolean onAttacked(IChampion champion, DamageSource source, float amount) {
		return !(source.getEntity() instanceof AreaEffectCloud)
				|| source.getDirectEntity() != champion.getLivingEntity();
	}

	@Override
	public List<Tuple<Integer, Goal>> getGoals(IChampion champion) {
		return Collections
				.singletonList(new Tuple<>(0, new DesecrateGoal((Mob) champion.getLivingEntity())));
	}

	@Override
	public AffixSetting createDefaultSetting() {
		return AffixSetting.builder()
				.withDefault()
				.setCategory(AffixCategory.OFFENSE)
				.build();
	}

	public static class DesecrateGoal extends Goal {

		private final Mob mobEntity;
		private int attackTime;

		public DesecrateGoal(final Mob mobEntity) {
			this.mobEntity = mobEntity;
		}

		@Override
		public void start() {
			this.attackTime = ChampionsConfig.desecratingCloudInterval * 20;
		}

		@Override
		public void tick() {
			LivingEntity target = this.mobEntity.getTarget();
			this.attackTime--;

			if (this.attackTime <= 0 && target != null) {
				this.attackTime =
						ChampionsConfig.desecratingCloudInterval * 20 +
								this.mobEntity.getRandom().nextInt(5) * 10;
				AreaEffectCloud cloud = new AreaEffectCloud(target.level(),
						target.position().x, target.position().y, target.position().z);
				cloud.setOwner(this.mobEntity);
				cloud.setRadius((float) ChampionsConfig.desecratingCloudRadius);
				cloud.setDuration(ChampionsConfig.desecratingCloudDuration * 20);
				cloud.setRadiusOnUse(-0.5F);
				cloud.setWaitTime(ChampionsConfig.desecratingCloudActivationTime * 20);
				cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
				cloud.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
				target.level().addFreshEntity(cloud);
			}
		}

		@Override
		public boolean canUse() {
			return BasicAffix.canTarget(this.mobEntity, this.mobEntity.getTarget(), true);
		}
	}
}
