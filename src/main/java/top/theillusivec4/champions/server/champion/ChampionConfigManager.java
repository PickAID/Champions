package top.theillusivec4.champions.server.champion;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 从数据包加载冠军生成数据，便于为实体应用
 */
public class ChampionConfigManager extends SimpleJsonResourceReloadListener<ChampionConfig> {
  private final ResourceKey<Registry<ChampionConfig>> key;
  private Map<Identifier, ChampionConfig> map = Map.of();

  public ChampionConfigManager(HolderLookup.Provider registries, ResourceKey<Registry<ChampionConfig>> key) {
    super(registries, ChampionConfig.CODEC, key);
    this.key = key;
  }

  public @Nullable ChampionConfig byKey(ResourceKey<ChampionConfig> key) {
    return this.byId(key.identifier());
  }

  public @Nullable ChampionConfig byId(Identifier id) {
    return this.map.get(id);
  }

  public @Nullable ChampionConfig byEntity(Entity entity) {
    if (this.key.equals(ChampionsRegistries.ENTITY_CONFIG)) {
      return this.byId(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()));
    }
    return null;
  }

  public @Nullable ChampionConfig byLevel(Level level) {
    if (this.key.equals(ChampionsRegistries.LEVEL_CONFIG)) {
      return this.byId(level.dimension().identifier());
    }
    return null;
  }

  public Set<Identifier> getKeys() {
    return this.map.keySet();
  }

  public Collection<ChampionConfig> getValues() {
    return this.map.values();
  }

  @Override
  protected void apply(Map<Identifier, ChampionConfig> preparations, ResourceManager manager, ProfilerFiller profiler) {
    ImmutableMap.Builder<Identifier, ChampionConfig> builder = ImmutableMap.builder();

    for (Map.Entry<Identifier, ChampionConfig> entry : preparations.entrySet()) {
      Identifier id = entry.getKey();
      ChampionConfig config = entry.getValue();
      builder.put(id, config);
    }

    this.map = builder.build();
  }
}
