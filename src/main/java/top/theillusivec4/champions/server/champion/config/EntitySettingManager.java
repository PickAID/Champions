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
 * 从数据包加载冠军生成数据，便于为实体应用
 */
public class EntitySettingManager extends SimpleJsonResourceReloadListener<EntitySetting> {
  private Map<Identifier, EntitySettingHolder> map = Map.of();

  public EntitySettingManager(HolderLookup.Provider registries) {
    super(registries, EntitySetting.CODEC, Registries.ENTITY_SETTING);
  }

  public @Nullable EntitySettingHolder byKey(ResourceKey<EntitySetting> key) {
    return this.byId(key.identifier());
  }

  public @Nullable EntitySettingHolder byId(Identifier id) {
    return this.map.get(id);
  }

  public Set<Identifier> getKeys() {
    return this.map.keySet();
  }

  public Collection<EntitySettingHolder> getValues() {
    return this.map.values();
  }

  @Override
  protected void apply(Map<Identifier, EntitySetting> preparations, ResourceManager manager, ProfilerFiller profiler) {
    ImmutableMap.Builder<Identifier, EntitySettingHolder> builder = ImmutableMap.builder();

    for (Map.Entry<Identifier, EntitySetting> entry : preparations.entrySet()) {
      Identifier id = entry.getKey();
      EntitySetting selector = entry.getValue();
      builder.put(id, new EntitySettingHolder(id, selector));
    }

    this.map = builder.build();
  }
}
