package top.theillusivec4.champions.world.loot.parameters;

import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import top.theillusivec4.champions.util.ChampionsUtil;

public final class ChampionsLootContextParams {
  public static final LootContextParam<Integer> AFFIX_LEVEL = create("affix_level");
  public static final LootContextParam<Integer> CHAMPION_TIER = create("champion_tier");
  public static final LootContextParam<Integer> DAMAGE_COUNT = create("damage_count");

  private ChampionsLootContextParams() {
  }

  private static <T> LootContextParam<T> create(String name) {
    return new LootContextParam<>(ChampionsUtil.id(name));
  }
}
