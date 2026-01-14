package top.theillusivec4.champions.world.entity;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.ChampionHandler;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.entity.ChampionHandlerEntity;
import top.theillusivec4.champions.particle.ParticleTypes;
import top.theillusivec4.champions.server.champion.config.EntitySettingHolder;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;
import top.theillusivec4.champions.stats.Stats;
import top.theillusivec4.champions.world.effect.MobEffects;

public final class EntityEventListener {
  private static final double BOSS_EVENT_DISTANCE_SQR = 3025.0;

  public static void register() {
    NeoForge.EVENT_BUS.register(new EntityEventListener());
  }

  private EntityEventListener() {
  }

  /**
   * 处理治疗值
   *
   * @param event LivingHealEvent
   */
  @SubscribeEvent
  public void onLivingHeal(LivingHealEvent event) {
    Entity entity = event.getEntity();
    MutableFloat heal = new MutableFloat(event.getAmount());
    if (entity.level() instanceof ServerLevel level) {
      if (entity instanceof LivingEntity livingEntity) {
        MobEffectInstance instance = livingEntity.getEffect(MobEffects.WOUND);
        if (instance != null) {
          heal.setValue(heal.floatValue() - heal.floatValue() * 0.1f * instance.getAmplifier());
        }
      }

      ChampionUtil.getHandler(entity).ifPresent(handler -> {
        float result = handler.modifyHeal(level, entity, heal.floatValue());
        // 治疗不应该出现负数
        event.setAmount(Math.max(result, 0.0f));
        // BossBar
        handler.getBossEvent().ifPresent(bossEvent -> bossEvent.setProgress(handler.getHealth() / handler.getMaxHealth()));
      });
    }

  }

  /**
   * 处理动态的伤害免疫事件
   *
   * @param event LivingIncomingDamageEvent
   */
  @SubscribeEvent
  public void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
    Entity victim = event.getEntity();
    DamageSource damageSource = event.getSource();

    if (victim instanceof LivingEntity livingEntity) {
      MobEffectInstance mobEffectInstance = livingEntity.getEffect(MobEffects.SHIELD);
      if (mobEffectInstance != null) {
        livingEntity.level().playSound(null, livingEntity.blockPosition(), SoundEvents.PLAYER_ATTACK_NODAMAGE, SoundSource.AMBIENT, 1.0f, 1.0f);
        livingEntity.removeEffect(MobEffects.SHIELD);
        event.setCanceled(true);
        return;
      }
    }

    ChampionUtil.getHandler(victim).ifPresent(handler -> {
      boolean invulnerable = handler.isImmuneToDamage((ServerLevel) victim.level(), victim, damageSource);
      if (invulnerable) {
        event.setCanceled(true);
      }
    });
  }

  /**
   * 处理伤害修改、伤害减免等
   *
   * @param event LivingDamageEvent.Pre
   */
  @SubscribeEvent
  public void onLivingDamagePre(LivingDamageEvent.Pre event) {
    Entity victim = event.getEntity();
    MutableFloat damage = new MutableFloat(event.getOriginalDamage());
    DamageSource source = event.getSource();

    if (victim.level() instanceof ServerLevel level) {
      /*
      MobEffect
     */
      if (victim instanceof LivingEntity livingEntity) {
        MobEffectInstance mobEffectInstance = livingEntity.getEffect(MobEffects.WOUND);
        if (mobEffectInstance != null) {
          damage.setValue(damage.floatValue() * 0.1f * mobEffectInstance.getAmplifier());
        }
      }

    /*
      伤害增幅
     */
      if (source.getEntity() != null) {
        Entity attacker = source.getEntity();
        damage.setValue(
          ChampionUtil.getHandler(attacker).map(handler -> handler.modifyDamage((ServerLevel) attacker.level(), victim, source, damage.floatValue())).orElse(damage.floatValue())
        );
      }

    /*
      伤害减免
      伤害值 = 原值 - 原值*减免比例
      减免比例取值范围: [-1024.0f, 1.0]
     */

      if (damage.floatValue() > 0.0f) {
        float protection = ChampionUtil.getHandler(victim).map(handler -> {
          float originProtection = handler.getDamageProtection(level, victim, event.getSource());
          return Math.clamp(originProtection, -1024.0f, 1.0f);
        }).orElse(0.0f);

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
  public void onLivingDamagePost(LivingDamageEvent.Post event) {
    Entity entity = event.getEntity();
    ChampionUtil.getHandler(entity).ifPresent(handler -> {
      DamageSource damageSource = event.getSource();
      Holder<DamageType> damageType = damageSource.typeHolder();
      handler.updateLatestDamage(damageType, event.getOriginalDamage());

      // BossBar
      handler.getBossEvent().ifPresent(bossEvent -> bossEvent.setProgress(handler.getHealth() / handler.getMaxHealth()));
    });
  }


  /**
   * 处理 Tick 效果
   *
   * @param event EntityTickEvent.Pre
   */
  @SubscribeEvent
  public void onEntityTickPre(EntityTickEvent.Pre event) {
    Entity entity = event.getEntity();
    if (entity.level() instanceof ServerLevel level) {
      ChampionUtil.getHandler(entity).ifPresent(handler -> {
        handler.tickEffects(level, entity);
        // BossBar
        handler.getBossEvent().ifPresent(bossEvent -> {
          for (ServerPlayer player : level.players()) {
            if (player.blockPosition().distSqr(entity.blockPosition()) <= BOSS_EVENT_DISTANCE_SQR) {
              bossEvent.addPlayer(player);
            } else {
              bossEvent.removePlayer(player);
            }
          }
        });

      });

    } else {
      ChampionUtil.getHandler(entity).ifPresent(handler -> {
        if (handler.spawnParticles()) {
          RandomSource randomSource = entity.getRandom();
          Vec3 position = entity.position();
          double x = position.x() + (randomSource.nextDouble() - 0.5) * entity.getBbWidth();
          double y = position.y() + randomSource.nextDouble() * entity.getBbHeight();
          double z = position.z() + (randomSource.nextDouble() - 0.5) * entity.getBbWidth();
          int color = handler.getColorOrDefault();
          entity.level().addParticle(ParticleTypes.rank(color), x, y, z, 1.0f, 1.0f, 1.0f);
        }
      });
    }
  }

  @SubscribeEvent
  public void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
    Level level = event.getLevel();
    Entity entity = event.getEntity();
    if (!level.isClientSide()) {
      ChampionUtil.getHandler(entity).flatMap(ChampionHandlerEntity::getBossEvent).ifPresent(ServerChampionBossEvent::removeAllPlayers);
    }
  }

  @SubscribeEvent
  public void onLivingDeath(LivingDeathEvent event) {
    DamageSource source = event.getSource();
    Entity attacker = source.getEntity();
    LivingEntity victim = event.getEntity();
    Level level = victim.level();
    boolean flag = ChampionUtil.getHandler(victim).map(ChampionHandler::isValid).orElse(false);
    if (!level.isClientSide() && flag && attacker != null && attacker.getType() == EntityType.PLAYER) {
      ((Player) attacker).awardStat(Stats.CHAMPION_MOBS_KILLED.get());
    }
  }

  /**
   * 处理实体转换的词条复制
   *
   * @param event LivingConversionEvent
   */
  @SubscribeEvent
  public void onLivingConversionPost(LivingConversionEvent.Post event) {
    Entity entity = event.getEntity();
    Entity newEntity = event.getOutcome();
    ChampionUtil.getHandler(newEntity).ifPresent(handler -> ChampionUtil.getHandler(entity).ifPresent(handler1 -> handler.applyConfig(handler1.deriveConfig())));
  }

  /**
   * 处理实体分裂的数据复制
   *
   * @param event MobSplitEvent
   */
  @SubscribeEvent
  public void onMobSplit(MobSplitEvent event) {
    Entity parent = event.getParent();
    ChampionUtil.getHandler(parent).ifPresent(handler -> {
      for (Mob child : event.getChildren()) {
        ChampionUtil.getHandler(child).ifPresent(handler1 -> handler1.applyConfig(handler.deriveConfig()));
      }
    });
  }

  /*
   * 注入到Mob初始化过程。
   * 对于刷怪蛋，如果刷怪蛋不附带实体数据，会触发FinalizeSpawnEvent事件，我不知道这是否合乎预期。
   */
  @SubscribeEvent
  public void onFinalizeSpawn(FinalizeSpawnEvent event) {
    if (event.getLevel() instanceof ServerLevel serverLevel) {
      Entity entity = event.getEntity();
      ChampionUtil.getHandler(entity).ifPresent(handler -> {
        if (handler.finalizeSpawn()) {
          Identifier id = EntityType.getKey(entity.getType());
          EntitySettingHolder selectorHolder = Champions.getInstance().getChampionConfigSelectorManager().byId(id);
          if (selectorHolder != null) {
            selectorHolder.value().select(serverLevel, entity, entity instanceof Mob mob ? mob.getSpawnType() : null)
              .ifPresent(handler::applyConfig);
          }
        }
      });

    }
  }


}
