package top.theillusivec4.champions.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import top.theillusivec4.champions.common.config.ConfigEnums;

import java.util.List;
import java.util.function.Supplier;

/**
 * Basic affix builder for custom affix behavior
 *
 * @param <T> Affix type
 */
public class BasicAffixBuilder<T extends IAffix> implements IAffixBuilder<T> {

  private final Supplier<T> affixSupplier;
  private boolean enabled;
  private MinMaxBounds.Ints tier;
  private List<ResourceLocation> mobList;
  private ConfigEnums.Permission mobPermission;
  private AffixCategory category;
  private String prefix;
  private boolean hasSubscriptions;

  public BasicAffixBuilder(Supplier<T> affixSupplier) {
    this.affixSupplier = affixSupplier;
  }

  public static <T extends IAffix, B extends BasicAffixBuilder<T>> Codec<T> of(Supplier<B> builderSupplier) {
    return RecordCodecBuilder.create(instance -> instance.group(
      Codec.BOOL.fieldOf("enabled").forGetter(IAffix::isEnabled),
      MinMaxBounds.Ints.CODEC.fieldOf("tier").forGetter(IAffix::getTier),
      Codec.list(ResourceLocation.CODEC).optionalFieldOf("mobList").forGetter(IAffix::getMobList),
      StringRepresentable.fromEnum(ConfigEnums.Permission::values).fieldOf("mobPermission").forGetter(IAffix::getMobPermission),
      StringRepresentable.fromEnum(AffixCategory::values).fieldOf("category").forGetter(IAffix::getCategory),
      Codec.STRING.fieldOf("prefix").forGetter(IAffix::getPrefix),
      Codec.BOOL.fieldOf("hasSubscriptions").forGetter(IAffix::hasSubscriptions)
    ).apply(instance, (enabled, tier, mobList, permission, category, prefix, hasSubscriptions) -> {
      // 使用 BasicAffixBuilder 构建对象
      BasicAffixBuilder<T> builder = builderSupplier.get();
      builder.enabled(enabled)
        .setTier(tier)
        .setMobList(mobList.orElse(List.of()))
        .setMobPermission(permission)
        .setCategory(category)
        .setPrefix(prefix);
      if (hasSubscriptions) {
        builder.setHasSubscriptions();
      }
      return builder.build();
    }));
  }

  @Override
  public BasicAffixBuilder<T> setCategory(AffixCategory pCategory) {
    this.category = pCategory;
    return this;
  }

  @Override
  public BasicAffixBuilder<T> setPrefix(String pPrefix) {
    this.prefix = pPrefix;
    return this;
  }

  @Override
  public IAffixBuilder<T> setHasSubscriptions() {
    this.hasSubscriptions = true;
    return this;
  }

  @Override
  public IAffixBuilder<T> enabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  @Override
  public IAffixBuilder<T> setMobList(List<ResourceLocation> mobList) {
    this.mobList = mobList;
    return this;
  }

  @Override
  public IAffixBuilder<T> setTier(MinMaxBounds.Ints tier) {
    this.tier = tier;
    return this;
  }

  @Override
  public IAffixBuilder<T> setMobPermission(ConfigEnums.Permission mobPermission) {
    this.mobPermission = mobPermission;
    return this;
  }

  @Override
  public T build() {
    var affix = affixSupplier.get();
    apply(affix);
    return affix;
  }

  private void apply(T pAffix) {
    pAffix.setSubscriptions(this.hasSubscriptions);
    pAffix.setCategory(this.category);
    pAffix.setPrefix(this.prefix);
    pAffix.setEnabled(this.enabled);
    pAffix.setMobList(this.mobList);
    pAffix.setMobPermission(this.mobPermission);
    pAffix.setTier(this.tier);
  }
}
