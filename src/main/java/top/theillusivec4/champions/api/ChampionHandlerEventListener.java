package top.theillusivec4.champions.api;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import top.theillusivec4.champions.api.affix.event.ChampionEvent;
import top.theillusivec4.champions.common.util.Utils;

public final class ChampionHandlerEventListener {

  public static void register() {
    NeoForge.EVENT_BUS.register(new ChampionHandlerEventListener());
  }

  private ChampionHandlerEventListener() {
  }

  @SubscribeEvent
  public void onUpdateAffixesPre(ChampionEvent.UpdateAffixes.Pre event) {
    event.getHandler().removeAttributeModifier();
  }

  @SubscribeEvent
  public void onUpdateAffixesPost(ChampionEvent.UpdateAffixes.Post event) {
    event.getHandler().addAttributeModifier();
  }


  @SubscribeEvent
  public void onLivingDamagePre(LivingDamageEvent.Pre event) {
    // 伤害减免组件，如：适应， 0.15f * 攻击次数
    Utils.getChampionHandler(event.getEntity()).ifPresent(handler -> {
      /*
      新伤害值 = 原伤害值 - 原伤害值 * 减免比例
       */
      float protection = handler.getDamageProtection((ServerLevel) event.getEntity().level(), event.getSource());
      float originalDamage = event.getOriginalDamage();
      event.setNewDamage(originalDamage - originalDamage * Math.clamp(protection, 0.0f, 1.0f));
    });
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

  @SubscribeEvent
  public void onEntityJoinLevel(EntityJoinLevelEvent event) {
    if (event.getLevel() instanceof ServerLevel serverLevel && !event.loadedFromDisk()) {
      Utils.getChampionHandler(event.getEntity()).ifPresent(handler -> handler.doSpawnEffects(serverLevel));
    }
  }
}
