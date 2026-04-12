package top.theillusivec4.champions.deprecated.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IChampionsApi {
  /**
   * register a affix
   * @param affix to register
   */
  void registerAffix(IAffix affix);

  /**
   *
   * @param affixes list to register
   */
  void registerAffixes(IAffix... affixes);

  /**
   * Get affix by string id
   * @param id Affix's String id
   * @return an Optional IAffix
   */
  Optional<IAffix> getAffix(String id);

  /**
   * Get all registered affixes copies
   * @return List of IAffix
   */
  List<IAffix> getAffixes();

  /**
   * Get List of IAffix by Affix Category
   * @param category Affix's Category
   * @return List of IAffix
   */
  List<IAffix> getCategory(AffixCategory category);

  /**
   * Get all Categories list
   * @return Array of AffixCategory
   */
  AffixCategory[] getCategories();

  /**
   * Get Relation of AffixCategory and IAffix Map copies
   * @return Map of AffixCategory and List of IAffix
   */
  Map<AffixCategory, List<IAffix>> getCategoryMap();
}
