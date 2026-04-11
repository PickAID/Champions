package top.theillusivec4.champions.world.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;

import java.util.function.Supplier;

public final class ChampionsLootItemConditions {
  private static final DeferredRegister<LootItemConditionType> DEFERRED_REGISTER = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, ChampionsMod.MOD_ID);
  public static final Supplier<LootItemConditionType> DAMAGE_COUNT = register("damage_count", DamageCountCondition.MAP_CODEC);

  private ChampionsLootItemConditions() {
  }

  private static Supplier<LootItemConditionType> register(String name, MapCodec<? extends LootItemCondition> mapCodec) {
    return DEFERRED_REGISTER.register(name, () -> new LootItemConditionType(mapCodec));
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
