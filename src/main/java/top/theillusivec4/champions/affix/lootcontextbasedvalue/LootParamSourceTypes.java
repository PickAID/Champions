package top.theillusivec4.champions.affix.lootcontextbasedvalue;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.affix.LatestDamage;
import top.theillusivec4.champions.loot.parameters.LootContextParams;
import top.theillusivec4.champions.registries.Registries;

import java.util.function.Supplier;

public final class LootParamSourceTypes {

  public static void register(IEventBus modEventBus) {
    Floats.register(modEventBus);
  }

  private LootParamSourceTypes() {
  }

  public static final class Floats {
    private static final DeferredRegister<FloatLootParamSource<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.FLOAT_LOOT_PARAM_SOURCE, Champions.MODID);
    //  public static final DeferredHolder<FloatLootParamSource<?>, FloatLootParamSource<DamageSource>> DAMAGE_AMOUNT = register("damage_amount", new FloatLootParamSource<>(net.minecraft.world.level.storage.loot.parameters.LootContextParams.DAMAGE_SOURCE, damageSource -> damageSource.));
    public static final DeferredHolder<FloatLootParamSource<?>, FloatLootParamSource<LatestDamage>> LATEST_DAMAGE_COUNT = register("latest_damage_count", new FloatLootParamSource<>(LootContextParams.LATEST_DAMAGE, latestDamage -> (float) latestDamage.damageCount(), 0.0f));
    public static final DeferredHolder<FloatLootParamSource<?>, FloatLootParamSource<LatestDamage>> LATEST_ORIGINAL_DAMAGE_AMOUNT = register("latest_original_damage_amount", new FloatLootParamSource<>(LootContextParams.LATEST_DAMAGE, LatestDamage::originalDamageAmount, 0.0f));

    private static <T> DeferredHolder<FloatLootParamSource<?>, FloatLootParamSource<T>> register(String name, FloatLootParamSource<T> floatLootParamSource) {
      return register(name, () -> floatLootParamSource);
    }

    private static <T> DeferredHolder<FloatLootParamSource<?>, FloatLootParamSource<T>> register(String name, Supplier<FloatLootParamSource<T>> lootParamFloatSourceSupplier) {
      return DEFERRED_REGISTER.register(name, lootParamFloatSourceSupplier);
    }

    private static void register(IEventBus modEventBus) {
      DEFERRED_REGISTER.register(modEventBus);
    }

    private Floats() {
    }
  }
}
