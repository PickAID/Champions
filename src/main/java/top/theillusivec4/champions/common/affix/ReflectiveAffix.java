package top.theillusivec4.champions.common.affix;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.affix.core.BasicAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.registry.ModDamageTypes;

public class ReflectiveAffix extends BasicAffix {

  @SubscribeEvent
  public void onDamageEvent(LivingDamageEvent.Pre evt) {
    if (!ChampionsConfig.reflectiveLethal && evt.getSource().is(ModDamageTypes.REFLECTION_DAMAGE)) {
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
      if (source.is(ModDamageTypes.REFLECTION_DAMAGE) || source.is(DamageTypes.THORNS)) {
        return newAmount;
      }

      var newSource = ModDamageTypes.of(ModDamageTypes.REFLECTION_DAMAGE, champion.getLivingEntity());

      if (source.getEntity() != null) {
        newSource = ModDamageTypes.of(ModDamageTypes.REFLECTION_DAMAGE, source.getDirectEntity(), champion.getLivingEntity());
      }
      float min = (float) ChampionsConfig.reflectiveMinPercent;
      float damage = (float) Math.min(amount * (sourceEntity.getRandom().nextFloat() * (ChampionsConfig.reflectiveMaxPercent - min) + min), ChampionsConfig.reflectiveMax);

      sourceEntity.hurt(newSource, damage);
    }
    return newAmount;
  }
}
