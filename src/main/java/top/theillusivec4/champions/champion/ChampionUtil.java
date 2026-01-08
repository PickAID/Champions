package top.theillusivec4.champions.champion;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.champions.capabilities.Capabilities;

import java.util.Optional;

public final class ChampionUtil {
  public static Optional<ChampionHandler> getHandler(Entity entity) {
    return Optional.ofNullable(entity.getCapability(Capabilities.ChampionHandlers.ENTITY));
  }

  public static Optional<ChampionHandler> getHandler(ItemStack itemStack, Level level) {
    return Optional.ofNullable(itemStack.getCapability(Capabilities.ChampionHandlers.ITEM, level));
  }

  private ChampionUtil() {
  }
}
