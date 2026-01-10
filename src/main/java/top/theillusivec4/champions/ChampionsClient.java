package top.theillusivec4.champions;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.client.event.ClientModEventListener;
import top.theillusivec4.champions.client.gui.Gui;
import top.theillusivec4.champions.client.item.CreativeModeTabEventListener;
import top.theillusivec4.champions.client.network.ClientGamePacketListener;

import java.util.Objects;

@Mod(value = Champions.MODID, dist = Dist.CLIENT)
public class ChampionsClient {
  public static final Logger LOGGER = LogManager.getLogger();
  private static ChampionsClient instance;
  private final Gui gui = new Gui();

  public static ChampionsClient getInstance() {
    return Objects.requireNonNull(instance, "过早的访问 ChampionsClient");
  }

  public ChampionsClient(IEventBus modEventBus, @SuppressWarnings("unused") ModContainer modContainer) {
    instance = this;
    CreativeModeTabEventListener.register(modEventBus);
    ClientModEventListener.register(modEventBus);
    ClientGamePacketListener.register(modEventBus);
  }

  public Gui getGui() {
    return gui;
  }
}
