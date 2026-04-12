package top.theillusivec4.champions.extralootparam;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import top.theillusivec4.champions.attachments.ChampionsAttachments;
import top.theillusivec4.champions.champion.ChampionHelper;
import top.theillusivec4.champions.world.loot.parameters.ChampionsLootContextParams;

public final class ExtraLootParamHelper {
  private ExtraLootParamHelper() {
  }

  public static void addExtraParameters(LootContextParamSet.Builder builder) {
    builder
      .required(ChampionsLootContextParams.CHAMPION_TIER)
      .required(ChampionsLootContextParams.DAMAGE_COUNT);
  }

  public static void withParameters(Entity entity, LootParams.Builder builder) {
    builder
      .withParameter(ChampionsLootContextParams.CHAMPION_TIER, ChampionHelper.getTier(entity))
      .withParameter(ChampionsLootContextParams.DAMAGE_COUNT, ExtraLootParamHelper.getDamageCount(entity));
  }

  public static Holder<DamageType> getLastDamageType(Entity entity) {
    RegistryAccess registry = entity.registryAccess();
    ResourceKey<DamageType> key = getKey(entity);
    return registry.holderOrThrow(key);
  }

  private static ResourceKey<DamageType> getKey(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.DAMAGE_TYPE).orElse(DamageTypes.GENERIC_KILL);
  }

  public static void updateDamageParameter(Entity entity, DamageSource source) {
    if (source.typeHolder() instanceof Holder.Reference<DamageType> holder) {
      ResourceKey<DamageType> key = holder.key();
      if (!getKey(entity).equals(key)) {
        entity.setData(ChampionsAttachments.DAMAGE_TYPE, key);
        setCount(entity, getDamageCount(entity) + 1);
      } else {
        setCount(entity, 1);
      }
    }

  }

  public static int getDamageCount(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.DAMAGE_COUNT).orElse(0);
  }

  public static void setCount(Entity entity, int count) {
    if (count > 0) {
      entity.setData(ChampionsAttachments.DAMAGE_COUNT, count);
    } else {
      entity.removeData(ChampionsAttachments.DAMAGE_COUNT);
    }
  }

}
