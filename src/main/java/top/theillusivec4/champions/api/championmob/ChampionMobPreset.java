package top.theillusivec4.champions.api.championmob;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import top.theillusivec4.champions.api.affix.AffixHelper;
import top.theillusivec4.champions.api.affix.EntityAffixes;
import top.theillusivec4.champions.api.affix.provider.AffixProvider;
import top.theillusivec4.champions.api.championmob.provider.ChampionMobPropertyProvider;

public record ChampionMobPreset(HolderSet<AffixProvider> affixes, ChampionMobPropertyProvider property) {
  public static final MapCodec<ChampionMobPreset> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    AffixProvider.LIST_CODEC.optionalFieldOf("affixes", HolderSet.empty()).forGetter(ChampionMobPreset::affixes),
    ChampionMobPropertyProvider.DIRECT_CODEC.fieldOf("property").forGetter(ChampionMobPreset::property)
  ).apply(instance, ChampionMobPreset::new));

  public void apply(ServerLevel level, RandomSource random, Entity entity, DifficultyInstance difficulty) {
    EntityType<?> entityType = entity.getType();
    EntityAffixes.Builder builder = EntityAffixes.builder();
    this.affixes.stream()
      .flatMap(affixProviderHolder -> affixProviderHolder.value().get(entityType, random, difficulty))
      .forEach(builder::add);
    EntityAffixes container = builder.build();
    ChampionMobProperty property = this.property.get();
    AffixHelper.set(entity, container);
    ChampionMobPropertyHelper.set(entity, property);
  }
}
