package top.theillusivec4.champions.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import top.theillusivec4.champions.affix.Affixable;
import top.theillusivec4.champions.util.ChampionsUtil;

public final class ChampionsDataMaps {
  public static final DataMapType<EntityType<?>, Affixable> AFFIXABLE = DataMapType.builder(ChampionsUtil.id("affixable"), Registries.ENTITY_TYPE, Affixable.CODEC).build();

  private ChampionsDataMaps() {
  }
}
