package top.theillusivec4.champions.deprecated.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import top.theillusivec4.champions.client.renderer.ColorizedBulletRenderer;
import top.theillusivec4.champions.client.particle.RankParticle;
import top.theillusivec4.champions.world.entity.EntityTypes;
import top.theillusivec4.champions.particle.ParticleTypes;
import top.theillusivec4.champions.util.Util;

@Deprecated
//@EventBusSubscriber(value = Dist.CLIENT, modid = Champions.MODID)
public class ClientModEventHandler {

  @SubscribeEvent
  public static void registerGuiOverlayEvent(final RegisterGuiLayersEvent evt) {
    evt.registerBelow(VanillaGuiLayers.BOSS_OVERLAY, Util.id("health_overlay"), new ChampionsOverlay());
  }

  @SubscribeEvent
  public static void onRegisterParticleProviders(RegisterParticleProvidersEvent evt) {
    evt.registerSpriteSet(ParticleTypes.RANK_PARTICLE_TYPE.get(), RankParticle.Provider::new);
  }

  @SubscribeEvent
  public static void rendererRegistering(final EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(EntityTypes.ARCTIC_BULLET.get(),
      (renderManager) -> new ColorizedBulletRenderer(renderManager, 0x42F5E3));
//    event.registerEntityRenderer(EntityTypes.ENKINDLING_BULLET.get(),
//      (renderManager) -> new ColorizedBulletRenderer<>(renderManager, 0xFC5A03));
  }

}
