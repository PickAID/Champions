package top.theillusivec4.champions.api.affix;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.config.ConfigEnums;

import java.util.List;
import java.util.Optional;

public interface IAffixSettingHolder {
    void applySetting(AffixSetting affixSetting);
    AffixSetting getSetting();
    void applyDefaultSetting();
    void applyDefaultSettingWithId(ResourceLocation id);
    MinMaxBounds.Ints getTier();
    ConfigEnums.Permission getMobPermission();
    Optional<List<ResourceLocation>> getMobList();

}
