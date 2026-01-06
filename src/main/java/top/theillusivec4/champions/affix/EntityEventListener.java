package top.theillusivec4.champions.affix;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import top.theillusivec4.champions.util.ChampionUtil;

public final class EntityEventListener {
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
      float modifiedDamage = ChampionUtil.getHandler(attacker)
        .map(handler -> handler.modifyDamage((ServerLevel) attacker.level(), victim, damageSource, event.getOriginalDamage()))
        .orElse(event.getOriginalDamage());
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
    ChampionUtil.getHandler(event.getEntity()).ifPresent(handler -> {
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
    });
  }

  /**
   * 处理 Tick 效果
   *
   * @param event EntityTickEvent.Pre
   */
  @SubscribeEvent
  public void onEntityTickPre(EntityTickEvent.Pre event) {
    if (event.getEntity().level() instanceof ServerLevel serverLevel) {
      ChampionUtil.getHandler(event.getEntity()).ifPresent(handler -> handler.tickEffects(serverLevel));
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
    ChampionUtil.getHandler(newEntity).ifPresent(handler -> handler.copyFrom(entity));
  }

  /**
   * 处理实体分裂的词条复制
   *
   * @param event
   */
  @SubscribeEvent
  public void onMobSplit(MobSplitEvent event) {
    Entity parent = event.getParent();
    ChampionUtil.getHandler(parent).ifPresent(parentHandler -> {
      for (Mob child : event.getChildren()) {
        ChampionUtil.getHandler(child).ifPresent(childHandler -> {
          int level = parentHandler.getLevel();
          EntityAffixes entityAffixes = parentHandler.getAllAffixes();
          childHandler.setLevel(level - 1);
          childHandler.updateAffixes(mutable -> {
            for (Holder<Affix> affix : entityAffixes.getAffixes()) {
              mutable.add(affix);
            }
          });
        });
      }
    });
  }

}
