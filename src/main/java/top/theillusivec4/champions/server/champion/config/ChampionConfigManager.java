package top.theillusivec4.champions.server.champion.config;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import top.theillusivec4.champions.registry.Registries;

import java.util.Map;
import java.util.Optional;

/**
 * 从数据包加载冠军配置数据，便于为实体应用
 */
public class ChampionConfigManager extends SimpleJsonResourceReloadListener<ChampionConfigSelector> {
  // 需保证数据不可变性 原子性
  private Map<Identifier, ChampionConfigSelector.Holder> map = Map.of();

  public ChampionConfigManager(HolderLookup.Provider registries) {
    super(registries, ChampionConfigSelector.CODEC, Registries.CHAMPION_CONFIG_SELECTOR);
  }

  public Optional<ChampionConfigSelector.Holder> get(EntityType<?> entityType) {
    Identifier id = EntityType.getKey(entityType);
    return Optional.ofNullable(this.map.get(id));
  }

  @Override
  protected void apply(Map<Identifier, ChampionConfigSelector> preparations, ResourceManager manager, ProfilerFiller profiler) {
    ImmutableMap.Builder<Identifier, ChampionConfigSelector.Holder> builder = ImmutableMap.builder();

    for (Map.Entry<Identifier, ChampionConfigSelector> entry : preparations.entrySet()) {
      builder.put(entry.getKey(), new ChampionConfigSelector.Holder(ResourceKey.create(Registries.CHAMPION_CONFIG_SELECTOR, entry.getKey()), entry.getValue()));
    }

    this.map = builder.build();
  }
}
