package top.theillusivec4.champions.champion;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.champions.capability.Capabilities;
import top.theillusivec4.champions.champion.entity.ChampionHandlerEntity;
import top.theillusivec4.champions.champion.item.ChampionHandlerItem;

import java.util.Optional;

public final class ChampionUtil {
  public static Optional<ChampionHandlerEntity> getHandler(Entity entity) {
    return Optional.ofNullable(entity.getCapability(Capabilities.ChampionHandlers.ENTITY));
  }

  public static Optional<ChampionHandlerItem> getHandler(ItemStack itemStack, Level level) {
    return Optional.ofNullable(itemStack.getCapability(Capabilities.ChampionHandlers.ITEM, level));
  }

  private ChampionUtil() {
  }
}
