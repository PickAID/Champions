package top.theillusivec4.champions;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = Champions.MODID, dist = Dist.CLIENT)
public class ChampionsClient {
  public static final Logger LOGGER = LogManager.getLogger();

  public ChampionsClient(IEventBus modEventBus, ModContainer modContainer) {

  }
}
