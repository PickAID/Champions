package top.theillusivec4.champions.common.affix;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.AbstractBasicAffix;
import top.theillusivec4.champions.common.affix.core.AffixData;
import top.theillusivec4.champions.common.affix.core.GoalCombatAffix;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.registry.ModEntityTypes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfestedAffix extends GoalCombatAffix {

  private static void spawnParasites(LivingEntity livingEntity, int amount,
                                     @Nullable LivingEntity target, ServerLevel world) {
    boolean isEnder = livingEntity.is(ModEntityTypes.Tags.IS_ENDER);
    EntityType<?> type =
      isEnder ? ChampionsConfig.infestedEnderParasite : ChampionsConfig.infestedParasite;
    List<Mob> children = new ArrayList<>();

    for (int i = 0; i < amount; i++) {
      var entity = type
        .create(world, null, livingEntity.blockPosition(), EntitySpawnReason.MOB_SUMMONED,
          false, false);
      if (entity instanceof Monster monster) {
        children.add(monster);
      }
    }

    if (!EventHooks.onMobSplit((Mob) livingEntity, children).isCanceled()) {
      children.forEach(child -> {
        world.addFreshEntity(child);
        child.spawnAnim();
        child.setLastHurtByMob(target);
        child.setTarget(target);
      });
    }

  }

  @Override
  public AffixSetting createDefaultSetting() {
    return AffixSetting.builder()
      .withDefault()
      .setCategory(AffixCategory.OFFENSE)
      .build();
  }


  @Override
  public void onInitialSpawn(IChampion champion) {
    AffixData.IntegerData buffer =
      AffixData.getData(champion, this.toString(), AffixData.IntegerData.class);
    buffer.num = Math.min(ChampionsConfig.infestedTotal, Math.max(1,
      (int) (champion.getLivingEntity().getMaxHealth() * ChampionsConfig.infestedPerHealth)));
    buffer.saveData();
  }

  @Override
  public float onHeal(IChampion champion, float amount, float newAmount) {
    if (newAmount > 0 && champion.getLivingEntity().getRandom().nextFloat() < 0.5F) {
      AffixData.IntegerData buffer = AffixData
        .getData(champion, this.toString(), AffixData.IntegerData.class);
      buffer.num = Math.min(ChampionsConfig.infestedTotal, buffer.num + 2);
      buffer.saveData();
      return Math.max(0, newAmount - 1);
    }
    return newAmount;
  }

  @Override
  public boolean onDeath(IChampion champion, DamageSource source) {
    AffixData.IntegerData buffer = AffixData
      .getData(champion, this.toString(), AffixData.IntegerData.class);
    LivingEntity target = null;

    if (source.getDirectEntity() instanceof LivingEntity) {
      target = (LivingEntity) source.getDirectEntity();
    }
    Level world = champion.getLivingEntity().level();

    if (world instanceof ServerLevel serverLevel) {
      spawnParasites(champion.getLivingEntity(), buffer.num, target, serverLevel);
    }
    return true;
  }

  @Override
  public List<Tuple<Integer, Goal>> getGoals(IChampion champion) {
    return Collections.singletonList(
      new Tuple<>(0, new SpawnParasiteGoal((Mob) champion.getLivingEntity())));
  }

  @Override
  public boolean canApply(IChampion champion) {
    EntityType<?> type = champion.getLivingEntity().getType();
    return type != ChampionsConfig.infestedParasite && type != ChampionsConfig.infestedEnderParasite
      && super.canApply(champion);
  }

  private class SpawnParasiteGoal extends Goal {
    private final Mob mobEntity;
    private int attackTime;

    public SpawnParasiteGoal(Mob mobEntity) {
      this.mobEntity = mobEntity;
    }

    @Override
    public void start() {
      this.attackTime = ChampionsConfig.infestedInterval * 20;
    }

    @Override
    public void tick() {
      this.attackTime--;

      if (this.attackTime <= 0) {
        ChampionAttachment.getAttachment(this.mobEntity).ifPresent(champion -> {
          AffixData.IntegerData buffer = AffixData
            .getData(champion, InfestedAffix.this.toString(), AffixData.IntegerData.class);

          if (buffer.num > 0 && this.mobEntity.level() instanceof ServerLevel serverLevel) {
            this.attackTime =
              ChampionsConfig.infestedInterval * 20 + this.mobEntity.getRandom().nextInt(5) * 10;
            int amount = ChampionsConfig.infestedAmount;
            spawnParasites(this.mobEntity, amount, this.mobEntity.getTarget(), serverLevel);
            buffer.num = Math.max(0, buffer.num - amount);
            buffer.saveData();
          }
        });
      }
    }

    @Override
    public boolean canUse() {
      return AbstractBasicAffix.canTarget(this.mobEntity, this.mobEntity.getTarget(), true);
    }
  }
}
