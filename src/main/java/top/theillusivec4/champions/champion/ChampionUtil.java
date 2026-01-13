package top.theillusivec4.champions.champion;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.champions.capability.Capabilities;
import top.theillusivec4.champions.champion.entity.ChampionHandlerEntity;
import top.theillusivec4.champions.champion.item.ChampionHandlerItem;

import java.util.Optional;

/**
 * 提供Capability对象获取方法，一些工具方法
 */
public final class ChampionUtil {
  public static Optional<ChampionHandlerEntity> getHandler(Entity entity) {
    return Optional.ofNullable(entity.getCapability(Capabilities.ChampionHandlers.ENTITY));
  }

  public static Optional<ChampionHandlerItem> getHandler(ItemStack itemStack) {
    return Optional.ofNullable(itemStack.getCapability(Capabilities.ChampionHandlers.ITEM));
  }

  private ChampionUtil() {
  }
}
