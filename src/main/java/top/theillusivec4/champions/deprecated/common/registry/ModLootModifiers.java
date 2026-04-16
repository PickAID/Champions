package top.theillusivec4.champions.deprecated.common.registry;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.common.loot.ChampionLootModifier;

public class ModLootModifiers {

  public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
    DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Champions.MOD_ID);
  public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ChampionLootModifier>> CHAMPION_LOOT_MODIFIER =
    LOOT_MODIFIER_SERIALIZERS.register("champion_loot_modifier", () -> ChampionLootModifier.CODEC);

  public static void register(IEventBus bus) {
    LOOT_MODIFIER_SERIALIZERS.register(bus);
  }

}
