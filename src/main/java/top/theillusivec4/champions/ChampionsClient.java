package top.theillusivec4.champions;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.client.config.ClientConfig;
import top.theillusivec4.champions.client.event.ClientModEventListener;
import top.theillusivec4.champions.client.gui.Gui;
import top.theillusivec4.champions.client.item.CreativeModeTabEventListener;

import java.util.Objects;

@Mod(value = Champions.MODID, dist = Dist.CLIENT)
public class ChampionsClient {
  private static final Logger LOGGER = LogManager.getLogger();
  private static ChampionsClient instance;
  private final Gui gui = new Gui();
  private final ClientConfig clientConfig;

  public static ChampionsClient getInstance() {
    return Objects.requireNonNull(instance, "过早的访问 ChampionsClient");
  }

  public ChampionsClient(IEventBus modEventBus, @SuppressWarnings("unused") ModContainer modContainer) {
    instance = this;
    this.clientConfig = new ClientConfig();
    modContainer.registerConfig(ModConfig.Type.CLIENT, this.clientConfig.getConfigSpec());
    modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    CreativeModeTabEventListener.register(modEventBus);
    ClientModEventListener.register(modEventBus);
  }

  public boolean displayHealthOverlay() {
    return this.clientConfig.displayHealthOverlay();
  }

  public ClientConfig getClientConfig() {
    return clientConfig;
  }

  public Gui getGui() {
    return gui;
  }
}
