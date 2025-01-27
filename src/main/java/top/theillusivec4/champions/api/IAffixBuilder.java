package top.theillusivec4.champions.api;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.common.config.ConfigEnums;

import java.util.List;

public interface IAffixBuilder<T extends IAffix> {
    /**
     * Set type for data generation
     *
     * @param type for data apply target
     * @return builder
     */
    BasicAffixBuilder<T> setType(ResourceLocation type);

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
     *
     * @return builder
     */
    IAffixBuilder<T> setHasSub();

    IAffixBuilder<T> setEnable(boolean enabled);

    IAffixBuilder<T> setMobList(List<ResourceLocation> mobList);

    IAffixBuilder<T> setTier(MinMaxBounds.Ints tier);

    IAffixBuilder<T> setMobPermission(ConfigEnums.Permission mobPermission);

    /**
     * Build affix with this builder
     *
     * @return Affix Instance
     */
    T build();
}
