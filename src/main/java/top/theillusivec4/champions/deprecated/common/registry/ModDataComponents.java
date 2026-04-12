package top.theillusivec4.champions.deprecated.common.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;

public class ModDataComponents {

  private static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ChampionsMod.MOD_ID);

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> ENTITY_TAG_COMPONENT = COMPONENTS.register("entity_tag", () -> DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC).build());

  public static void register(IEventBus bus) {
    COMPONENTS.register(bus);
  }
}
