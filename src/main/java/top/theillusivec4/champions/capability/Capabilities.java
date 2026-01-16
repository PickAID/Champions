package top.theillusivec4.champions.capability;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.champion.entity.ChampionHandlerEntity;
import top.theillusivec4.champions.champion.entity.CommonChampionEntity;
import top.theillusivec4.champions.champion.item.ChampionHandlerItem;
import top.theillusivec4.champions.champion.item.CommonChampionItem;
import top.theillusivec4.champions.util.Utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Capabilities {

  public static void register(IEventBus modEventBus) {
    modEventBus.addListener(RegisterCapabilitiesEvent.class, event -> {
      // EntityType
      for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
        if (entityType.getCategory() == MobCategory.MONSTER) {
          event.registerEntity(ChampionHandlers.ENTITY, entityType, ChampionHandlers.Providers.ENTITY);
          ChampionHandlers.IMPLEMENTED_ENTITY_TYPES.add(entityType);

          Item item = BuiltInRegistries.ITEM.getValue(EntityType.getKey(entityType).withSuffix("_spawn_egg"));
          if (item != Items.AIR) {
            ChampionHandlers.IMPLEMENTED_ITEMS.add(item);
            event.registerItem(ChampionHandlers.ITEM, ChampionHandlers.Providers.ITEM, item);
          }
        }
      }

    });
  }


  private Capabilities() {
  }

  public static final class ChampionHandlers {
    public static final EntityCapability<ChampionHandlerEntity, @Nullable Void> ENTITY = EntityCapability.createVoid(Utils.id("champion_handler"), ChampionHandlerEntity.class);
    public static final ItemCapability<ChampionHandlerItem, @Nullable Void> ITEM = ItemCapability.createVoid(Utils.id("champion_handler"), ChampionHandlerItem.class);

    private static final Set<EntityType<?>> IMPLEMENTED_ENTITY_TYPES = new HashSet<>();
    private static final Set<Item> IMPLEMENTED_ITEMS = new HashSet<>();
    private static final Set<EntityType<?>> IMPLEMENTED_ENTITY_TYPES_VIEW = Collections.unmodifiableSet(IMPLEMENTED_ENTITY_TYPES);
    private static final Set<Item> IMPLEMENTED_ITEMS_VIEW = Collections.unmodifiableSet(IMPLEMENTED_ITEMS);

    public static Set<Item> getImplementedItems() {
      return IMPLEMENTED_ITEMS_VIEW;
    }

    public static Set<EntityType<?>> getImplementedEntityTypes() {
      return IMPLEMENTED_ENTITY_TYPES_VIEW;
    }

    public static boolean isImplemented(EntityType<?> entityType) {
      return IMPLEMENTED_ENTITY_TYPES.contains(entityType);
    }

    public static boolean isImplemented(Item item) {
      return IMPLEMENTED_ITEMS.contains(item);
    }

    private ChampionHandlers() {
    }

    public static final class Providers {
      public static final ICapabilityProvider<Entity, @Nullable Void, ChampionHandlerEntity> ENTITY = (entity, _) -> new CommonChampionEntity(entity);
      public static final ICapabilityProvider<ItemStack, @Nullable Void, ChampionHandlerItem> ITEM = (itemStack, _) -> new CommonChampionItem(itemStack);

      private Providers() {
      }
    }
  }
}
