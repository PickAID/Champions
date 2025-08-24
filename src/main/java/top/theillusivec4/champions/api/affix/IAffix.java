package top.theillusivec4.champions.api.affix;

import top.theillusivec4.champions.api.data.AffixCategory;

public interface IAffix extends IAffixBase,
  IAffixSettingHolder,
  IAffixSyncable,
  IAffixCompatibility {
	/**
	 * Is this category same with other category?
	 *
	 * @param other affix
	 * @return true if same category, else false
	 */
	default boolean sameCategory(AffixCategory other) {
		return other == getCategory();
	}

	default String toLanguageKey() {
		return getPrefix() + getIdentifier().toLanguageKey();
	}

	default boolean hasSubscriptions() {
		return getSetting().hasSub().orElse(false);
	}

}
