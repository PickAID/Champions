package top.theillusivec4.champions.common.loot.parameters;

import net.minecraft.util.context.ContextKey;
import top.theillusivec4.champions.api.affix.LatestDamage;
import top.theillusivec4.champions.common.util.Utils;

public final class LootContextParams {
  public static final ContextKey<Integer> CHAMPION_LEVEL = create("champion_level");
  public static final ContextKey<LatestDamage> LATEST_DAMAGE = create("latest_damage");
  public static final ContextKey<Float> DAMAGE_AMOUNT = create("damage_amount");

  private static <T> ContextKey<T> create(String name) {
    return new ContextKey<>(Utils.id(name));
  }

  private LootContextParams() {
  }
}
