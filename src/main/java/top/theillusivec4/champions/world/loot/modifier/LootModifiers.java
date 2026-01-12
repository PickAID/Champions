package top.theillusivec4.champions.world.loot.modifier;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.Champions;

public final class LootModifiers {
  private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Champions.MODID);
  public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ChampionLootModifier>> CHAMPION = DEFERRED_REGISTER.register("champion", () -> ChampionLootModifier.MAP_CODEC);

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private LootModifiers() {
  }
}
