package top.theillusivec4.champions.api;

public interface IAffixBuilder<T extends IAffix> {

  /**
   * Set affix category
   *
   * @param pCategory affix's category
   * @return builder
   */
  IAffixBuilder<T> setCategory(AffixCategory pCategory);

  /**
   * Set affix prefix
   *
   * @param pPrefix affix prefix
   * @return builder
   */
  IAffixBuilder<T> setPrefix(String pPrefix);

  /**
   * Set this affix has subscription, if true, will subscript to forge event.
   * @return builder
   */
  IAffixBuilder<T> setHasSubscriptions();

  /**
   * Build affix with this builder
   *
   * @return Affix Instance
   */
  T build();
}
