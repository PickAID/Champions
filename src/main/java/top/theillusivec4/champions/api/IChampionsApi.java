package top.theillusivec4.champions.api;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IChampionsApi {
  /**
   * Get affix by string id
   *
   * @param id Affix's String id
   * @return an Optional IAffix
   */
  Optional<IAffix> getAffix(String id);

  Optional<IAffix> getAffix(ResourceLocation id);
  Optional<ResourceLocation> getAffixId(IAffix affix);

  /**
   * Get all registered affixes copies
   *
   * @return List of IAffix
   */
  List<IAffix> getAffixes();

  /**
   * Get List of IAffix by Affix Category
   *
   * @param category Affix's Category
   * @return List of IAffix
   */
  List<IAffix> getAffixes(AffixCategory category);

  /**
   * Get all Categories list
   *
   * @return Array of AffixCategory
   */
  AffixCategory[] getCategories();

  /**
   * Get Relation of AffixCategory and IAffix Map copies
   *
   * @return Map of AffixCategory and List of IAffix
   */
  Map<AffixCategory, List<IAffix>> getCategoryMap();

  void addCategory(AffixCategory category, IAffix affix);
}
