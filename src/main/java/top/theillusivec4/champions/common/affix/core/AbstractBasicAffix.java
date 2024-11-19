package top.theillusivec4.champions.common.affix.core;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.*;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.network.SPacketSyncAffixData;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractBasicAffix implements IAffix {
  private boolean enabled;
  private MinMaxBounds.Ints tier;
  private List<ResourceLocation> mobList;
  private ConfigEnums.Permission mobPermission;
  private AffixCategory category;
  private String prefix;
  private boolean hasSubscriptions;

  public AbstractBasicAffix() {

    if (hasSubscriptions()) {
      NeoForge.EVENT_BUS.register(this);
    }
  }

  public AbstractBasicAffix(boolean enabled, MinMaxBounds.Ints tier, ConfigEnums.Permission mobPermission, AffixCategory category, String prefix, Boolean hasSub) {
    this.enabled = enabled;
    this.tier = tier;
    this.mobPermission = mobPermission;
    this.category = category;
    this.prefix = prefix;
    this.hasSubscriptions = hasSub;
  }


  public static boolean canTarget(LivingEntity livingEntity, LivingEntity target, boolean sightCheck) {

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
  public void setSubscriptions(boolean hasSubscriptions) {
    this.hasSubscriptions = hasSubscriptions;
  }

  @Override
  public boolean hasSubscriptions() {
    return hasSubscriptions;
  }

  @Override
  public ResourceLocation getIdentifier() {
    return AffixRegistry.AFFIX_REGISTRY.getKey(this);
  }

  @Override
  public String toString() {
    return this.getIdentifier().toString();
  }

  @Override
  public AffixCategory getCategory() {
    return this.category;
  }

  @Override
  public void setCategory(AffixCategory category) {
    this.category = category;
    Champions.API.addCategory(this.getCategory(), this);
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public ConfigEnums.Permission getMobPermission() {
    return mobPermission == null ? ConfigEnums.Permission.BLACKLIST : mobPermission;
  }

  @Override
  public void setMobPermission(ConfigEnums.Permission mobPermission) {
    this.mobPermission = mobPermission;
  }

  @Override
  public String getPrefix() {
    return this.prefix == null ? "affix." : this.prefix;
  }

  @Override
  public void setPrefix(String prefix) {
    this.prefix = prefix;
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
    if (getMobPermission() == ConfigEnums.Permission.BLACKLIST) {
      isValidEntity = getMobList().map(e -> !e.contains(entityKey)).orElse(true);
    } else {
      isValidEntity = getMobList().map(e -> e.contains(entityKey)).orElse(false);
    }
    return this.enabled && isValidEntity && champion.getServer().getRank().map(rank -> getTier().matches(rank.getTier())).orElse(false);
  }

  @Override
  public MinMaxBounds.Ints getTier() {
    return tier == null ? MinMaxBounds.Ints.atLeast(1) : tier;
  }

  @Override
  public void setTier(MinMaxBounds.Ints tier) {
    this.tier = tier;
  }

  public Optional<List<ResourceLocation>> getMobList() {
    return Optional.ofNullable(mobList);
  }

  @Override
  public void setMobList(List<ResourceLocation> mobList) {
    this.mobList = mobList;
  }

  public static  <T extends IAffix, B extends BasicAffixBuilder<T>> Codec<T> codec(Supplier<B> affixType) {
    return BasicAffixBuilder.of(affixType);
  }
}
