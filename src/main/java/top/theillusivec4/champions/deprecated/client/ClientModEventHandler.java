package top.theillusivec4.champions.deprecated.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.client.renderer.ColorizedBulletRenderer;
import top.theillusivec4.champions.deprecated.common.item.ChampionEggItem;
import top.theillusivec4.champions.deprecated.common.particle.RankParticle;
import top.theillusivec4.champions.deprecated.common.registry.ModEntityTypes;
import top.theillusivec4.champions.deprecated.common.registry.ModItems;
import top.theillusivec4.champions.deprecated.common.registry.ModParticleTypes;

public class ClientModEventHandler {

  @SubscribeEvent
  public static void onRegisterColor(final RegisterColorHandlersEvent.Item event) {
    event.register(ChampionEggItem::getColor, ModItems.CHAMPION_EGG_ITEM.get());
  }

  @SubscribeEvent
  public static void registerGuiOverlayEvent(final RegisterGuiLayersEvent evt) {
    evt.registerBelow(VanillaGuiLayers.BOSS_OVERLAY, Champions.getLocation("health_overlay"), new ChampionsOverlay());
  }

  @SubscribeEvent
  public static void onRegisterParticleProviders(RegisterParticleProvidersEvent evt) {
    evt.registerSpriteSet(ModParticleTypes.RANK_PARTICLE_TYPE.get(), RankParticle.RankFactory::new);
  }

  @SubscribeEvent
  public static void rendererRegistering(final EntityRenderersEvent.RegisterRenderers evt) {
    evt.registerEntityRenderer(ModEntityTypes.ARCTIC_BULLET.get(),
      (renderManager) -> new ColorizedBulletRenderer(renderManager, 0x42F5E3));
    evt.registerEntityRenderer(ModEntityTypes.ENKINDLING_BULLET.get(),
      (renderManager) -> new ColorizedBulletRenderer(renderManager, 0xFC5A03));
  }

}
