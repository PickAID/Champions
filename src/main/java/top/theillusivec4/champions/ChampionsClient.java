package top.theillusivec4.champions;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import top.theillusivec4.champions.client.ChampionsClientConfig;
import top.theillusivec4.champions.client.gui.ChampionsGui;
import top.theillusivec4.champions.client.gui.ChampionsGuiLayers;
import top.theillusivec4.champions.client.particle.RankParticle;
import top.theillusivec4.champions.client.renderer.ColorizedBulletRenderer;
import top.theillusivec4.champions.core.particles.ChampionsParticleTypes;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.world.entity.ChampionsEntityTypes;
import top.theillusivec4.champions.world.item.ChampionsCreativeModeTabs;

@Mod(value = Champions.MOD_ID, dist = Dist.CLIENT)
public class ChampionsClient {
  private static ChampionsClient instance = null;
  private final ChampionsGui gui = new ChampionsGui();

  public ChampionsClient(IEventBus bus, ModContainer container) {
    instance = this;
    bus.register(this);
    container.registerConfig(ModConfig.Type.CLIENT, ChampionsClientConfig.SPEC);
    container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
  }

  public static ChampionsClient getInstance() {
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
        registry.lookupOrThrow(ChampionsRegistries.CHAMPION_MOB_EGG).listElements()
          .forEach(template -> event.accept(template.value().create()));
      }
    }
  }

  @SubscribeEvent
  public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(
      ChampionsEntityTypes.ARCTIC_BULLET.get(),
      (renderManager) -> new ColorizedBulletRenderer(renderManager, 0x42F5E3)
    );
    event.registerEntityRenderer(
      ChampionsEntityTypes.ENKINDLING_BULLET.get(),
      (renderManager) -> new ColorizedBulletRenderer(renderManager, 0xFC5A03)
    );
  }

}
