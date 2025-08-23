package top.theillusivec4.champions.common.util;

import com.google.common.collect.ImmutableSortedMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.Sephirah;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.event.customEvent.ChampionsEventHooks;
import top.theillusivec4.champions.common.network.SPacketSyncChampion;
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
    if (!ChampionsEventHooks.onAddSephiahName(champion, null)) {
      ChampionBuilder.removeSephirah(champion.getLivingEntity());
    }
    LivingEntity entity = champion.getLivingEntity();
    Rank newRank = ChampionBuilder.createRank(entity);
    if (newRank != null && newRank.getTier() >= 1) {
      if (!ChampionsEventHooks.onPreChampionSpawn(champion)) {
        return; // 事件被取消
      }
      champion.getServer().setRank(newRank);
      ChampionBuilder.applyGrowth(champion, newRank.getGrowthFactor());
      List<IAffix> newAffixes = ChampionBuilder.createAffixes(newRank, champion);
      champion.getServer().setAffixes(newAffixes);
      Utils.consumeIfLifeCycle(newAffixes, lifecycle -> lifecycle.onInitialSpawn(champion));
      // only post when champion spawned
      ChampionsEventHooks.onPostChampionSpawn(champion);
    }
  }

  public static void spawnPreset(final IChampion champion, int tier, List<IAffix> affixes) {
    Rank newRank = RankManager.getRank(tier);
    champion.getServer().setRank(newRank);
    ChampionBuilder.applyGrowth(champion, newRank.getGrowthFactor());
    affixes = affixes.isEmpty() ? ChampionBuilder.createAffixes(newRank, champion) : affixes;
    champion.getServer().setAffixes(affixes);
    Utils.consumeIfLifeCycle(affixes, lifecycle -> lifecycle.onInitialSpawn(champion));
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

    // 过滤实体对应的 tier 范围
    Integer[] tierRange = new Integer[]{null, null};
    EntityManager.getSettings(livingEntity.getType()).ifPresent(entitySettings -> {
      tierRange[0] = entitySettings.minTier;
      tierRange[1] = entitySettings.maxTier;
    });

    Integer firstTier = tierRange[0] != null ? tierRange[0] : ranks.firstKey();
    int maxTier = tierRange[1] != null ? tierRange[1] : -1;

    ImmutableSortedMap<Integer, Rank> filteredRanks;
    if (maxTier == -1) {
      filteredRanks = ranks.tailMap(firstTier, true);
    } else {
      filteredRanks = ranks.tailMap(firstTier, true).headMap(maxTier + 1, false);
    }

    if (filteredRanks.isEmpty()) {
      Champions.LOGGER.warn(
        "No valid ranks found in the specified range! Assigning EmptyRank to {}", livingEntity);
      return RankManager.getEmptyRank();
    }

    // 推荐方案：基于累积概率的偏移法
    return selectRankWithCumulativeBias(livingEntity, filteredRanks);

    // 备选方案：多次抽取取最大值法
    // return selectRankWithMultipleDraws(livingEntity, filteredRanks);
  }

  /**
   * 方案1：基于累积概率的偏移法（推荐）
   * 通过变换累积概率分布来实现偏向，避免权重操作的问题
   */
  private static Rank selectRankWithCumulativeBias(LivingEntity livingEntity,
                                                   ImmutableSortedMap<Integer, Rank> filteredRanks) {
    double factor = hasSephirah(livingEntity) ? getSephiahFactor(livingEntity) : 1.0;

    // 计算原始累积概率分布
    List<Rank> rankList = new ArrayList<>(filteredRanks.values());
    int totalWeight = 0;
    for (Rank rank : rankList) {
      totalWeight += rank.getWeight();
    }

    double[] originalProbabilities = new double[rankList.size()];
    double[] cumulativeProbabilities = new double[rankList.size()];

    double cumulative = 0.0;
    for (int i = 0; i < rankList.size(); i++) {
      double prob = (double) rankList.get(i).getWeight() / totalWeight;
      originalProbabilities[i] = prob;
      cumulative += prob;
      cumulativeProbabilities[i] = cumulative;
    }

    /*Champions.LOGGER.info("=== Original Rank Distribution ===");
    for (int i = 0; i < rankList.size(); i++) {
      Champions.LOGGER.info("Rank {}: probability={}, cumulative={}",
        rankList.get(i).getTier(), originalProbabilities[i], cumulativeProbabilities[i]);
    }*/

    // 生成随机数并应用偏向
    double rawRandom = livingEntity.getRandom().nextDouble();

    // 使用幂函数变换累积概率，让随机数更容易命中高等级区间
    // factor > 1 时，随机数向1偏移，更容易选中高等级
    double biasedRandom = Math.pow(rawRandom, 1.0 / factor);

    /*Champions.LOGGER.info("Random generation: raw={}, biased={}, factor={}",
      rawRandom, biasedRandom, factor);
    */
    // 根据偏移后的随机数选择rank
    for (int i = 0; i < rankList.size(); i++) {
      if (biasedRandom <= cumulativeProbabilities[i]) {
        /*Champions.LOGGER.info("Selected rank {}, Sephirah mark: {}",
          rankList.get(i).getTier(), hasSephirah(livingEntity));*/
        return rankList.get(i);
      }
    }

    // 保底返回最高等级
    return rankList.get(rankList.size() - 1);
  }

  /**
   * 方案2：多次抽取取最大值法
   * 进行多次独立抽取，选择其中等级最高的结果
   */
  private static Rank selectRankWithMultipleDraws(LivingEntity livingEntity,
                                                  ImmutableSortedMap<Integer, Rank> filteredRanks) {
    double factor = hasSephirah(livingEntity) ? getSephiahFactor(livingEntity) : 1.0;

    // 根据factor决定抽取次数
    int drawCount = Math.max(1, (int) Math.round(factor));

    List<Rank> rankList = new ArrayList<>(filteredRanks.values());
    int totalWeight = 0;
    int[] weights = new int[rankList.size()];

    for (int i = 0; i < rankList.size(); i++) {
      weights[i] = rankList.get(i).getWeight();
      totalWeight += weights[i];
    }

    Champions.LOGGER.info("=== Multiple Draws Method ===");
    Champions.LOGGER.info("Draw count: {}, factor: {}", drawCount, factor);

    Rank bestRank = null;
    int bestTier = -1;

    for (int draw = 0; draw < drawCount; draw++) {
      int randomValue = livingEntity.getRandom().nextInt(totalWeight);

      int cumulativeWeight = 0;
      for (int i = 0; i < rankList.size(); i++) {
        cumulativeWeight += weights[i];
        if (randomValue < cumulativeWeight) {
          Rank selectedRank = rankList.get(i);
          Champions.LOGGER.info("Draw {}: selected rank {}", draw + 1, selectedRank.getTier());

          if (bestRank == null || selectedRank.getTier() > bestTier) {
            bestRank = selectedRank;
            bestTier = selectedRank.getTier();
          }
          break;
        }
      }
    }

    Champions.LOGGER.info("Final selected rank: {}, Sephirah mark: {}",
      bestRank.getTier(), hasSephirah(livingEntity));
    return bestRank;
  }

  /**
   * 方案3：分段概率重新分配法
   * 将概率空间分为几个段，高等级段获得更多概率
   */
  private static Rank selectRankWithSegmentedReallocation(LivingEntity livingEntity,
                                                          ImmutableSortedMap<Integer, Rank> filteredRanks) {
    double factor = hasSephirah(livingEntity) ? getSephiahFactor(livingEntity) : 1.0;

    List<Rank> rankList = new ArrayList<>(filteredRanks.values());
    int rankCount = rankList.size();

    // 将ranks分为三个段：低、中、高
    int lowEnd = rankCount / 3;
    int midEnd = (rankCount * 2) / 3;

    // 计算原始权重分布
    int totalWeight = 0;
    int[] originalWeights = new int[rankCount];
    for (int i = 0; i < rankCount; i++) {
      originalWeights[i] = rankList.get(i).getWeight();
      totalWeight += originalWeights[i];
    }

    // 根据factor重新分配概率
    double[] newProbabilities = new double[rankCount];
    double totalNewProb = 0.0;

    for (int i = 0; i < rankCount; i++) {
      double originalProb = (double) originalWeights[i] / totalWeight;
      double multiplier = 1.0;

      if (i >= midEnd) {
        // 高等级段：增加概率
        multiplier = factor;
      } else if (i >= lowEnd) {
        // 中等级段：保持或略微减少
        multiplier = Math.sqrt(factor);
      } else {
        // 低等级段：减少概率
        multiplier = 1.0 / Math.sqrt(factor);
      }

      newProbabilities[i] = originalProb * multiplier;
      totalNewProb += newProbabilities[i];
    }

    // 归一化概率
    for (int i = 0; i < rankCount; i++) {
      newProbabilities[i] /= totalNewProb;
    }

    Champions.LOGGER.info("=== Segmented Reallocation Method ===");
    double cumulative = 0.0;
    for (int i = 0; i < rankCount; i++) {
      cumulative += newProbabilities[i];
      Champions.LOGGER.info("Rank {}: original={}, new={}, cumulative={}",
        rankList.get(i).getTier(),
        (double)originalWeights[i] / totalWeight,
        newProbabilities[i],
        cumulative);
    }

    // 根据新概率分布选择
    double randomValue = livingEntity.getRandom().nextDouble();
    Champions.LOGGER.info("Random value: {}, factor: {}", randomValue, factor);

    double cumulativeProb = 0.0;
    for (int i = 0; i < rankCount; i++) {
      cumulativeProb += newProbabilities[i];
      if (randomValue <= cumulativeProb) {
        Champions.LOGGER.info("Selected rank {}, Sephirah mark: {}",
          rankList.get(i).getTier(), hasSephirah(livingEntity));
        return rankList.get(i);
      }
    }

    return rankList.get(rankCount - 1);
  }


  private static boolean hasSephirah(LivingEntity livingEntity) {
    return livingEntity.getPersistentData().contains("sephirahName");
  }

  /**
   * Set sephirah type of livingEntity
   *
   * @param livingEntity target to set sephirah
   * @param sephirahName the name of sephirahName, if not correct, throws IllegalArgumentException
   * @return true if successful, else false
   */
  public static boolean addSephirah(LivingEntity livingEntity, String sephirahName) throws IllegalArgumentException {
    if (!hasSephirah(livingEntity)) {
      livingEntity.getPersistentData().putString("sephirahName", Sephirah.valueOf(sephirahName).getSerializedName());
      return true;
    }
    return false;
  }

  public static boolean removeSephirah(LivingEntity livingEntity) throws IllegalArgumentException {
    if (hasSephirah(livingEntity)) {
      livingEntity.getPersistentData().remove("sephirahName");
      return true;
    }
    return false;
  }

  private static double getSephiahFactor(LivingEntity livingEntity) {
    String sephirahName = livingEntity.getPersistentData().getString("sephirahName");
    Double factor = ChampionsConfig.sephirahFactors.get(Sephirah.valueOf(sephirahName)).get();
    return factor.isNaN() ? 1.0 : factor;
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

  /**
   * Reset champion's attribute's modifier, only can use on server side.
   *
   * @param champion Need reset attribute target
   */
  public static void resetChampionModifiers(final IChampion champion) {
    var livingEntity = champion.getLivingEntity();
    var playerList = Objects.requireNonNull(livingEntity.getServer()).getPlayerList();
    Champions.API.getAttributesModifierDataLoader().getLoadedData().forEach((identifier, value) -> {
      if (value.enable()) {
        var attribute = BuiltInRegistries.ATTRIBUTE.getHolder(value.attributeType());
        var matches = value.modifierCondition().map(championModifierCondition -> championModifierCondition.test(champion)).orElse(true);
        if (matches) {
          attribute.ifPresent(attributeValue -> {
            var attributeInstance = livingEntity.getAttributes().getInstance(attributeValue);
            if (attributeInstance != null) {
              attributeInstance.getModifiers().forEach(attributeModifier -> {
                var isChampionModifier = attributeModifier.id().getNamespace().equals(Champions.MODID);
                if (isChampionModifier) {
                  attributeInstance.removeModifier(attributeModifier);
                  if (ChampionsConfig.enableDebug) {
                    var debugInfo = "Removed champion modifier: Name:%s Operation: %s Amount: %s".formatted(attributeModifier.id(), attributeModifier.operation(), attributeModifier.amount());
                    Champions.LOGGER.debug(debugInfo);
                    playerList.getPlayers().stream().filter(p -> p.hasPermissions(2)).forEach(serverPlayer -> serverPlayer.sendSystemMessage(Component.literal(debugInfo)));
                  }
                }
              });
            }
          });
        }
      }
    });
  }

  /**
   * Resets the champion's growth, affixes, and rank, then updates the client.
   * <p>
   * This is a convenience method that resets all aspects of the champion.
   *
   * @param champion The champion whose attributes, rank, and affixes should be reset.
   */
  public static void resetAndUpdate(IChampion champion) {
    resetAndUpdate(champion, true, true, true);
  }

  /**
   * Resets specific aspects of the champion and optionally updates the client.
   * <p>
   * This method allows fine-grained control over which attributes to reset:
   * - Growth modifiers (e.g., stat boosts)
   * - Rank (e.g., champion tier)
   * - Affixes (e.g., special abilities)
   * <p>
   * If either rank or affixes are reset, the update will be sent to the client.
   *
   * @param champion    The champion to reset.
   * @param resetGrowth If true, resets the champion's growth modifiers.
   * @param resetRank   If true, resets the champion's rank to an empty rank.
   * @param resetAffix  If true, clears the champion's affixes.
   */
  public static void resetAndUpdate(final IChampion champion, final boolean resetGrowth, final boolean resetRank, final boolean resetAffix) {

    var serverChampion = champion.getServer();
    var targetEntity = champion.getLivingEntity();

    if (resetGrowth) {
      ChampionBuilder.resetChampionModifiers(champion);
    }

    if (resetRank) {
      serverChampion.setRank(RankManager.getEmptyRank());
    }

    if (resetAffix) {
      serverChampion.setAffixes(List.of());
    }

    // only manually sync to client because attributes will automatically sync by mojang
    if (resetAffix || resetRank) {
      SPacketSyncChampion.syncChampionDataToPlayerTrackingEntity(serverChampion, targetEntity);
    }
  }

  private static void applyAttributeModifier(LivingEntity livingEntity, Holder.Reference<Attribute> attributeValue, ResourceLocation modifierId, Pair<Double, AttributeModifier.Operation> setting, float growthFactor) {
    applyAttributeModifier(livingEntity, attributeValue, Utils.getLocation(modifierId.getNamespace() + "_" + modifierId.getPath().split("\\.json")[0] + "_modifier"), setting.getFirst() * growthFactor, setting.getSecond());
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
    Utils.consumeIfLifeCycle(newServer.getAffixes(), lifecycle -> {
      lifecycle.onInitialSpawn(newChampion);
      lifecycle.onSpawn(newChampion);
    });
  }
}
