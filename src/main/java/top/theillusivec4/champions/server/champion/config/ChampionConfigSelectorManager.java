package top.theillusivec4.champions.server.champion.config;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.registry.Registries;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 从数据包加载冠军配置数据，便于为实体应用
 */
public class ChampionConfigSelectorManager extends SimpleJsonResourceReloadListener<ChampionConfigSelector> {
  private Map<Identifier, ChampionConfigSelectorHolder> map = Map.of();

  public ChampionConfigSelectorManager(HolderLookup.Provider registries) {
    super(registries, ChampionConfigSelector.CODEC, Registries.CHAMPION_CONFIG_SELECTOR);
  }

  public @Nullable ChampionConfigSelectorHolder byKey(ResourceKey<ChampionConfigSelector> key) {
    return this.byId(key.identifier());
  }

  public @Nullable ChampionConfigSelectorHolder byId(Identifier id) {
    return this.map.get(id);
  }

  public Set<Identifier> getKeys() {
    return this.map.keySet();
  }

  public Collection<ChampionConfigSelectorHolder> getValues() {
    return this.map.values();
  }

  @Override
  protected void apply(Map<Identifier, ChampionConfigSelector> preparations, ResourceManager manager, ProfilerFiller profiler) {
    ImmutableMap.Builder<Identifier, ChampionConfigSelectorHolder> builder = ImmutableMap.builder();

    for (Map.Entry<Identifier, ChampionConfigSelector> entry : preparations.entrySet()) {
      Identifier id = entry.getKey();
      ChampionConfigSelector selector = entry.getValue();
      builder.put(id, new ChampionConfigSelectorHolder(id, selector));
    }

    this.map = builder.build();
  }
}
