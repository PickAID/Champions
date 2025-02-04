package top.theillusivec4.champions.common.util;

import com.google.common.collect.ImmutableSortedMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixCategory;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.network.NetworkHandler;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.util.EntityManager.EntitySettings;

import java.util.*;

public class ChampionBuilder {
    private static final RandomSource RAND = RandomSource.createNewThreadLocalInstance();

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
        addAffixToList(size, affixesToAdd, validAffixes, RAND);
        return affixesToAdd;
    }

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
        int randomValue = RAND.nextInt(totalWeight);
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
        var livingEntity = champion.getLivingEntity();
        if (growthFactor != 0) {
            Champions.API.getAttributesModifierDataLoader().getLoadedData().forEach((identifier, value) -> {
                if (value.enable()) {
                    var attribute = ForgeRegistries.ATTRIBUTES.getDelegate(value.attributeType());
                    var setting = value.setting();
                    var matches = value.modifierCondition().map(championModifierCondition -> championModifierCondition.test(champion)).orElse(true);
                    if (matches) {
                        attribute.ifPresent(attributeValue -> applyAttributeModifier(livingEntity, attributeValue, identifier, setting, growthFactor));
                    }
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
                var attribute = ForgeRegistries.ATTRIBUTES.getDelegate(value.attributeType());
                var matches = value.modifierCondition().map(championModifierCondition -> championModifierCondition.test(champion)).orElse(true);
                if (matches) {
                    attribute.ifPresent(attributeValue -> {
                        var attributeInstance = livingEntity.getAttributes().getInstance(attributeValue.get());
                        if (attributeInstance != null) {
                            attributeInstance.getModifiers().forEach(attributeModifier -> {
                                var isChampionModifier = attributeModifier.getName().contains(Champions.MODID);
                                if (isChampionModifier) {
                                    attributeInstance.removePermanentModifier(attributeModifier.getId());
                                    if (ChampionsConfig.enableDebug) {
                                        var debugInfo = "Removed champion modifier: UUID: %s Name:%s Operation: %s".formatted(attributeModifier.getId(), attributeModifier.getName(), attributeModifier.getOperation());
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
            NetworkHandler.syncChampionDataToPlayerTrackingEntity(serverChampion, targetEntity);
        }
    }

    private static void applyAttributeModifier(LivingEntity livingEntity, Holder.Reference<Attribute> attributeValue, ResourceLocation modifierId, Pair<Double, AttributeModifier.Operation> setting, float growthFactor) {
        applyAttributeModifier(livingEntity, attributeValue, UUID.randomUUID(), Champions.getLocation(modifierId.getNamespace() + "_" + modifierId.getPath().split("\\.json")[0] + "_modifier"), setting.getFirst() * growthFactor, setting.getSecond());
    }

    public static void applyAttributeModifier(LivingEntity livingEntity, Holder<Attribute> attribute, UUID modifierUuid, ResourceLocation modifierName, double amount, AttributeModifier.Operation operation) {
        var attributeInstance = livingEntity.getAttributes().getInstance(attribute.get());
        var attributeModifier = new AttributeModifier(modifierUuid, modifierName.toString(), amount, operation);
        if (attributeInstance != null && !attributeInstance.hasModifier(attributeModifier)) {
            attributeInstance.addPermanentModifier(attributeModifier);
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
        ChampionBuilder.applyGrowth(newChampion, rank.getGrowthFactor());
        List<IAffix> oldAffixes = oldChampion.getServer().getAffixes();
        newServer.setAffixes(oldAffixes);
        oldAffixes.forEach(affix -> affix.onInitialSpawn(newChampion));
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
}
