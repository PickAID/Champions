package top.theillusivec4.champions.api.affix;

import net.minecraft.resources.Identifier;
import top.theillusivec4.champions.api.data.AffixCategory;

public interface IAffixBase {
    /**
     * Get IAffix id
     *
     * @return String of IAffix id
     */
    Identifier getIdentifier();

    /**
     * Get IAffix's Category
     *
     * @return AffixCategory
     */
    AffixCategory getCategory();


    /**
     * Get affix's prefix, usually used for translate key
     *
     * @return Affix prefix
     */
    String getPrefix();

    /**
     * Dose affix has event
     *
     * @return True if it has subs, else false.
     */
    boolean hasSubscriptions();

    /**
     * Does affix enabled
     * @return affix enabled
     */
    boolean isEnabled();
}
