package top.theillusivec4.champions.deprecated.common.affix;

import net.minecraft.world.damagesource.DamageSource;
import top.theillusivec4.champions.deprecated.api.AffixCategory;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.common.affix.core.BasicAffix;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;

public class DampeningAffix extends BasicAffix {
  public DampeningAffix() {
    super("dampening", AffixCategory.DEFENSE);
  }

  @Override
  public float onHurt(IChampion champion, DamageSource source, float amount, float newAmount) {
    return source.isDirect() ? newAmount * (float) (1.0F
      - ChampionsConfig.dampenedDamageReduction) : newAmount;
  }
}
