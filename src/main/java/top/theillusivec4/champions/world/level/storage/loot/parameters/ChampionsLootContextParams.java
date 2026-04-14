package top.theillusivec4.champions.world.level.storage.loot.parameters;

import net.minecraft.util.context.ContextKey;
import top.theillusivec4.champions.util.ChampionsUtil;
import top.theillusivec4.champions.world.entity.champion.property.ChampionProperty;
import top.theillusivec4.champions.world.entity.damagetracker.DamageTracker;

public final class ChampionsLootContextParams {
	public static final ContextKey<Integer> AFFIX_LEVEL = create("affix_level");
	public static final ContextKey<Integer> CHAMPION_TIER = create("champion_tier");
	public static final ContextKey<ChampionProperty> CHAMPION_MOB_PROPERTY = create("champion_mob_property");
	public static final ContextKey<DamageTracker> DAMAGE_TRACKER = create("damage_tracker");


  private static <T> ContextKey<T> create(String name) {
    return new ContextKey<>(ChampionsUtil.id(name));
  }

  private ChampionsLootContextParams() {
  }
}
