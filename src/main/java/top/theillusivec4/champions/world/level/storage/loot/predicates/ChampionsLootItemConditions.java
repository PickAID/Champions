package top.theillusivec4.champions.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

import java.util.function.Supplier;

public final class ChampionsLootItemConditions {
  private static final DeferredRegister<MapCodec<? extends LootItemCondition>> DEFERRED_REGISTER = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Champions.MOD_ID);

	public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T extends LootItemCondition> Supplier<MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
    return register(name, () -> mapCodec);
  }

  private static <T extends LootItemCondition> Supplier<MapCodec<T>> register(String name, Supplier<MapCodec<T>> mapCodecSupplier) {
    return DEFERRED_REGISTER.register(name, mapCodecSupplier);
  }

  private ChampionsLootItemConditions() {
  }
}
