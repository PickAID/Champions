package top.theillusivec4.champions.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.loot.AffixesPredicate;
import top.theillusivec4.champions.common.rank.RankManager;

import java.util.Optional;
import java.util.Set;

public record ChampionModifierCondition(Optional<Set<ResourceLocation>> mobList, Optional<MinMaxBounds.Ints> tier,
                                        Optional<AffixesPredicate> affixes, ConfigEnums.Permission permission) {
    public static final MapCodec<ChampionModifierCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            setOf(ResourceLocation.CODEC).optionalFieldOf("entity").forGetter(ChampionModifierCondition::mobList),
            AffixSetting.INTS_CODEC.optionalFieldOf("tier").forGetter(ChampionModifierCondition::tier),
            AffixesPredicate.codec().optionalFieldOf("affixes").forGetter(ChampionModifierCondition::affixes),
            StringRepresentable.fromEnum(ConfigEnums.Permission::values).fieldOf("permission").forGetter(ChampionModifierCondition::permission)
    ).apply(instance, ChampionModifierCondition::new));

    public static <T> Codec<Set<T>> setOf(final Codec<T> codec) {
        return Codec.list(codec).xmap(ImmutableSet::copyOf, ImmutableList::copyOf);
    }

    public boolean test(@NotNull IChampion champion) {
        var entityType = champion.getLivingEntity().getType();
        var entityId = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        var championAffixes = champion.getServer().getAffixes();
        var championTier = champion.getServer().getRank().orElse(RankManager.getEmptyRank()).getTier();
        // 首先处理 mobList + permission 的逻辑
        boolean entityMatches = mobList.map(mobs -> mobs.contains(entityId))
                .map(matches -> (permission == ConfigEnums.Permission.WHITELIST) == matches)
                .orElse(true); // 如果没有 mobList，则默认通过

        // 其他条件保持不变
        return entityMatches
                && tier.map(t -> t.matches(championTier))
                .orElse(true)
                && affixes.map(affixesPredicate -> affixesPredicate.matches(championAffixes))
                .orElse(true);
    }
}
