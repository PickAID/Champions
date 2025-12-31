package top.theillusivec4.champions.api.data;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.loot.AffixesPredicate;
import top.theillusivec4.champions.common.rank.RankManager;

import java.util.Optional;
import java.util.Set;

public record ChampionModifierCondition(Optional<Set<Identifier>> mobList, Optional<MinMaxBounds.Ints> tier,
                                        Optional<AffixesPredicate> affixes, ConfigEnums.Permission permission) {
  public static final MapCodec<ChampionModifierCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    NeoForgeExtraCodecs.setOf(Identifier.CODEC).optionalFieldOf("entity").forGetter(ChampionModifierCondition::mobList),
    MinMaxBounds.Ints.CODEC.optionalFieldOf("tier").forGetter(ChampionModifierCondition::tier),
    AffixesPredicate.CODEC.optionalFieldOf("affixes").forGetter(ChampionModifierCondition::affixes),
    StringRepresentable.fromEnum(ConfigEnums.Permission::values).fieldOf("permission").forGetter(ChampionModifierCondition::permission)
  ).apply(instance, ChampionModifierCondition::new));

  public boolean test(@NotNull IChampion champion) {
    var entityType = champion.getLivingEntity().getType();
    var entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
    var championAffixes = champion.getServer().getAffixes();
    var championTier = champion.getServer().getRank().orElse(RankManager.getEmptyRank()).getTier();
    // 首先处理 mobList + permission 的逻辑
    boolean entityMatches = mobList.map(mobs -> mobs.contains(entityId))
      .map(matches -> permission == ConfigEnums.Permission.WHITELIST ? matches : !matches)
      .orElse(true); // 如果没有 mobList，则默认通过

    // 其他条件保持不变
    return entityMatches
      && tier.map(t -> t.matches(championTier))
      .orElse(true)
      && affixes.map(affixesPredicate -> affixesPredicate.matches(championAffixes))
      .orElse(true);
  }
}
