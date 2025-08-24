package top.theillusivec4.champions.api;

import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixDataLoader;
import top.theillusivec4.champions.api.data.AttributesModifierDataLoader;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ChampionsApiImpl implements IChampionsApi {
  private static final ConcurrentHashMap<AffixCategory, List<IAffix>> categories = new ConcurrentHashMap<>();
  private static final AffixDataLoader AFFIX_DATA_LOADER = new AffixDataLoader();
  private static final AttributesModifierDataLoader ATTRIBUTES_MODIFIER_DATA_LOADER = new AttributesModifierDataLoader();
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
  public Optional<ResourceLocation> getAffixId(IAffix affix) {
    return Optional.ofNullable(AffixRegistry.AFFIX_REGISTRY.getKey(affix));
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

  @Override
  public AffixDataLoader getAffixDataLoader() {
    return AFFIX_DATA_LOADER;
  }

  @Override
  public AttributesModifierDataLoader getAttributesModifierDataLoader() {
    return ATTRIBUTES_MODIFIER_DATA_LOADER;
  }
}
