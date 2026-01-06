package top.theillusivec4.champions.deprecated.api.affix;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.resources.Identifier;
import top.theillusivec4.champions.deprecated.api.data.AffixSetting;
import top.theillusivec4.champions.deprecated.common.config.ConfigEnums;

import java.util.List;
import java.util.Optional;

@Deprecated
public interface IAffixSettingHolder {
    void applySetting(AffixSetting affixSetting);
    AffixSetting getSetting();
    MinMaxBounds.Ints getTier();
    ConfigEnums.Permission getMobPermission();
    Optional<List<Identifier>> getMobList();

}
