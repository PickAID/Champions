package top.theillusivec4.champions.common.util;

import com.google.common.collect.ImmutableSortedMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixCategory;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.util.EntityManager.EntitySettings;

import java.util.*;

public class ChampionBuilder {

  /**
   * Read champion data, or create affix by creating new rank levels.<br/>
   * only if new rank tier >= 1, will set rank and new affixes to this champion
   *
   * @param champion to read or construct new champion
   */
  public static void spawn(final IChampion champion) {

    if (ChampionData.read(champion)) {
      return;
    }
    LivingEntity entity = champion.getLivingEntity();
    Rank newRank = ChampionBuilder.createRank(entity);
    if (newRank != null && newRank.getTier() >= 1) {
      champion.getServer().setRank(newRank);
      ChampionBuilder.applyGrowth(champion, newRank.getGrowthFactor());
      List<IAffix> newAffixes = ChampionBuilder.createAffixes(newRank, champion);
      champion.getServer().setAffixes(newAffixes);
      newAffixes.forEach(affix -> affix.onInitialSpawn(champion));
    }
  }

  public static void spawnPreset(final IChampion champion, int tier, List<IAffix> affixes) {
    Rank newRank = RankManager.getRank(tier);
    champion.getServer().setRank(newRank);
    ChampionBuilder.applyGrowth(champion, newRank.getGrowthFactor());
    affixes = affixes.isEmpty() ? ChampionBuilder.createAffixes(newRank, champion) : affixes;
    champion.getServer().setAffixes(affixes);
    affixes.forEach(affix -> affix.onInitialSpawn(champion));
  }

  public static List<IAffix> createAffixes(final Rank rank, final IChampion champion) {
    int size = rank.getNumAffixes();
    List<IAffix> affixesToAdd = new ArrayList<>();
    Optional<EntitySettings> entitySettings = EntityManager
      .getSettings(champion.getLivingEntity().getType());

    if (size > 0) {
      entitySettings.ifPresent(settings -> {

        if (settings.presetAffixes != null) {
          affixesToAdd.addAll(settings.presetAffixes);
        }
      });
      rank.getPresetAffixes().forEach(affix -> {

        if (!affixesToAdd.contains(affix)) {
          affixesToAdd.add(affix);
        }
      });
    }
    Map<AffixCategory, List<IAffix>> allAffixes = Champions.API.getCategoryMap();
    Map<AffixCategory, List<IAffix>> validAffixes = new HashMap<>();

    for (AffixCategory category : Champions.API.getCategories()) {
      validAffixes.put(category, new ArrayList<>());
    }
    allAffixes.forEach((k, v) -> validAffixes.get(k).addAll(v.stream().filter(affix -> {
      /*
        return new affix list that can apply with entity and affix settings, and affix can apply to champion.
       */
      return !affixesToAdd.contains(affix) && entitySettings
        .map(entitySettings1 -> entitySettings1.canApply(affix)).orElse(true) && affix
        .canApply(champion);
    }).toList()));
    addAffixToList(size, affixesToAdd, validAffixes, champion.getLivingEntity().getRandom());
    return affixesToAdd;
  }

  /**
   * Add random affix from random affixes list
   *
   * @param size         How much random affix to add
   * @param toModifier   Affix list that will add random affix to it.
   * @param validAffixes Affix list that can apply with entity and affix settings, and can apply to champion.
   * @param rand         mojang random source used get affix from random list.
   */
  public static void addAffixToList(int size, List<IAffix> toModifier, Map<AffixCategory, List<IAffix>> validAffixes, RandomSource rand) {
    List<IAffix> randomList = new ArrayList<>();
    validAffixes.forEach((k, v) -> randomList.addAll(v));

    while (!randomList.isEmpty() && toModifier.size() < size) {
      int randomIndex = rand.nextInt(randomList.size());
      IAffix randomAffix = randomList.get(randomIndex);

      if (toModifier.stream().allMatch(affix -> affix.isCompatible(randomAffix) && (
        randomAffix.getCategory() == AffixCategory.OFFENSE || (affix.getCategory() != randomAffix
          .getCategory())))) {
        toModifier.add(randomAffix);
      }
      randomList.remove(randomIndex);
    }
  }

  /**
   * Create new rank for this LivingEntity.
   *
   * @param livingEntity to create new rank with this entity settings
   * @return lowest rank if this living entity not potential, else living entity with new rank that applied entity settings.
   */
  public static Rank createRank(final LivingEntity livingEntity) {

    if (ChampionHelper.notPotential(livingEntity)) {
      return RankManager.getEmptyRank();
    }
    ImmutableSortedMap<Integer, Rank> ranks = RankManager.getRanks();

    if (ranks.isEmpty()) {
      Champions.LOGGER.error(
        "No rank configuration found! Please check the 'champions-ranks.toml' file in the 'serverconfigs'.");
      return RankManager.getEmptyRank();
    }
    Integer[] tierRange = new Integer[]{null, null};

    EntityManager.getSettings(livingEntity.getType()).ifPresent(entitySettings -> {
      tierRange[0] = entitySettings.minTier;
      tierRange[1] = entitySettings.maxTier;
    });

    Integer firstTier = tierRange[0] != null ? tierRange[0] : ranks.firstKey();
    int maxTier = tierRange[1] != null ? tierRange[1] : -1;

    ImmutableSortedMap<Integer, Rank> filteredRanks;

    if (maxTier == -1) {
      /* 如果 maxTier 未设置，则仅过滤 firstTier 以上的 Rank */
      filteredRanks = ranks.tailMap(firstTier, true);
    } else {
      /* 如果 maxTier 设置了，过滤 firstTier 和 maxTier 范围内的 Rank */
      filteredRanks = ranks.tailMap(firstTier, true).headMap(maxTier + 1, true);
    }

    // 如果没有符合条件的 Rank，返回 EmptyRank
    if (filteredRanks.isEmpty()) {
      Champions.LOGGER.warn(
        "No valid ranks found in the specified range! Assigning EmptyRank to {}", livingEntity);
      return RankManager.getEmptyRank();
    }
    int totalWeight = filteredRanks.values().stream()
      .mapToInt(Rank::getWeight)
      .sum();

    // 如果所有权重为 0，返回 EmptyRank
    if (totalWeight <= 0) {
      Champions.LOGGER.warn(
        "All ranks have zero weight! Assigning EmptyRank to {}", livingEntity);
      return RankManager.getEmptyRank();
    }
    int randomValue = livingEntity.getRandom().nextInt(totalWeight);
    int cumulativeWeight = 0;

    for (Rank rank : filteredRanks.values()) {
      cumulativeWeight += rank.getWeight();
      if (randomValue < cumulativeWeight) {
        return rank;
      }
    }

    return RankManager.getEmptyRank();
  }

  public static void applyGrowth(final IChampion champion, float growthFactor) {
    if (growthFactor != 0) {
      Champions.API.getAttributesModifierDataLoader().getLoadedData().forEach((identifier, value) -> {
        if (!value.enable()) {
          return;
        }

        var attribute = BuiltInRegistries.ATTRIBUTE.getHolder(value.attributeType());
        var setting = value.setting();
        var matches = value.modifierCondition().map(championModifierCondition -> championModifierCondition.test(champion)).orElse(true);
        if (matches) {
          attribute.ifPresent(attributeValue -> applyAttributeModifier(champion.getLivingEntity(), attributeValue, identifier, setting, growthFactor));
        }

      });
    }
  }

  private static void applyAttributeModifier(LivingEntity livingEntity, Holder.Reference<Attribute> attributeValue, ResourceLocation modifierId, Pair<Double, AttributeModifier.Operation> setting, float growthFactor) {
    applyAttributeModifier(livingEntity, attributeValue, Champions.getLocation(modifierId.getNamespace() + "_" + modifierId.getPath().split("\\.json")[0] + "_modifier"), setting.getFirst() * growthFactor, setting.getSecond());
  }

  public static void applyAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, ResourceLocation modifierId, double amount, AttributeModifier.Operation operation) {
    var attributeInstance = livingEntity.getAttributes().getInstance(attribute);
    if (attributeInstance != null && !attributeInstance.hasModifier(modifierId)) {
      attributeInstance.addOrReplacePermanentModifier(new AttributeModifier(modifierId, amount, operation));
      if (attributeInstance.getAttribute() == Attributes.MAX_HEALTH) {
        livingEntity.setHealth(livingEntity.getMaxHealth());
      }
    }
  }

  public static void copy(IChampion oldChampion, IChampion newChampion) {
    IChampion.Server oldServer = oldChampion.getServer();
    IChampion.Server newServer = newChampion.getServer();
    Rank rank = oldServer.getRank().orElse(RankManager.getEmptyRank());
    newServer.setRank(rank);
    ChampionBuilder.applyGrowth(newChampion, rank.getGrowthFactor());
    List<IAffix> oldAffixes = oldChampion.getServer().getAffixes();
    newServer.setAffixes(oldAffixes);
    newServer.getAffixes().forEach(affix -> {
      affix.onInitialSpawn(newChampion);
      affix.onSpawn(newChampion);
    });
  }
}
