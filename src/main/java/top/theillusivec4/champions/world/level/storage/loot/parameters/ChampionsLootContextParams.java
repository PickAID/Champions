package top.theillusivec4.champions.world.level.storage.loot.parameters;

import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import top.theillusivec4.champions.util.Util;
import top.theillusivec4.champions.api.championmob.ChampionMobProperty;
import top.theillusivec4.champions.world.entity.damagetracker.DamageTracker;

public final class ChampionsLootContextParams {
  public static final LootContextParam<Integer> AFFIX_LEVEL = create("affix_level");
  public static final LootContextParam<Integer> CHAMPION_TIER = create("champion_tier");
  public static final LootContextParam<Integer> DAMAGE_COUNT = create("damage_count");
  public static final LootContextParam<ChampionMobProperty> CHAMPION_MOB_PROPERTY = create("champion_mob_property");
  public static final LootContextParam<DamageTracker> DAMAGE_TRACKER = create("damage_tracker");

  private ChampionsLootContextParams() {
  }

  private static <T> LootContextParam<T> create(String name) {
    return new LootContextParam<>(Util.id(name));
  }
}
