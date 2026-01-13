package top.theillusivec4.champions.world.loot.parameters;

import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.EntitySpawnReason;
import top.theillusivec4.champions.champion.affix.Damage;
import top.theillusivec4.champions.util.Utils;

public final class LootContextParams {
  public static final ContextKey<Integer> CHAMPION_LEVEL = create("champion_level");
  public static final ContextKey<Damage> LATEST_DAMAGE = create("latest_damage");
  public static final ContextKey<EntitySpawnReason> ENTITY_SPAWN_REASON = create("reason");

  private static <T> ContextKey<T> create(String name) {
    return new ContextKey<>(Utils.id(name));
  }

  private LootContextParams() {
  }
}
