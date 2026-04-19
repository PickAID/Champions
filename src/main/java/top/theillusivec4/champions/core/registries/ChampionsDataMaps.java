package top.theillusivec4.champions.core.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import top.theillusivec4.champions.util.Util;
import top.theillusivec4.champions.api.affix.Affixable;
import top.theillusivec4.champions.api.championmob.ChampionMobPreset;

public final class ChampionsDataMaps {
  public static final DataMapType<EntityType<?>, Affixable> AFFIXABLE = DataMapType.builder(Util.id("affixable"), Registries.ENTITY_TYPE, Affixable.CODEC).build();
  public static final DataMapType<EntityType<?>, ChampionMobPreset> CHAMPION_MOB_PRESET = DataMapType.builder(Util.id("champion_mob_preset"), Registries.ENTITY_TYPE, ChampionMobPreset.MAP_CODEC.codec()).build();

  private ChampionsDataMaps() {
  }
}
