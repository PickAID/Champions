package top.theillusivec4.champions.api;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import top.theillusivec4.champions.common.util.Utils;

public final class ChampionHandlerEventListener {

  public static void register() {
    NeoForge.EVENT_BUS.register(new ChampionHandlerEventListener());
  }

  private ChampionHandlerEventListener() {
  }

  @SubscribeEvent
  public void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
    Entity victim = event.getEntity();
    DamageSource damageSource = event.getSource();
    Utils.getChampionHandler(victim).ifPresent(handler -> {
      boolean invulnerable = handler.isImmuneToDamage((ServerLevel) victim.level(), damageSource);
      if (invulnerable) {
        event.setCanceled(true);
      }
    });
  }

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
      float modifiedDamage = Utils.getChampionHandler(attacker)
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
      float protection = Utils.getChampionHandler(victim).map(handler -> {
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

  @SubscribeEvent
  public void onLivingDamagePost(LivingDamageEvent.Post event) {
    Utils.getChampionHandler(event.getEntity()).ifPresent(handler -> {
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

  @SubscribeEvent
  public void onEntityTickPre(EntityTickEvent.Pre event) {
    if (event.getEntity().level() instanceof ServerLevel serverLevel) {
      Utils.getChampionHandler(event.getEntity()).ifPresent(handler -> handler.tickEffects(serverLevel));
    }
  }

}
