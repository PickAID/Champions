package top.theillusivec4.champions.util;

import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.affix.ChampionHandler;
import top.theillusivec4.champions.capabilities.Capabilities;

import java.util.Optional;

public final class ChampionUtil {
  public static Optional<ChampionHandler> getHandler(Entity entity) {
    return Optional.ofNullable(entity.getCapability(Capabilities.ChampionHandlers.ENTITY));
  }

  private ChampionUtil() {
  }
}
