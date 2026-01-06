package top.theillusivec4.champions.deprecated.api.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import top.theillusivec4.champions.deprecated.api.AffixSettingBuilder;
import top.theillusivec4.champions.deprecated.common.config.ConfigEnums;
import top.theillusivec4.champions.util.Utils;

import java.util.List;
import java.util.Optional;

@Deprecated
public record AffixSetting(Identifier type, boolean enabled, Optional<MinMaxBounds.Ints> tier,
                           Optional<List<Identifier>> mobList, Optional<ConfigEnums.Permission> mobPermission,
                           AffixCategory category, Optional<String> prefix, Optional<Boolean> hasSub) {

  public static final Codec<AffixSetting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    // set affix type for data generation, must match this registry name
    Identifier.CODEC.fieldOf("type").forGetter(AffixSetting::type),
    Codec.BOOL.fieldOf("enable").forGetter(AffixSetting::enabled),
    MinMaxBounds.Ints.CODEC.optionalFieldOf("tier").forGetter(AffixSetting::tier),
    Codec.list(Identifier.CODEC).optionalFieldOf("mobList").forGetter(AffixSetting::mobList),
    StringRepresentable.fromEnum(ConfigEnums.Permission::values).optionalFieldOf("mobPermission").forGetter(AffixSetting::mobPermission),
    StringRepresentable.fromEnum(AffixCategory::values).fieldOf("category").forGetter(AffixSetting::category),
    Codec.STRING.optionalFieldOf("prefix").forGetter(AffixSetting::prefix),
    Codec.BOOL.optionalFieldOf("hasSub").forGetter(AffixSetting::hasSub)
  ).apply(instance, AffixSetting::new));

  public static AffixSetting empty() {
    return new AffixSetting(Utils.id("empty"), false, Optional.empty(), Optional.empty(), Optional.empty(), AffixCategory.CC, Optional.empty(), Optional.empty());
  }

  public static AffixSettingBuilder builder() {
    return new AffixSettingBuilder();
  }

  public AffixSetting withNewType(Identifier newType) {
    return new AffixSetting(
      newType,
      this.enabled(),
      this.tier(),
      this.mobList(),
      this.mobPermission(),
      this.category(),
      this.prefix(),
      this.hasSub()
    );
  }
}
