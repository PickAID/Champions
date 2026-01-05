package top.theillusivec4.champions.common.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

import java.util.function.Supplier;

public final class LootItemConditionTypes {
  private static final DeferredRegister<MapCodec<? extends LootItemCondition>> DEFERRED_REGISTER = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Champions.MODID);
  public static final DeferredHolder<MapCodec<? extends LootItemCondition>, MapCodec<LatestDamageCondition>> DAMAGE_SOURCE = register("latest_damage", LatestDamageCondition.MAP_CODEC);

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T extends LootItemCondition> DeferredHolder<MapCodec<? extends LootItemCondition>, MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
    return register(name, () -> mapCodec);
  }

  private static <T extends LootItemCondition> DeferredHolder<MapCodec<? extends LootItemCondition>, MapCodec<T>> register(String name, Supplier<MapCodec<T>> mapCodecSupplier) {
    return DEFERRED_REGISTER.register(name, mapCodecSupplier);
  }

  private LootItemConditionTypes() {
  }
}
