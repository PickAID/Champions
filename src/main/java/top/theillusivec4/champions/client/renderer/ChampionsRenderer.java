package top.theillusivec4.champions.client.renderer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.registry.ModEntityTypes;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Champions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChampionsRenderer {

	@SubscribeEvent
	public static void rendererRegistering(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ARCTIC_BULLET.get(),
				(renderManager) -> new ColorizedBulletRenderer(renderManager, 0x42F5E3));
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.ENKINDLING_BULLET.get(),
				(renderManager) -> new ColorizedBulletRenderer(renderManager, 0xFC5A03));
	}
}
