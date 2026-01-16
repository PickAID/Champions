package top.theillusivec4.champions.deprecated.common.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import top.theillusivec4.champions.util.Util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class ModLootTables {
  private static final Set<ResourceKey<LootTable>> LOCATIONS = new HashSet<>();
  public static final ResourceKey<LootTable> CHAMPION_LOOT = create("champion_loot");
  private static final Set<ResourceKey<LootTable>> IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);

  private static ResourceKey<LootTable> create(String name) {
    return create(ResourceKey.create(Registries.LOOT_TABLE, Util.id(name)));
  }

  private static ResourceKey<LootTable> create(ResourceKey<LootTable> name) {
    if (LOCATIONS.add(name)) {
      return name;
    } else {
      throw new IllegalArgumentException(name.identifier() + " is already a registered built-in loot table");
    }
  }

  static void bootstrap(BootstrapContext<LootTable> context) {
    var getter = context.lookup(Registries.LOOT_TABLE);
    context.register(CHAMPION_LOOT, getter.getOrThrow(CHAMPION_LOOT).value());
  }

  public static Set<ResourceKey<LootTable>> all() {
    return IMMUTABLE_LOCATIONS;
  }


}
