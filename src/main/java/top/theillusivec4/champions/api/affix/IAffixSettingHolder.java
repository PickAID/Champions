package top.theillusivec4.champions.api.affix;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.resources.Identifier;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.config.ConfigEnums;

import java.util.List;
import java.util.Optional;

public interface IAffixSettingHolder {
    void applySetting(AffixSetting affixSetting);
    AffixSetting getSetting();
    MinMaxBounds.Ints getTier();
    ConfigEnums.Permission getMobPermission();
    Optional<List<Identifier>> getMobList();

}
