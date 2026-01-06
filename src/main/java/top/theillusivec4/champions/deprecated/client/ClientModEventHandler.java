package top.theillusivec4.champions.deprecated.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.client.renderer.ColorizedBulletRenderer;
import top.theillusivec4.champions.deprecated.common.particle.RankParticle;
import top.theillusivec4.champions.entities.EntityTypes;
import top.theillusivec4.champions.deprecated.common.particles.ParticleTypes;
import top.theillusivec4.champions.util.Utils;

@Deprecated
@EventBusSubscriber(value = Dist.CLIENT, modid = Champions.MODID)
public class ClientModEventHandler {

  @SubscribeEvent
  public static void registerGuiOverlayEvent(final RegisterGuiLayersEvent evt) {
    evt.registerBelow(VanillaGuiLayers.BOSS_OVERLAY, Utils.id("health_overlay"), new ChampionsOverlay());
  }

  @SubscribeEvent
  public static void onRegisterParticleProviders(RegisterParticleProvidersEvent evt) {
    evt.registerSpriteSet(ParticleTypes.RANK_PARTICLE_TYPE.get(), RankParticle.RankFactory::new);
  }

  @SubscribeEvent
  public static void rendererRegistering(final EntityRenderersEvent.RegisterRenderers evt) {
    evt.registerEntityRenderer(EntityTypes.ARCTIC_BULLET.get(),
      (renderManager) -> new ColorizedBulletRenderer<>(renderManager, 0x42F5E3));
    evt.registerEntityRenderer(EntityTypes.ENKINDLING_BULLET.get(),
      (renderManager) -> new ColorizedBulletRenderer<>(renderManager, 0xFC5A03));
  }

}
