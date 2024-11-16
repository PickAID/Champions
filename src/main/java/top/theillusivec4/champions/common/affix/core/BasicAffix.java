package top.theillusivec4.champions.common.affix.core;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixCategory;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.network.SPacketSyncAffixData;

import java.util.List;

public abstract class BasicAffix implements IAffix {
  private boolean enabled;
  private MinMaxBounds.Ints tier;
  private List<EntityType<?>> mobList = List.of();
  private ConfigEnums.Permission mobPermission;
  private AffixCategory category;
  private String prefix;
  private boolean hasSubscriptions;

  public BasicAffix() {

    if (hasSubscriptions()) {
      NeoForge.EVENT_BUS.register(this);
    }
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

    if (getMobPermission() == ConfigEnums.Permission.BLACKLIST) {
      isValidEntity = !getMobList().contains(champion.getLivingEntity().getType());
    } else {
      isValidEntity = getMobList().contains(champion.getLivingEntity().getType());
    }
    return this.enabled && isValidEntity && champion.getServer().getRank().map(rank -> getTier().matches(rank.getTier())).orElse(false);
  }

  public MinMaxBounds.Ints getTier() {
    return tier == null ? MinMaxBounds.Ints.atLeast(1) : tier;
  }

  @Override
  public void setTier(MinMaxBounds.Ints tier) {
    this.tier = tier;
  }

  public List<EntityType<?>> getMobList() {
    return mobList == null ? List.of() : mobList;
  }

  @Override
  public void setMobList(List<EntityType<?>> mobList) {
    this.mobList = mobList;
  }
}
