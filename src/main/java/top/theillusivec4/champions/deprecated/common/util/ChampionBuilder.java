package top.theillusivec4.champions.deprecated.common.util;

import com.google.common.collect.ImmutableSortedMap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.api.AffixCategory;
import top.theillusivec4.champions.deprecated.api.IAffix;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.common.affix.core.AffixManager;
import top.theillusivec4.champions.deprecated.common.affix.core.AffixManager.AffixSettings;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.deprecated.common.rank.Rank;
import top.theillusivec4.champions.deprecated.common.rank.RankManager;
import top.theillusivec4.champions.deprecated.common.util.EntityManager.EntitySettings;

import java.util.*;

public class ChampionBuilder {

  private static final RandomSource RAND = RandomSource.createNewThreadLocalInstance();
  private static final ResourceLocation MAX_HEALTH_MODIFIER = Champions.getLocation("max_health_modifier");
  private static final ResourceLocation ATTACK_DAMAGE_MODIFIER = Champions.getLocation("attack_damage_modifier");
  private static final ResourceLocation ARMOR_MODIFIER = Champions.getLocation("armor_modifier");
  private static final ResourceLocation ARMOR_TOUGHNESS_MODIFIER = Champions.getLocation("armor_toughness_modifier");
  private static final ResourceLocation KNOCKBACK_RESISTANCE_MODIFIER = Champions.getLocation("knock_back_resistance_modifier");

  public static void spawn(final IChampion champion) {

    if (ChampionData.read(champion)) {
      return;
    }
    LivingEntity entity = champion.getLivingEntity();
    Rank newRank = ChampionBuilder.createRank(entity);
    champion.getServer().setRank(newRank);
    ChampionBuilder.applyGrowth(entity, newRank.getGrowthFactor());
    List<IAffix> newAffixes = ChampionBuilder.createAffixes(newRank, champion);
    champion.getServer().setAffixes(newAffixes);
    newAffixes.forEach(affix -> affix.onInitialSpawn(champion));
  }

  public static void spawnPreset(final IChampion champion, int tier, List<IAffix> affixes) {
    LivingEntity entity = champion.getLivingEntity();
    Rank newRank = RankManager.getRank(tier);
    champion.getServer().setRank(newRank);
    ChampionBuilder.applyGrowth(entity, newRank.getGrowthFactor());
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
      Optional<AffixSettings> settings = AffixManager.getSettings(affix.getIdentifier());
      return !affixesToAdd.contains(affix) && entitySettings
        .map(entitySettings1 -> entitySettings1.canApply(affix)).orElse(true) && settings
        .map(affixSettings -> affixSettings.canApply(champion)).orElse(true) && affix
        .canApply(champion);
    }).toList()));
    List<IAffix> randomList = new ArrayList<>();
    validAffixes.forEach((k, v) -> randomList.addAll(v));

    while (!randomList.isEmpty() && affixesToAdd.size() < size) {
      int randomIndex = RAND.nextInt(randomList.size());
      IAffix randomAffix = randomList.get(randomIndex);

      if (affixesToAdd.stream().allMatch(affix -> affix.isCompatible(randomAffix) && (
        randomAffix.getCategory() == AffixCategory.OFFENSE || (affix.getCategory() != randomAffix
          .getCategory())))) {
        affixesToAdd.add(randomAffix);
      }
      randomList.remove(randomIndex);
    }
    return affixesToAdd;
  }

  public static Rank createRank(final LivingEntity livingEntity) {

    if (ChampionHelper.isPotential(livingEntity)) {
      return RankManager.getLowestRank();
    }
    ImmutableSortedMap<Integer, Rank> ranks = RankManager.getRanks();

    if (ranks.isEmpty()) {
      Champions.LOGGER.error(
        "No rank configuration found! Please check the 'champions-ranks.toml' file in the 'serverconfigs'.");
      return RankManager.getLowestRank();
    }
    Integer[] tierRange = new Integer[]{null, null};
    EntityManager.getSettings(livingEntity.getType()).ifPresent(entitySettings -> {
      tierRange[0] = entitySettings.minTier;
      tierRange[1] = entitySettings.maxTier;
    });
    Integer firstTier = tierRange[0] != null ? tierRange[0] : ranks.firstKey();
    int maxTier = tierRange[1] != null ? tierRange[1] : -1;
    Iterator<Integer> iter = ranks.navigableKeySet().tailSet(firstTier, false).iterator();
    Rank result = ranks.get(firstTier);

    if (result == null) {
      Champions.LOGGER.error("Tier {} cannot be found in {}! Assigning lowest available rank to {}",
        firstTier, ranks, livingEntity);
      return RankManager.getLowestRank();
    }

    while (iter.hasNext() && (result.getTier() < maxTier || maxTier == -1)) {
      Rank rank = ranks.get(iter.next());

      if (rank == null) {
        return result;
      }
      float chance = rank.getChance();


      if (RAND.nextFloat() < chance) {
        result = rank;
      } else {
        return result;
      }
    }
    return result;
  }

  public static void applyGrowth(final LivingEntity livingEntity, float growthFactor) {

    if (growthFactor != 0) {
      applyAttributeModifier(livingEntity, Attributes.MAX_HEALTH, MAX_HEALTH_MODIFIER, ChampionsConfig.healthGrowth * growthFactor, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
      applyAttributeModifier(livingEntity, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER, ChampionsConfig.attackGrowth * growthFactor, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
      applyAttributeModifier(livingEntity, Attributes.ARMOR, ARMOR_MODIFIER, ChampionsConfig.armorGrowth * growthFactor, AttributeModifier.Operation.ADD_VALUE);
      applyAttributeModifier(livingEntity, Attributes.ARMOR_TOUGHNESS, ARMOR_TOUGHNESS_MODIFIER, ChampionsConfig.toughnessGrowth * growthFactor, AttributeModifier.Operation.ADD_VALUE);
      applyAttributeModifier(livingEntity, Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RESISTANCE_MODIFIER, ChampionsConfig.knockbackResistanceGrowth * growthFactor, AttributeModifier.Operation.ADD_VALUE);
    }
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
    Rank rank = oldServer.getRank().orElse(RankManager.getLowestRank());
    newServer.setRank(rank);
    ChampionBuilder.applyGrowth(newChampion.getLivingEntity(), rank.getGrowthFactor());
    List<IAffix> oldAffixes = oldChampion.getServer().getAffixes();
    newServer.setAffixes(oldAffixes);
    newServer.getAffixes().forEach(affix -> {
      affix.onInitialSpawn(newChampion);
      affix.onSpawn(newChampion);
    });
  }
}
