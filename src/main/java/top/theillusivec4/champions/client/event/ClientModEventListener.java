package top.theillusivec4.champions.client.event;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import top.theillusivec4.champions.ChampionsClient;
import top.theillusivec4.champions.client.gui.GuiLayers;
import top.theillusivec4.champions.client.particle.RankParticle;
import top.theillusivec4.champions.client.renderer.ColorizedBulletRenderer;
import top.theillusivec4.champions.particle.ParticleTypes;
import top.theillusivec4.champions.world.entity.EntityTypes;

public final class ClientModEventListener {

  public static void register(IEventBus modEventBus) {
    modEventBus.register(new ClientModEventListener());
  }

  private ClientModEventListener() {
  }

  @SubscribeEvent
  private void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
    event.registerBelow(
      VanillaGuiLayers.BOSS_OVERLAY,
      GuiLayers.CHAMPION_OVERLAY,
      ChampionsClient.getInstance().getGui()::renderChampionHealthOverlay
    );
  }

  @SubscribeEvent
  private void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
    event.registerSpriteSet(ParticleTypes.RANK_PARTICLE_TYPE.get(), RankParticle.Provider::new);
  }

  @SubscribeEvent
  private void onEntityRenderersRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(EntityTypes.ARCTIC_BULLET.get(), context -> new ColorizedBulletRenderer(context, 0x42F5E3));
    event.registerEntityRenderer(EntityTypes.ENKINDLING_BULLET.get(), context -> new ColorizedBulletRenderer(context, 0xFC5A03));
  }

}
