package top.theillusivec4.champions.core.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.ProjectileTemplate;
import top.theillusivec4.champions.api.affix.effect.AffixEntityEffect;
import top.theillusivec4.champions.api.affix.effect.AffixLocationBasedEffect;
import top.theillusivec4.champions.api.affix.effect.AffixValueEffect;
import top.theillusivec4.champions.api.affix.provider.AffixProvider;
import top.theillusivec4.champions.api.championmob.Rank;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.util.Util;
import top.theillusivec4.champions.api.championmob.provider.ChampionMobPropertyProvider;
import top.theillusivec4.champions.world.item.champion.ChampionEggTemplate;

public final class ChampionsRegistries {
	public static final ResourceKey<Registry<MapCodec<? extends AffixProvider>>> AFFIX_PROVIDER_TYPE = register("affix_provider_type");
	public static final ResourceKey<Registry<MapCodec<? extends ProjectileTemplate>>> PROJECTILE_TEMPLATE_TYPE = register("projectile_template_type");
	public static final ResourceKey<Registry<MapCodec<? extends LevelBasedValue>>> LEVEL_BASED_VALUE_TYPE = register("level_based_value_type");
	public static final ResourceKey<Registry<MapCodec<? extends AffixLocationBasedEffect>>> AFFIX_LOCATION_BASED_EFFECT_TYPE = register("affix_location_based_effect_type");
	public static final ResourceKey<Registry<MapCodec<? extends AffixEntityEffect>>> AFFIX_ENTITY_EFFECT_TYPE = register("affix_entity_effect_type");
	public static final ResourceKey<Registry<MapCodec<? extends AffixValueEffect>>> AFFIX_VALUE_EFFECT_TYPE = register("affix_value_effect_type");
	public static final ResourceKey<Registry<MapCodec<? extends ChampionMobPropertyProvider>>> CHAMPION_MOB_PROPERTY_PROVIDER_TYPE = register("champion_mob_property_provider_type");
	public static final ResourceKey<Registry<Affix>> AFFIX = register("affix");
	public static final ResourceKey<Registry<AffixProvider>> AFFIX_PROVIDER = register("affix_provider");
	public static final ResourceKey<Registry<DataComponentType<?>>> AFFIX_EFFECT_COMPONENT_TYPE = register("affix_effect_component_type");
	public static final ResourceKey<Registry<Rank>> RANK = register("rank");
	public static final ResourceKey<Registry<ChampionEggTemplate>> CHAMPION_MOB_EGG = register("champion_mob_egg");
	public static final ResourceKey<Registry<ChampionMobPropertyProvider>> CHAMPION_MOB_PROPERTY_PROVIDER = register("champion_mob_property_provider");

	private ChampionsRegistries() {
	}

	private static <T> ResourceKey<Registry<T>> register(String name) {
		return ResourceKey.createRegistryKey(Util.id(name));
	}
}
