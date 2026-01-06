package top.theillusivec4.champions.deprecated.common.affix;

import net.minecraft.world.damagesource.DamageSource;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.api.data.AffixCategory;
import top.theillusivec4.champions.deprecated.api.data.AffixSetting;
import top.theillusivec4.champions.deprecated.common.affix.core.CombatAffix;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;

@Deprecated
public class DampeningAffix extends CombatAffix {

  @Override
  public float onHurt(IChampion champion, DamageSource source, float amount, float newAmount) {
    return source.isDirect() ? newAmount * (float) (1.0F
      - ChampionsConfig.dampenedDamageReduction) : newAmount;
  }

  @Override
  public AffixSetting createDefaultSetting() {
    return AffixSetting.builder()
      .withDefault()
      .setCategory(AffixCategory.DEFENSE)
      .build();
  }
}
