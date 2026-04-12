package top.theillusivec4.champions;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import top.theillusivec4.champions.client.config.ChampionsClientConfig;
import top.theillusivec4.champions.client.gui.ChampionsGui;
import top.theillusivec4.champions.client.gui.ChampionsGuiLayers;
import top.theillusivec4.champions.client.particle.RankParticle;
import top.theillusivec4.champions.particles.ChampionsParticleTypes;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.world.item.ChampionsCreativeModeTabs;

@Mod(value = ChampionsMod.MOD_ID, dist = Dist.CLIENT)
public class ChampionsModClient {
  private static ChampionsModClient instance = null;
  private final ChampionsGui gui = new ChampionsGui();

  public ChampionsModClient(IEventBus modEventBus, ModContainer modContainer) {
    instance = this;
    modEventBus.register(this);
    modContainer.registerConfig(ModConfig.Type.CLIENT, ChampionsClientConfig.SPEC);
    modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
  }

  public static ChampionsModClient getInstance() {
    return instance;
  }

  public ChampionsGui getGui() {
    return gui;
  }

  @SubscribeEvent
  public void registerGuiLayers(RegisterGuiLayersEvent event) {
    event.registerBelow(
      VanillaGuiLayers.BOSS_OVERLAY,
      ChampionsGuiLayers.HEALTH_OVERLAY,
      this.gui::renderHealthOverlay
    );
  }

  @SubscribeEvent
  public void registerParticleProviders(RegisterParticleProvidersEvent event) {
    event.registerSpriteSet(ChampionsParticleTypes.RANK.get(), RankParticle.Provider::new);
  }

  @SubscribeEvent
  public void modifyCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
    if (event.getTab() == ChampionsCreativeModeTabs.CHAMPION_EGG.get()) {
      if (Minecraft.getInstance().level != null) {
        RegistryAccess registry = Minecraft.getInstance().level.registryAccess();
        registry.lookupOrThrow(ChampionsRegistries.CHAMPION_EGG).listElements()
          .forEach(template -> event.accept(template.value().create()));
      }
    }
  }
}
