package top.theillusivec4.champions.data.registries;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.world.entity.affix.Affixes;
import top.theillusivec4.champions.world.entity.champion.Ranks;
import top.theillusivec4.champions.world.damagesource.ChampionsDamageTypes;
import top.theillusivec4.champions.world.item.champion.ChampionEggTemplates;

public final class ModdedRegistries {
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.DAMAGE_TYPE, ChampionsDamageTypes::bootstrap)
			.add(ChampionsRegistries.AFFIX, Affixes::bootstrap)
			.add(ChampionsRegistries.RANK, Ranks::bootstrap)
			.add(ChampionsRegistries.CHAMPION_MOB_EGG, ChampionEggTemplates::bootstrap);

  private ModdedRegistries() {
  }
}
