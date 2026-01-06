package top.theillusivec4.champions.deprecated.api.affix;

import top.theillusivec4.champions.deprecated.api.IChampion;

@Deprecated
public interface IAffixCompatibility {
    /**
     * Does this IChampion mob can apply this Affix?
     *
     * @param champion to apply affix
     * @return able to apply affix
     */
    default boolean canApply(IChampion champion) {
        return true;
    }

    /**
     * Is this Affix compatible to another affix?
     *
     * @param affix another affix
     * @return able to compatible affix
     */
    default boolean isCompatible(IAffix affix) {
        return affix != this;
    }
}
