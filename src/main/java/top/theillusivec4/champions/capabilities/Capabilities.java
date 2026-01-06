package top.theillusivec4.champions.capabilities;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.affix.EntityChampionHandler;
import top.theillusivec4.champions.affix.ChampionHandler;
import top.theillusivec4.champions.util.Utils;

public final class Capabilities {

  public static void register(IEventBus modEventBus) {
    modEventBus.addListener(RegisterCapabilitiesEvent.class, event -> {
      for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
        if (entityType.getCategory() == MobCategory.MONSTER) {
          event.registerEntity(ChampionHandlers.ENTITY, entityType, ChampionHandlers.Providers.ENTITY);
        }
      }
    });
  }

  private Capabilities() {
  }

  public static final class ChampionHandlers {
    public static final EntityCapability<ChampionHandler, @Nullable Void> ENTITY = EntityCapability.create(Utils.id("champion_handler"), ChampionHandler.class, Void.class);

    private ChampionHandlers() {
    }

    public static final class Providers {
      public static final ICapabilityProvider<Entity, Void, ChampionHandler> ENTITY = (entity, context) -> new EntityChampionHandler(entity);

      private Providers() {
      }
    }
  }
}
