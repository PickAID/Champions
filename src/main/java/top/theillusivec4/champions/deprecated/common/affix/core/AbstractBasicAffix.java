package top.theillusivec4.champions.deprecated.common.affix.core;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.api.affix.IAffix;
import top.theillusivec4.champions.deprecated.api.data.AffixCategory;
import top.theillusivec4.champions.deprecated.api.data.AffixSetting;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.deprecated.common.config.ConfigEnums;
import top.theillusivec4.champions.deprecated.common.network.SPacketSyncAffixData;

import java.util.List;
import java.util.Optional;

@Deprecated
public abstract class AbstractBasicAffix implements IAffix {
  public static final String DEFAULT_PREFIX = "affix.";
  protected AffixSetting setting = AffixSetting.empty();

  public static boolean canTarget(LivingEntity livingEntity, @Nullable LivingEntity target, boolean sightCheck) {

    if (target == null || !target.isAlive() || target instanceof ArmorStand || (sightCheck && !hasLineOfSight(livingEntity, target))) {
      return false;
    }
    AttributeInstance attributeInstance = livingEntity.getAttribute(Attributes.FOLLOW_RANGE);
    double range = attributeInstance == null ? 16.0D : attributeInstance.getValue();
    range = ChampionsConfig.affixTargetRange == 0 ? range : Math.min(range, ChampionsConfig.affixTargetRange);
    return livingEntity.distanceTo(target) <= range;
  }

  private static boolean hasLineOfSight(LivingEntity livingEntity, LivingEntity target) {

    if (livingEntity instanceof Mob mob) {
      return mob.getSensing().hasLineOfSight(target);
    } else {
      return livingEntity.hasLineOfSight(target);
    }
  }

  @Override
  public Identifier getIdentifier() {
    return Champions.API.getAffixId(this).orElseThrow();
  }

  @Override
  public String toString() {
    return this.getIdentifier().toString();
  }

  @Override
  public AffixCategory getCategory() {
    return setting.category();
  }

  @Override
  public boolean isEnabled() {
    return setting.enabled();
  }

  public ConfigEnums.Permission getMobPermission() {
    return setting.mobPermission().orElse(ConfigEnums.Permission.BLACKLIST);
  }

  @Override
  public String getPrefix() {
    return setting.prefix().orElse(DEFAULT_PREFIX);
  }

  @Override
  public void sync(IChampion champion) {
    LivingEntity livingEntity = champion.getLivingEntity();
    CompoundTag tag = this.writeSyncTag(champion);
    PacketDistributor.sendToPlayersTrackingEntity(livingEntity, new SPacketSyncAffixData(livingEntity.getId(), this.toString(), tag));
  }

  public boolean canApply(IChampion champion) {
    boolean isValidEntity;
    var entityKey = BuiltInRegistries.ENTITY_TYPE.getKey(champion.getLivingEntity().getType());
    if (isBlackList()) {
      isValidEntity = getMobList().map(mobList -> !mobList.contains(entityKey)).orElse(true);
    } else {
      isValidEntity = getMobList().map(mobList -> mobList.contains(entityKey)).orElse(false);
    }
    return isEnabled() && isValidEntity && champion.getServer().getRank().map(rank -> getTier().matches(rank.getTier())).orElse(false);
  }

  @Override
  public MinMaxBounds.Ints getTier() {
    return setting.tier().orElse(MinMaxBounds.Ints.atLeast(1));
  }

  @Override
  public Optional<List<Identifier>> getMobList() {
    return setting.mobList();
  }

  @Override
  public AffixSetting getSetting() {
    if (this.setting == null) {
      this.setting = createDefaultSetting();
    }
    return this.setting;
  }

  @Override
  public void applySetting(AffixSetting affixSetting) {
    this.setting = affixSetting;
  }
  public void applyDefaultSettingWithId() {
    applySetting(createDefaultSetting().withNewType(getIdentifier()));
  }

  public boolean isBlackList() {
    return getMobPermission() == ConfigEnums.Permission.BLACKLIST;
  }

  public abstract AffixSetting createDefaultSetting();

}
