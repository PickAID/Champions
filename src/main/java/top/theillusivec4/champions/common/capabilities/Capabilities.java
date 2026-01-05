package top.theillusivec4.champions.common.capabilities;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.api.EntityChampionHandler;
import top.theillusivec4.champions.api.IChampionHandler;
import top.theillusivec4.champions.common.util.Utils;

public final class Capabilities {

  public static void register(IEventBus modEventBus) {
    modEventBus.addListener(RegisterCapabilitiesEvent.class, event -> {
      for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
        if (entityType.getCategory() == MobCategory.MONSTER) {
          event.registerEntity(ChampionHandler.ENTITY, entityType, ChampionHandler.Providers.ENTITY);
        }
      }
    });
  }

  private Capabilities() {
  }

  public static final class ChampionHandler {
    public static final EntityCapability<IChampionHandler, @Nullable Void> ENTITY = EntityCapability.create(Utils.id("champion_handler"), IChampionHandler.class, Void.class);

    private ChampionHandler() {
    }

    public static final class Providers {
      public static final ICapabilityProvider<Entity, Void, IChampionHandler> ENTITY = (entity, context) -> new EntityChampionHandler(entity);

      private Providers() {
      }
    }
  }
}
