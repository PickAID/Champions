package top.theillusivec4.champions.world.loot.parameters;

import net.minecraft.util.context.ContextKey;
import top.theillusivec4.champions.champion.affix.Damage;
import top.theillusivec4.champions.util.Util;

public final class LootContextParams {
  public static final ContextKey<Integer> CHAMPION_LEVEL = create("champion_level");
  public static final ContextKey<Damage> LATEST_DAMAGE = create("latest_damage");

  private static <T> ContextKey<T> create(String name) {
    return new ContextKey<>(Util.id(name));
  }

  private LootContextParams() {
  }
}
