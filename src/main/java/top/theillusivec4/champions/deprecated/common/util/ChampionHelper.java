package top.theillusivec4.champions.deprecated.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.common.capabilities.ChampionAttachment;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.deprecated.common.config.ConfigEnums.Permission;
import top.theillusivec4.champions.deprecated.common.rank.Rank;
import top.theillusivec4.champions.world.entity.EntityTypes;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Deprecated
public class ChampionHelper {

  private static final Set<BlockPos> BEACON_POS = new HashSet<>();

  private static MinecraftServer server = null;

  /**
   * check entity is LivingEntity & Enemy, if allowChampionsList enabled, check entityType is in champions list,
   * based on allow allowChampionsPermission
   * @param entity The entity will be champions
   * @return true is valid champion entity type, else false
   */
  public static boolean isValidChampionEntity(@Nullable final Entity entity) {
    if (entity instanceof LivingEntity) {
      if (ChampionsConfig.allowChampionsList) {
        // When champions list is enabled, allow if entity is tagged and permission is WHITELIST
        if (ChampionsConfig.allowChampionsPermission == Permission.WHITELIST) {
          return entity.is(EntityTypes.Tags.ALLOW_CHAMPIONS);
        }
        // If entitiesPermission is BLACKLIST, reject the entity
        else if (ChampionsConfig.allowChampionsPermission == Permission.BLACKLIST) {
          return !entity.is(EntityTypes.Tags.ALLOW_CHAMPIONS);
        }
      } else {
        // If champions are not allowed, check if the entity is an enemy
        return entity instanceof Enemy;
      }
    }
    return false; // If entity is not a LivingEntity
  }
  /**
   * check entityType is Monster, if allowChampionsList enabled, check entityType is in champions list,
   * based on allow allowChampionsPermission
   * @param entityType The entity type will be champions
   * @return true is valid champion entity type, else false
   */
  public static boolean isValidChampionEntityType(final EntityType<?> entityType) {

    if (ChampionsConfig.allowChampionsList) {
      // When champions list is enabled, allow if entity is tagged and permission is WHITELIST
//      if (ChampionsConfig.allowChampionsPermission == Permission.WHITELIST) {
//        return entityType.is(ModEntityTypes.Tags.ALLOW_CHAMPIONS);
//      }
      // If entitiesPermission is BLACKLIST, reject the entity
//      else if (ChampionsConfig.allowChampionsPermission == Permission.BLACKLIST) {
//        return !entityType.is(ModEntityTypes.Tags.ALLOW_CHAMPIONS);
//      }
    }

    return entityType.getCategory() == MobCategory.MONSTER; // If entity is not a LivingEntity
  }

  /**
   * @param client champion to check
   * @return True if champion is valid Champion(Has ranks and affixes), else false.
   */
  public static boolean isValidChampion(IChampion.Client client) {
    var rank = client.getRank();
    return rank.isPresent() && rank.map(Tuple::getA).orElse(0) > 0 && !client.getAffixes().isEmpty();
  }

  /**
   * @param server champion to check
   * @return True if champion is valid Champion(Has ranks and affixes), else false.
   */
  public static boolean isValidChampion(IChampion.Server server) {
    var rank = server.getRank();
    return rank.isPresent() && rank.map(Rank::getTier).orElse(-1) > 0 && !server.getAffixes().isEmpty();
  }

  /**
   * Check entity is champion (have affixes and rank)
   *
   * @param entity the entity to check
   * @return true if entity is champion, false not champion
   */
  public static boolean isChampionEntity(Entity entity) {
    return ChampionAttachment.getAttachment(entity).map(champion -> ChampionHelper.isValidChampion(champion.getServer())).orElse(false);
  }

  /**
   * Check LivingEntity is potential champion entity.(can have data and spawn etc...)
   *
   * @param livingEntity that will check for.
   * @return True if this is not potential champion, else false.
   */
  public static boolean notPotential(final LivingEntity livingEntity) {
    return !isValidEntity(livingEntity) ||
      !isValidDimension(livingEntity.level().dimension().identifier()) ||
      nearActiveBeacon(livingEntity);
  }

  public static void addBeacon(BlockPos pos) {

    if (server != null) {
      BEACON_POS.add(pos);
    }
  }

  private static boolean isValidEntity(final LivingEntity livingEntity) {
    Identifier rl = BuiltInRegistries.ENTITY_TYPE.getKey(livingEntity.getType());

    String entity = rl.toString();

    if (ChampionsConfig.entitiesPermission == Permission.BLACKLIST) {
      return !ChampionsConfig.entitiesList.contains(entity);
    } else {
      return ChampionsConfig.entitiesList.contains(entity);
    }
  }

  public static boolean areEntitiesNearby(BlockPos pos, List<LivingEntity> livingEntities, EntityType<?> entityType) {
    for (LivingEntity livingentity : livingEntities) {
      if (livingentity.isAlive()
        && !livingentity.isRemoved()
        && pos.closerToCenterThan(livingentity.position(), 32.0)
        && livingentity.getType() == entityType) {
        return true;
      }
    }

    return false;
  }

  private static boolean isValidDimension(final Identifier resourceLocation) {
    String dimension = resourceLocation.toString();

    if (ChampionsConfig.dimensionPermission == Permission.BLACKLIST) {
      return !ChampionsConfig.dimensionList.contains(dimension);
    } else {
      return ChampionsConfig.dimensionList.contains(dimension);
    }
  }

  private static boolean nearActiveBeacon(final LivingEntity livingEntity) {
    int range = ChampionsConfig.beaconProtectionRange;

    if (range <= 0) {
      return false;
    }
    Set<BlockPos> toRemove = new HashSet<>();

    for (BlockPos pos : BEACON_POS) {
      Level level = livingEntity.level();

      if (!level.isLoaded(pos)) {
        continue;
      }
      BlockEntity blockEntity = level.getBlockEntity(pos);

      if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity && !blockEntity.isRemoved()) {

        if (livingEntity.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= range * range) {

          if (beaconBlockEntity.levels > 0) {
            return true;
          }
        }

      } else {
        toRemove.add(pos);
      }
    }
    BEACON_POS.removeAll(toRemove);
    return false;
  }

  public static void clearBeacons() {
    BEACON_POS.clear();
  }

  public static void setServer(MinecraftServer serverIn) {
    server = serverIn;
  }
}
