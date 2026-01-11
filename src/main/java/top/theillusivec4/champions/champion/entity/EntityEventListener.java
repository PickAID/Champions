package top.theillusivec4.champions.champion.entity;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.particle.ParticleTypes;
import top.theillusivec4.champions.server.champion.config.ChampionConfigSelectorHolder;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;

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
    ChampionUtil.getHandler(entity).ifPresent(handler -> {
      float amount = handler.modifyHeal((ServerLevel) entity.level(), event.getAmount());
      // 治疗理应不应该出现负数
      event.setAmount(Math.max(amount, 0.0f));
    });
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
    ChampionUtil.getHandler(victim).ifPresent(handler -> {
      boolean invulnerable = handler.isImmuneToDamage((ServerLevel) victim.level(), damageSource);
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
    float damage = event.getOriginalDamage();
    DamageSource damageSource = event.getSource();
    /*
    伤害修改
     */
    // 攻击者
    if (damageSource.getEntity() != null) {
      Entity attacker = damageSource.getEntity();
      float modifiedDamage = ChampionUtil.getHandler(attacker).map(handler -> handler.modifyDamage((ServerLevel) attacker.level(), victim, damageSource, event.getOriginalDamage())).orElse(event.getOriginalDamage());
      damage = Math.max(modifiedDamage, 0.0f);
    }
//    // 受害者
//    float finalDamage = damage;
//    damage = Utils.getChampionHandler(victim)
//      .map(handler -> handler.modifyDamage((ServerLevel) victim.level(), AffixTarget.VICTIM, victim, damageSource, finalDamage))
//      .orElse(damage);

    /*
    伤害减免
    伤害值 = 原值 - 原值*减免比例
    减免比例取值范围: [-1024.0f, 1.0]
     */
    if (damage > 0.0f) {
      float protection = ChampionUtil.getHandler(victim).map(handler -> {
        float damageProtection = handler.getDamageProtection((ServerLevel) event.getEntity().level(), event.getSource());
        return Math.clamp(damageProtection, -1024.0f, 1.0f);
      }).orElse(0.0f);

      damage = damage - damage * protection;
    }

    /*
    防止造成负伤害，预期外的行为
     */
    event.setNewDamage(Math.max(damage, 0.0f));
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
      Holder<DamageType> damage = damageSource.typeHolder();
      handler.updateLatestDamage(mutable -> {
        if (mutable.getDamageType() == damageSource.typeHolder()) {
          mutable.setDamageCount(mutable.getDamageCount() + 1);
        } else {
          mutable.setDamageType(damage).setDamageCount(1);
        }

        mutable.setLatestTime(mutable.getLatestTime() + 1);
        mutable.setOriginalDamageAmount(mutable.getOriginalDamageAmount() + event.getOriginalDamage());
      });

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
    if (entity.level() instanceof ServerLevel serverLevel) {
      ChampionUtil.getHandler(event.getEntity()).ifPresent(handler -> {
        handler.tickEffects(serverLevel);
        // BossBar
        handler.getBossEvent().ifPresent(bossEvent -> {
          for (ServerPlayer player : serverLevel.players()) {
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
//        float red = ARGB.red(color) / 255.0f;
//        float green = ARGB.green(color) / 255.0f;
//        float blue = ARGB.blue(color) / 255.0f;
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

  /**
   * 使用进入维度事件来执行实体初始化
   */
  @SubscribeEvent
  public void onEntityJoinLevel(EntityJoinLevelEvent event) {
    if (event.getLevel() instanceof ServerLevel serverLevel && !event.loadedFromDisk()) {
      Entity entity = event.getEntity();
      ChampionUtil.getHandler(entity).ifPresent(handler -> {
        if (handler.finalizeSpawn()) {
          Identifier id = EntityType.getKey(entity.getType());
          ChampionConfigSelectorHolder selectorHolder = Champions.getInstance().getChampionConfigSelectorManager().byId(id);
          if (selectorHolder != null) {
            selectorHolder.value().select(serverLevel, entity, entity instanceof Mob mob ? mob.getSpawnType() : null)
              .ifPresent(handler::applyConfig);
          }
        }
      });

    }
  }

  /**
   * 注入到Mob初始化过程。
   * 对于刷怪蛋，如果刷怪蛋不附带实体数据，会触发FinalizeSpawnEvent事件，我不知道这是否合乎预期。
   */
//  @SubscribeEvent
//  public void onFinalizeSpawn(FinalizeSpawnEvent event) {
//    if (event.getLevel() instanceof ServerLevel serverLevel) {
//      Entity entity = event.getEntity();
//      ChampionUtil.getHandler(entity).ifPresent(handler -> {
//        if (handler.shouldApplyConfigOnSpawn()) {
//          Identifier id = EntityType.getKey(entity.getType());
//          ChampionConfigSelectorHolder selectorHolder = Champions.getInstance().getChampionConfigSelectorManager().byId(id);
//          if (selectorHolder != null) {
//            selectorHolder.value().select(serverLevel, entity, entity instanceof Mob mob ? mob.getSpawnType() : null)
//              .ifPresent(handler::applyConfig);
//          }
//        }
//      });
//
//    }
//  }


}
