package top.theillusivec4.champions.api.data;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.entity.EntityType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.loot.AffixesPredicate;
import top.theillusivec4.champions.common.rank.RankManager;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class ChampionModifierCondition {
	public static final MapCodec<ChampionModifierCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			setOf(ResourceLocation.CODEC).optionalFieldOf("entity").forGetter(ChampionModifierCondition::mobList),
			IntCodec.codec().optionalFieldOf("tier").forGetter(ChampionModifierCondition::tier),
			AffixesPredicate.codec().optionalFieldOf("affixes").forGetter(ChampionModifierCondition::affixes),
			IStringSerializable.fromEnum(ConfigEnums.Permission::values, (p) -> ConfigEnums.Permission.valueOf(p.toUpperCase())).fieldOf("permission").forGetter(ChampionModifierCondition::permission)
	).apply(instance, ChampionModifierCondition::new));
	private final Optional<Set<ResourceLocation>> mobList;
	private final Optional<MinMaxBounds.IntBound> tier;
	private final Optional<AffixesPredicate> affixes;
	private final ConfigEnums.Permission permission;

	public ChampionModifierCondition(Optional<Set<ResourceLocation>> mobList, Optional<MinMaxBounds.IntBound> tier,
	                                 Optional<AffixesPredicate> affixes, ConfigEnums.Permission permission) {
		this.mobList = mobList;
		this.tier = tier;
		this.affixes = affixes;
		this.permission = permission;
	}

	public static <T> Codec<Set<T>> setOf(final Codec<T> codec) {
		return Codec.list(codec).xmap(ImmutableSet::copyOf, ImmutableList::copyOf);
	}

	public boolean test(IChampion champion) {
		EntityType<?> entityType = champion.getLivingEntity().getType();
		ResourceLocation entityId = ForgeRegistries.ENTITIES.getKey(entityType);
		List<IAffix> championAffixes = champion.getServer().getAffixes();
		int championTier = champion.getServer().getRank().orElse(RankManager.getEmptyRank()).getTier();
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

	public Optional<Set<ResourceLocation>> mobList() {
		return mobList;
	}

	public Optional<MinMaxBounds.IntBound> tier() {
		return tier;
	}

	public Optional<AffixesPredicate> affixes() {
		return affixes;
	}

	public ConfigEnums.Permission permission() {
		return permission;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		ChampionModifierCondition that = (ChampionModifierCondition) obj;
		return Objects.equals(this.mobList, that.mobList) &&
				Objects.equals(this.tier, that.tier) &&
				Objects.equals(this.affixes, that.affixes) &&
				Objects.equals(this.permission, that.permission);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mobList, tier, affixes, permission);
	}

	@Override
	public String toString() {
		return "ChampionModifierCondition[" +
				"mobList=" + mobList + ", " +
				"tier=" + tier + ", " +
				"affixes=" + affixes + ", " +
				"permission=" + permission + ']';
	}

}
