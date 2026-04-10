package top.theillusivec4.champions.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.affix.AffixHelper;
import top.theillusivec4.champions.champion.ChampionHelper;
import top.theillusivec4.champions.world.effect.ChampionsMobEffects;

@EventBusSubscriber(modid = ChampionsMod.MOD_ID)
public final class EntityEventHandler {

  private EntityEventHandler() {
  }

  /**
   * 处理治疗值
   *
   * @param event LivingHealEvent
   */
  @SubscribeEvent
  private static void onLivingHeal(LivingHealEvent event) {
    Entity entity = event.getEntity();
    MutableFloat heal = new MutableFloat(event.getAmount());
    if (entity.level() instanceof ServerLevel level) {
      if (entity instanceof LivingEntity livingEntity) {
        MobEffectInstance instance = livingEntity.getEffect(ChampionsMobEffects.WOUND);
        if (instance != null) {
          heal.setValue(heal.floatValue() - heal.floatValue() * 0.1f * instance.getAmplifier());
        }
      }

      float result = AffixHelper.modifyHeal(level, entity, heal.floatValue());
      event.setAmount(Math.max(result, 0.0f));
      ChampionHelper.updateBossbarProgress(entity);
    }

  }

  /**
   * 处理动态的伤害免疫事件
   *
   * @param event LivingIncomingDamageEvent
   */
  @SubscribeEvent
  private static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
    Entity victim = event.getEntity();
    DamageSource source = event.getSource();

    if (victim instanceof LivingEntity livingEntity) {
      MobEffectInstance mobEffectInstance = livingEntity.getEffect(ChampionsMobEffects.SHIELD);
      if (mobEffectInstance != null) {
        livingEntity.level().playSound(null, livingEntity.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1.0f, 1.0f);
        livingEntity.removeEffect(ChampionsMobEffects.SHIELD);
        event.setCanceled(true);
        return;
      }
    }

    boolean immune = AffixHelper.isImmuneToDamage((ServerLevel) victim.level(), victim, source);
    if (immune) {
      event.setCanceled(true);
    }
  }

  /**
   * 处理伤害修改、伤害减免等
   *
   * @param event LivingDamageEvent.Pre
   */
  @SubscribeEvent
  private static void onLivingDamagePre(LivingDamageEvent.Pre event) {
    Entity victim = event.getEntity();
    MutableFloat damage = new MutableFloat(event.getOriginalDamage());
    DamageSource source = event.getSource();

    if (victim.level() instanceof ServerLevel level) {
      /*
      MobEffect
     */
      if (victim instanceof LivingEntity livingEntity) {
        MobEffectInstance mobEffectInstance = livingEntity.getEffect(ChampionsMobEffects.WOUND);
        if (mobEffectInstance != null) {
          damage.setValue(damage.floatValue() + damage.floatValue() * 0.1f * mobEffectInstance.getAmplifier());
        }
      }

    /*
      伤害增幅
     */
      if (source.getEntity() != null) {
        Entity attacker = source.getEntity();
        damage.setValue(
//						ChampionUtil.getHandler(attacker).map(handler -> handler.modifyDamage((ServerLevel) attacker.level(), victim, source, damage.floatValue())).orElse(damage.floatValue())
          AffixHelper.modifyDamage(level, victim, source, damage.floatValue())
        );
      }

    /*
      伤害减免
      伤害值 = 原值 - 原值*减免比例
      减免比例取值范围: [-1024.0f, 1.0]
     */

      if (damage.floatValue() > 0.0f) {
//				float protection = ChampionUtil.getHandler(victim).map(handler -> {
//					float originProtection = handler.getDamageProtection(level, victim, event.getSource());
//					return Math.clamp(originProtection, -1024.0f, Champions.getInstance().getCommonConfig().getMaxDamageProtection());
//				}).orElse(0.0f);
        float protection = AffixHelper.getDamageProtection(level, victim, event.getSource());
        damage.setValue(damage.floatValue() * (1.0f - protection));
      }

    /*
    防止造成负伤害，预期外的行为
     */
      event.setNewDamage(Math.max(damage.floatValue(), 0.0f));
    }

  }

  /**
   * 处理受伤后的相关逻辑
   * 记录上次攻击的相关数据
   *
   * @param event LivingDamageEvent.Post
   */
  @SubscribeEvent
  private static void onLivingDamagePost(LivingDamageEvent.Post event) {
    ServerLevel level = (ServerLevel) event.getEntity().level();
    Entity entity = event.getEntity();
    DamageSource source = event.getSource();
    float origin = event.getOriginalDamage();
//		ChampionUtil.getHandler(entity).ifPresent(handler -> {
//			DamageSource damageSource = event.getSource();
//			Holder<DamageType> damageType = damageSource.typeHolder();
//			handler.updateLatestDamage(damageType, event.getOriginalDamage());
//
//			// BossBar
//			handler.getBossEvent().ifPresent(bossEvent -> bossEvent.setProgress(handler.getHealth() / handler.getMaxHealth()));
//		});
//    AffixHelper.updateLatestDamage(entity, source, origin);
    ChampionHelper.updateBossbarProgress(entity);

  }


  /**
   * 处理 Tick 效果
   *
   * @param event EntityTickEvent.Pre
   */
  @SubscribeEvent
  private static void onEntityTickPre(EntityTickEvent.Pre event) {
    Entity entity = event.getEntity();
    if (entity.level() instanceof ServerLevel level) {
      AffixHelper.tickEffects(level, entity);
      AffixHelper.targetEffects(level, entity);
      ChampionHelper.updateBossbarPlayers(entity);
    } else {
      ChampionHelper.doParticlesEffects(entity);
    }
  }

  @SubscribeEvent
  private static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
    Level level = event.getLevel();
    Entity entity = event.getEntity();
    if (!level.isClientSide()) {
      ChampionHelper.removeBoss(entity);
    }
  }

  @SubscribeEvent
  private static void onLivingDeath(LivingDeathEvent event) {
    DamageSource source = event.getSource();
    Entity attacker = source.getEntity();
    LivingEntity victim = event.getEntity();
    Level level = victim.level();
//    if (!AffixHelper.getAffixContainer(victim).isEmpty() && attacker instanceof Player player) {
//      player.awardStat(Stats.CHAMPION_MOBS_KILLED.get());
//    }
  }

  /**
   * 处理实体转换的词条复制
   *
   * @param event LivingConversionEvent
   */
  @SubscribeEvent
  private static void onLivingConversionPost(LivingConversionEvent.Post event) {
    ServerLevel level = (ServerLevel) event.getEntity().level();
    Entity from = event.getEntity();
    Entity to = event.getOutcome();
//    AffixHelper.updateFromEntity(level, to, from);
  }

  /**
   * 处理实体分裂的数据复制
   *
   * @param event MobSplitEvent
   */
  @SubscribeEvent
  private static void onMobSplit(MobSplitEvent event) {
    ServerLevel level = (ServerLevel) event.getParent().level();
    Entity parent = event.getParent();
    for (Mob child : event.getChildren()) {
//      AffixHelper.updateFromEntity(level, child, parent);
    }
  }

  /*
   * 注入到Mob初始化过程。
   * 对于刷怪蛋，如果刷怪蛋不附带实体数据，会触发FinalizeSpawnEvent事件，我不知道这是否合乎预期。
   */
  @SubscribeEvent
  private static void onFinalizeSpawn(FinalizeSpawnEvent event) {
    if (event.getLevel() instanceof ServerLevel level) {
      Mob mob = event.getEntity();
      double x = event.getX();
      double y = event.getY();
      double z = event.getZ();
      DifficultyInstance instance = event.getDifficulty();
      MobSpawnType reason = event.getSpawnType();
      AffixHelper.doFinalizeSpawn(level, mob, x, y, z, instance, reason);
    }
  }

}
