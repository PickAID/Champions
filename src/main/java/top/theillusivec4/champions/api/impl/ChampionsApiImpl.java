package top.theillusivec4.champions.api.impl;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.api.AffixCategory;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.IChampionsApi;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ChampionsApiImpl implements IChampionsApi {
  private static final ConcurrentHashMap<AffixCategory, List<IAffix>> categories = new ConcurrentHashMap<>();
  private static final Logger LOGGER = LogManager.getLogger();
  private static ChampionsApiImpl instance = null;

  private ChampionsApiImpl() {
  }

  public static IChampionsApi getInstance() {
    if (instance == null) {
      instance = new ChampionsApiImpl();
      categories.clear();

      for (AffixCategory value : AffixCategory.values()) {
        categories.put(value, new ArrayList<>());
      }
    }
    return instance;
  }

  @Override
  public Optional<IAffix> getAffix(String id) {
    return getAffix(ResourceLocation.parse(id));
  }

  @Override
  public Optional<IAffix> getAffix(ResourceLocation id) {
    return AffixRegistry.AFFIX_REGISTRY.getOptional(id);
  }

  @Override
  public List<IAffix> getAffixes() {
    return getAffixStream().toList();
  }

  public Stream<IAffix> getAffixStream() {
    return AffixRegistry.AFFIX_REGISTRY.stream();
  }

  @Override
  public List<IAffix> getAffixes(AffixCategory category) {
    return getAffixStream().filter(affix -> affix.sameCategory(category)).toList();
  }

  @Override
  public AffixCategory[] getCategories() {
    return AffixCategory.values();
  }

  @Override
  public Map<AffixCategory, List<IAffix>> getCategoryMap() {
    Map<AffixCategory, List<IAffix>> copy = new HashMap<>();
    categories.forEach((k, v) -> copy.put(k, Collections.unmodifiableList(v)));
    return Collections.unmodifiableMap(copy);
  }

  @Override
  public void addCategory(AffixCategory category, IAffix affix) {
    categories.get(category).add(affix);
  }
}
