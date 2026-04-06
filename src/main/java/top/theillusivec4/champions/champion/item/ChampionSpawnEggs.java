package top.theillusivec4.champions.champion.item;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.Util;

public final class ChampionSpawnEggs {
	public static final ResourceKey<ChampionSpawnEgg> ZOMBIE = register("zombie");

	private ChampionSpawnEggs() {
	}

	public static void bootstrap(BootstrapContext<ChampionSpawnEgg> context) {
		HolderGetter<Rank> ranks = context.lookup(ChampionsRegistries.RANK);
		register(
				context,
				ZOMBIE,
				ChampionSpawnEgg.builder(Items.ZOMBIE_SPAWN_EGG)
						.setRank(ranks.getOrThrow(Ranks.SKILLED))
						.setPrefix(Component.literal("做完的做你的，做完你的做你的，别急别急，我心里有数"))
		);
	}

	private static ResourceKey<ChampionSpawnEgg> register(String name) {
		return ResourceKey.create(ChampionsRegistries.SPAWN_EGG, Util.id(name));
	}

	private static void register(BootstrapContext<ChampionSpawnEgg> context, ResourceKey<ChampionSpawnEgg> key, ChampionSpawnEgg.Builder builder) {

		context.register(
				key,
				builder.build(context)
		);
	}
}
