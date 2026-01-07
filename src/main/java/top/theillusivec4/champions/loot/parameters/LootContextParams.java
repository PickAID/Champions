package top.theillusivec4.champions.loot.parameters;

import net.minecraft.util.context.ContextKey;
import top.theillusivec4.champions.champion.affix.LatestDamage;
import top.theillusivec4.champions.util.Utils;

public final class LootContextParams {
  public static final ContextKey<Integer> CHAMPION_LEVEL = create("champion_level");
  public static final ContextKey<LatestDamage> LATEST_DAMAGE = create("latest_damage");

  private static <T> ContextKey<T> create(String name) {
    return new ContextKey<>(Utils.id(name));
  }

  private LootContextParams() {
  }
}
