package top.theillusivec4.champions.world.loot.providers.number;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.value.based.lootcontext.LevelBasedValue;

import java.util.function.Supplier;

public final class NumberProviders {
	private static final DeferredRegister<MapCodec<? extends NumberProvider>> DEFERRED_REGISTER = DeferredRegister.create(Registries.LOOT_NUMBER_PROVIDER_TYPE, Champions.MODID);
	public static final Supplier<MapCodec<ChampionLevelProvider>> CHAMPION_LEVEL = register("champion_level", ChampionLevelProvider.MAP_CODEC);

	private NumberProviders() {
	}

	public static void register(IEventBus modEventBus) {
		DEFERRED_REGISTER.register(modEventBus);
	}

	public static NumberProvider championLevel(LevelBasedValue amount) {
		return new ChampionLevelProvider(amount);
	}

	public static <T extends NumberProvider> Supplier<MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
		return DEFERRED_REGISTER.register(name, () -> mapCodec);
	}
}
