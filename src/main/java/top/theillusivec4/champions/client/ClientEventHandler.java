package top.theillusivec4.champions.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.item.ChampionEggItem;
import top.theillusivec4.champions.common.particle.RankParticle;
import top.theillusivec4.champions.common.registry.ModItems;
import top.theillusivec4.champions.common.registry.ModParticleTypes;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = Champions.MODID)
public class ClientEventHandler {


	@SubscribeEvent
	public static void clientSetup(final FMLClientSetupEvent evt) {
		Minecraft.getInstance().getItemColors()
				.register(ChampionEggItem::getColor, ModItems.CHAMPION_EGG_ITEM.get());

		OverlayRegistry.registerOverlayTop("Champions Health Bar", new ChampionsOverlay());
	}

	@SubscribeEvent
	public static void registerParticleFactories(ParticleFactoryRegisterEvent evt) {
		Minecraft.getInstance().particleEngine.register(ModParticleTypes.RANK_PARTICLE_TYPE.get(), RankParticle.RankFactory::new);
	}

}
