package top.theillusivec4.champions.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.item.ChampionEggItem;
import top.theillusivec4.champions.common.particle.RankParticle;
import top.theillusivec4.champions.common.registry.ModItems;
import top.theillusivec4.champions.common.registry.ModParticleTypes;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = Champions.MODID)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onRegisterColor(final RegisterColorHandlersEvent.Item event) {
        event.register(ChampionEggItem::getColor, ModItems.CHAMPION_EGG_ITEM.get());
    }

    @SubscribeEvent
    public static void registerGuiOverlayEvent(final RegisterGuiOverlaysEvent evt) {
        evt.registerBelow(VanillaGuiOverlay.BOSS_EVENT_PROGRESS.id(), Champions.MODID + "_health_overlay", new ChampionsOverlay());
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent evt) {
        evt.register(ModParticleTypes.RANK_PARTICLE_TYPE.get(), RankParticle.RankFactory::new);
    }

}
