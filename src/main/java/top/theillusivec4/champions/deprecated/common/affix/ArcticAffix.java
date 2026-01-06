package top.theillusivec4.champions.deprecated.common.affix;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.api.data.AffixSetting;
import top.theillusivec4.champions.deprecated.common.affix.core.AbstractBasicAffix;
import top.theillusivec4.champions.deprecated.common.affix.core.GoalAffix;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.entities.ArcticBulletEntity;

import java.util.Collections;
import java.util.List;

@Deprecated
public class ArcticAffix extends GoalAffix {

  @Override
  public List<Tuple<Integer, Goal>> getGoals(IChampion champion) {
    return Collections.singletonList(
      new Tuple<>(0, new AttackGoal((Mob) champion.getLivingEntity())));
  }

  @Override
  public AffixSetting createDefaultSetting() {
    return AffixSetting.builder()
      .withDefault()
      .build();
  }

  static class AttackGoal extends Goal {
    private final Mob mobEntity;

    private int attackTime;

    public AttackGoal(Mob mobEntity) {
      this.mobEntity = mobEntity;
    }

    @Override
    public boolean canUse() {
      LivingEntity livingentity = this.mobEntity.getTarget();

      if (livingentity != null && livingentity.isAlive()) {
        return AbstractBasicAffix.canTarget(this.mobEntity, livingentity, true)
          && this.mobEntity.level().getDifficulty() != Difficulty.PEACEFUL;
      } else {
        return false;
      }
    }

    @Override
    public void start() {
      this.attackTime = ChampionsConfig.arcticAttackInterval * 20;
    }

    @Override
    public void tick() {
      if (this.mobEntity.level().getDifficulty() != Difficulty.PEACEFUL) {
        --this.attackTime;
        LivingEntity livingentity = this.mobEntity.getTarget();

        if (livingentity != null) {
          this.mobEntity.getLookControl().setLookAt(livingentity, 180.0F, 180.0F);
          double sqrDistance = this.mobEntity.distanceToSqr(livingentity);

          if (sqrDistance < 400.0D) {
            if (this.attackTime <= 0) {
              this.attackTime = ChampionsConfig.arcticAttackInterval * 20 +
                this.mobEntity.getRandom().nextInt(10) * 20 / 2;
              this.mobEntity.level().addFreshEntity(
                new ArcticBulletEntity(this.mobEntity.level(), this.mobEntity, livingentity,
                  this.mobEntity.getDirection().getAxis()));
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
