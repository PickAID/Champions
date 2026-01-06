package top.theillusivec4.champions.deprecated.common.affix;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.api.data.AffixCategory;
import top.theillusivec4.champions.deprecated.api.data.AffixSetting;
import top.theillusivec4.champions.deprecated.common.affix.core.CombatAffix;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.deprecated.common.damagesource.DamageTypes;

@Deprecated
public class ReflectiveAffix extends CombatAffix {

  @SubscribeEvent
  public void onDamageEvent(LivingDamageEvent.Pre evt) {
    if (!ChampionsConfig.reflectiveLethal && evt.getSource().is(DamageTypes.REFLECTION_DAMAGE)) {
      LivingEntity living = evt.getEntity();
      float currentDamage = evt.getOriginalDamage();

      if (currentDamage >= living.getHealth()) {
        evt.setNewDamage(living.getHealth() - 1);
      }
    }
  }

  @Override
  public float onDamage(IChampion champion, DamageSource source, float amount, float newAmount) {
    // getDirectEntity causing damage entity, example arrow entity
    // getEntity which is using weapon entity.
    // source which is causing champion damage source.
    if (source.getDirectEntity() instanceof LivingEntity sourceEntity) {
      // if damage source came from reflection damage ( applied damage to player) or came from self, skipping repeat damage reflection calculation.
      if (source.is(DamageTypes.REFLECTION_DAMAGE) || source.is(net.minecraft.world.damagesource.DamageTypes.THORNS)) {
        return newAmount;
      }

      var newSource = DamageTypes.of(DamageTypes.REFLECTION_DAMAGE, champion.getLivingEntity());

      if (source.getEntity() != null) {
        newSource = DamageTypes.of(DamageTypes.REFLECTION_DAMAGE, source.getDirectEntity(), champion.getLivingEntity());
      }
      float min = (float) ChampionsConfig.reflectiveMinPercent;
      float damage = (float) Math.min(amount * (sourceEntity.getRandom().nextFloat() * (ChampionsConfig.reflectiveMaxPercent - min) + min), ChampionsConfig.reflectiveMax);

      sourceEntity.hurt(newSource, damage);
    }
    return newAmount;
  }

  @Override
  public AffixSetting createDefaultSetting() {
    return AffixSetting.builder()
      .withDefault()
      .setCategory(AffixCategory.OFFENSE)
      .setHasSub()
      .build();
  }

}
