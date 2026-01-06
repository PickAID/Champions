package top.theillusivec4.champions.deprecated.api;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.resources.Identifier;
import top.theillusivec4.champions.deprecated.api.data.AffixCategory;
import top.theillusivec4.champions.deprecated.api.data.AffixSetting;
import top.theillusivec4.champions.deprecated.common.config.ConfigEnums;

import java.util.List;
import java.util.Optional;

/**
 * Basic affix builder for custom affix behavior
 *
 */
@Deprecated
public class AffixSettingBuilder {

  private Identifier type;
  private boolean isEnable;
  private MinMaxBounds.Ints tier;
  private List<Identifier> mobList;
  private ConfigEnums.Permission mobPermission;
  private AffixCategory category;
  private String prefix;
  private boolean hasSubscriptions;

    /*public static <T extends IAffix, B extends BasicAffixBuilder<T>> Codec<T> of(Supplier<B> builderSupplier) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.fieldOf("setEnable").forGetter(IAffix::isEnabled),
                IntCodec.codec().fieldOf("tier").forGetter(IAffix::getTier),
                Codec.list(Identifier.CODEC).optionalFieldOf("mobList").forGetter(IAffix::getMobList),
                StringRepresentable.fromEnum(ConfigEnums.Permission::values).fieldOf("mobPermission").forGetter(IAffix::getMobPermission),
                StringRepresentable.fromEnum(AffixCategory::values).fieldOf("category").forGetter(IAffix::getCategory),
                Codec.STRING.fieldOf("prefix").forGetter(IAffix::getPrefix),
                Codec.BOOL.fieldOf("hasSubscriptions").forGetter(IAffix::hasSubscriptions)
        ).apply(instance, (isEnable, tier, mobList, permission, category, prefix, hasSubscriptions) -> {
            // 使用 BasicAffixBuilder 构建对象
            BasicAffixBuilder<T> builder = builderSupplier.get();
            builder.setEnable(isEnable)
                    .setTier(tier)
                    .setMobList(mobList.orElse(List.of()))
                    .setMobPermission(permission)
                    .setCategory(category)
                    .setPrefix(prefix);
            if (hasSubscriptions) {
                builder.setHasSub();
            }
            return builder.build();
        }));
    }*/

  public AffixSettingBuilder withDefault() {
    return this.setEnable(true)
      .setCategory(AffixCategory.CC)
      .setTier(MinMaxBounds.Ints.between(1, 100));
  }

  public AffixSettingBuilder setType(Identifier type) {
    this.type = type;
    return this;
  }


  public AffixSettingBuilder setCategory(AffixCategory pCategory) {
    this.category = pCategory;
    return this;
  }


  public AffixSettingBuilder setPrefix(String pPrefix) {
    this.prefix = pPrefix;
    return this;
  }


  public AffixSettingBuilder setHasSub() {
    this.hasSubscriptions = true;
    return this;
  }


  public AffixSettingBuilder setHasSub(boolean pHasSub) {
    this.hasSubscriptions = pHasSub;
    return this;
  }


  public AffixSettingBuilder setEnable(boolean pEnable) {
    this.isEnable = pEnable;
    return this;
  }


  public AffixSettingBuilder setMobList(List<Identifier> mobList) {
    this.mobList = mobList;
    return this;
  }


  public AffixSettingBuilder setTier(MinMaxBounds.Ints tier) {
    this.tier = tier;
    return this;
  }


  public AffixSettingBuilder setMobPermission(ConfigEnums.Permission mobPermission) {
    this.mobPermission = mobPermission;
    return this;
  }


  public AffixSetting build() {
    return new AffixSetting(
      type,
      isEnable,
      Optional.ofNullable(tier),
      Optional.ofNullable(mobList),
      Optional.ofNullable(mobPermission),
      category,
      Optional.ofNullable(prefix),
      Optional.of(hasSubscriptions)
    );
  }
}
