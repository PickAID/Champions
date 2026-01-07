package top.theillusivec4.champions;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.client.gui.Gui;
import top.theillusivec4.champions.client.gui.GuiLayers;
import top.theillusivec4.champions.client.gui.components.BossHealthOverlayHandler;
import top.theillusivec4.champions.client.gui.components.ChampionClientBossEvent;
import top.theillusivec4.champions.client.particles.RankParticle;
import top.theillusivec4.champions.deprecated.client.renderer.ColorizedBulletRenderer;
import top.theillusivec4.champions.entities.EntityTypes;
import top.theillusivec4.champions.particles.ParticleTypes;

@Mod(value = Champions.MODID, dist = Dist.CLIENT)
public class ChampionsClient {
  public static final Logger LOGGER = LogManager.getLogger();

  public ChampionsClient(IEventBus modEventBus, ModContainer modContainer) {
    ClientModEventListener.register(modEventBus);
    ClientEventListener.register();
  }

  private static final class ClientModEventListener {
    private final Gui gui = new Gui();

    private static void register(IEventBus modEventBus) {
      modEventBus.register(new ClientModEventListener());
    }

    private ClientModEventListener() {
    }

    @SubscribeEvent
    public void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
      event.registerBelow(VanillaGuiLayers.BOSS_OVERLAY, GuiLayers.CHAMPION_HEALTH, this.gui::renderChampionHealthOverlay);
    }

    @SubscribeEvent
    public void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
      event.registerSpriteSet(ParticleTypes.RANK_PARTICLE_TYPE.get(), RankParticle.RankFactory::new);
    }

    @SubscribeEvent
    public void onEntityRenderersRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
      event.registerEntityRenderer(EntityTypes.ARCTIC_BULLET.get(), context -> new ColorizedBulletRenderer<>(context, 0x42F5E3));
      event.registerEntityRenderer(EntityTypes.ENKINDLING_BULLET.get(), context -> new ColorizedBulletRenderer<>(context, 0xFC5A03));
    }
  }


  private static final class ClientEventListener {
    private final BossHealthOverlayHandler overlayHandler = new BossHealthOverlayHandler();

    private static void register() {
      NeoForge.EVENT_BUS.register(new ClientEventListener());
    }

    private ClientEventListener() {
    }

    @SubscribeEvent
    public void onCustomizeGuiOverlayBossProgress(CustomizeGuiOverlayEvent.BossEventProgress event) {
      if (event.getBossEvent() instanceof ChampionClientBossEvent championClientBossEvent) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        int x = event.getX();
        int y = event.getY();
        overlayHandler.render(guiGraphics, x, y, championClientBossEvent);
        event.setIncrement(event.getIncrement() + 6);
        event.setCanceled(true);
      }
    }

//    @SubscribeEvent
//    public void onRenderNameTagCanRender(RenderNameTagEvent.CanRender event) {
//      ChampionUtil.getHandler(event.getEntity()).ifPresent(handler -> {
//        if (handler.getLevel() >= 1) {
//          event.setCanRender(TriState.TRUE);
//        }
//      });
//    }
//
//    @SubscribeEvent
//    public void onRenderNameTagDoRender(RenderNameTagEvent.DoRender event) {
//      PoseStack poseStack = event.getPoseStack();
//
//    }

  }
}
