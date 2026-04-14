package top.theillusivec4.champions.world.entity.champion;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;
import top.theillusivec4.champions.world.entity.affix.*;
import top.theillusivec4.champions.world.entity.affix.provider.AffixProvider;
import top.theillusivec4.champions.world.entity.champion.property.ChampionProperty;
import top.theillusivec4.champions.world.entity.champion.property.ChampionPropertyHelper;
import top.theillusivec4.champions.world.entity.champion.property.provider.ChampionPropertyProvider;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.tags.AffixTags;

public record ChampionMobPreset(HolderSet<AffixProvider> affixes, ChampionPropertyProvider property) {
  public static final MapCodec<ChampionMobPreset> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    AffixProvider.LIST_CODEC.optionalFieldOf("affixes", HolderSet.empty()).forGetter(ChampionMobPreset::affixes),
    ChampionPropertyProvider.DIRECT_CODEC.fieldOf("property").forGetter(ChampionMobPreset::property)
  ).apply(instance, ChampionMobPreset::new));

  public static void bootstrap(HolderLookup.Provider provider, DataMapProvider.Builder<ChampionMobPreset, EntityType<?>> context) {
    HolderGetter<Affix> affixes = provider.lookupOrThrow(ChampionsRegistries.AFFIX);
    HolderGetter<Rank> ranks = provider.lookupOrThrow(ChampionsRegistries.RANK);
    register(
      context,
      EntityType.HUSK,
      HolderSet.direct(
        Holder.direct(AffixProvider.single(affixes.getOrThrow(Affixes.ADAPTABLE), ConstantInt.of(1)))
      ),
      ChampionPropertyProvider.byRank(ranks.getOrThrow(Ranks.ELITE))
    );
    register(
      context,
      EntityType.ZOMBIE,
      HolderSet.direct(
        Holder.direct(AffixProvider.single(affixes.getOrThrow(Affixes.ADAPTABLE), UniformInt.of(1, 3))),
        Holder.direct(AffixProvider.single(affixes.getOrThrow(Affixes.DAMPENING), UniformInt.of(1, 3)))
      ),
      ChampionPropertyProvider.single(
        ChampionProperty.builder()
          .prefix(Component.literal("Test"))
          .tier(3)
          .color(TextColor.fromLegacyFormat(ChatFormatting.RED))
          .boss(false)
      )
    );
    register(
      context,
      EntityType.SKELETON,
      HolderSet.direct(
        Holder.direct(AffixProvider.byCost(affixes.getOrThrow(AffixTags.DAMAGE_PROTECTION_EXCLUSIVE), UniformInt.of(20, 60))),
        Holder.direct(AffixProvider.byCostWithDifficulty(affixes.getOrThrow(AffixTags.DAMAGE_EXCLUSIVE), 20, 80))
      ),
      ChampionPropertyProvider.byRank(ranks.getOrThrow(Ranks.COMMON))
    );
  }

  private static void register(DataMapProvider.Builder<ChampionMobPreset, EntityType<?>> context, EntityType<?> entityType, HolderSet<AffixProvider> affixes, ChampionPropertyProvider property) {
    context.add(
      BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entityType),
      new ChampionMobPreset(affixes, property),
      false
    );
  }


  public void apply(ServerLevel level, RandomSource random, Entity entity, DifficultyInstance difficulty) {
    EntityType<?> entityType = entity.getType();
    EntityAffixes.Builder builder = EntityAffixes.builder();
    this.affixes.stream()
      .flatMap(affixProviderHolder -> affixProviderHolder.value().get(entityType, random, difficulty))
      .forEach(builder::add);
    EntityAffixes container = builder.build();
    ChampionProperty property = this.property.get();
    AffixHelper.set(entity, container);
    ChampionPropertyHelper.set(entity, property);
  }
}
