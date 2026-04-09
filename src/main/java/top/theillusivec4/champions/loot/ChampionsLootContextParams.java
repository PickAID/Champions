package top.theillusivec4.champions.loot;

import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import top.theillusivec4.champions.damage.Damage;
import top.theillusivec4.champions.util.ChampionsUtil;

public final class ChampionsLootContextParams {
  public static final LootContextParam<Integer> CHAMPION_LEVEL = create("champion_level");
  public static final LootContextParam<Damage> LATEST_DAMAGE = create("latest_damage");

  private ChampionsLootContextParams() {
  }

  private static <T> LootContextParam<T> create(String name) {
    return new LootContextParam<>(ChampionsUtil.id(name));
  }
}
