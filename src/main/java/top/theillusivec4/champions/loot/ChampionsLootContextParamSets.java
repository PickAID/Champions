package top.theillusivec4.champions.loot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import top.theillusivec4.champions.util.ChampionsUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class ChampionsLootContextParamSets {
  public static final Map<ResourceLocation, LootContextParamSet> REGISTRY = new HashMap<>();
  public static final LootContextParamSet SPAWN = register(
    "spawn",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
  );
  public static final LootContextParamSet LOCATION = register(
    "location",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(ChampionsLootContextParams.CHAMPION_LEVEL)
  );
  public static final LootContextParamSet ATTRIBUTES = register("attribute",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(ChampionsLootContextParams.CHAMPION_LEVEL)
  );
  public static final LootContextParamSet KNOCKBACK = register("knockback",
    builder -> builder.required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(ChampionsLootContextParams.CHAMPION_LEVEL)
      .optional(ChampionsLootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final LootContextParamSet DAMAGE = register("damage",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(ChampionsLootContextParams.CHAMPION_LEVEL)
      .optional(ChampionsLootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final LootContextParamSet HEAL = register("heal",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(ChampionsLootContextParams.CHAMPION_LEVEL)
      .optional(ChampionsLootContextParams.LATEST_DAMAGE)
  );
  public static final LootContextParamSet POST_ATTACK = register("post_attack",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(ChampionsLootContextParams.CHAMPION_LEVEL)
      .optional(ChampionsLootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final LootContextParamSet DAMAGE_IMMUNITY = register(
    "damage_immunity",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(ChampionsLootContextParams.CHAMPION_LEVEL)
      .optional(ChampionsLootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final LootContextParamSet DAMAGE_PROTECTION = register("damage_protection",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(ChampionsLootContextParams.CHAMPION_LEVEL)
      .optional(ChampionsLootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final LootContextParamSet TICK = register("tick",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(ChampionsLootContextParams.CHAMPION_LEVEL)
      .optional(ChampionsLootContextParams.LATEST_DAMAGE)
  );

  private ChampionsLootContextParamSets() {
  }

  private static LootContextParamSet register(String name, Consumer<LootContextParamSet.Builder> consumer) {
    LootContextParamSet.Builder builder = new LootContextParamSet.Builder();
    consumer.accept(builder);
    ResourceLocation id = ChampionsUtil.id(name);
    LootContextParamSet set = builder.build();
    LootContextParamSet set1 = REGISTRY.put(id, set);
    if (set1 != null) {
      throw new IllegalStateException("Loot table parameter set " + id + " is already registered");
    } else {
      return set;
    }
  }
}
