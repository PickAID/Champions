package top.theillusivec4.champions.common.affix.core;

import net.minecraft.advancements.critereon.MinMaxBounds;
import top.theillusivec4.champions.api.AffixCategory;
import top.theillusivec4.champions.common.config.ConfigEnums;

public class BasicAffix extends AbstractBasicAffix {
  public BasicAffix() {
  }

  public BasicAffix(boolean enabled, MinMaxBounds.Ints tier, ConfigEnums.Permission mobPermission, AffixCategory category, String prefix, Boolean hasSub) {
    super(enabled, tier, mobPermission, category, prefix, hasSub);
  }
}
