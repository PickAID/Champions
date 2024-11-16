package top.theillusivec4.champions.api;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.entity.EntityType;
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
  private List<EntityType<?>> mobList;
  private ConfigEnums.Permission mobPermission;
  private AffixCategory category;
  private String prefix;
  private boolean hasSubscriptions;

  public BasicAffixBuilder(Supplier<T> affixSupplier) {
    this.affixSupplier = affixSupplier;
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
  public IAffixBuilder<T> setMobList(List<EntityType<?>> mobList) {
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
