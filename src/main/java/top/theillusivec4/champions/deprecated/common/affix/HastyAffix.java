package top.theillusivec4.champions.deprecated.common.affix;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.api.data.AffixCategory;
import top.theillusivec4.champions.deprecated.api.data.AffixSetting;
import top.theillusivec4.champions.deprecated.common.affix.core.CombatLifeCycleAffix;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.util.Utils;

@Deprecated
public class HastyAffix extends CombatLifeCycleAffix {

  @Override
  public void onInitialSpawn(IChampion champion) {
    AttributeInstance speed = champion.getLivingEntity().getAttribute(Attributes.MOVEMENT_SPEED);
    AttributeModifier hastyModifier =
      new AttributeModifier(Utils.id("hasty"),
        ChampionsConfig.hastyMovementBonus,
        AttributeModifier.Operation.ADD_VALUE);

    if (speed != null && !speed.hasModifier(hastyModifier.id())) {
      speed.addTransientModifier(hastyModifier);
    }
  }

  @Override
  public boolean canApply(IChampion champion) {
    return champion.getLivingEntity().getAttribute(Attributes.MOVEMENT_SPEED) != null
      && super.canApply(champion);
  }

  @Override
  public void onServerUpdate(IChampion champion) {
    LivingEntity livingEntity = champion.getLivingEntity();

    if (livingEntity.tickCount % 20 == 0) {
      onInitialSpawn(champion);
    }
  }

  @Override
  public AffixSetting createDefaultSetting() {
    return AffixSetting.builder()
      .withDefault()
      .setCategory(AffixCategory.OFFENSE)
      .build();
  }

}
