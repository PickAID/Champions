package top.theillusivec4.champions.client.integration.jade;

import mcp.mobius.waila.api.*;
import net.minecraft.world.entity.LivingEntity;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

	@Override
	public void register(IWailaCommonRegistration registration) {
		// register data provider
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		// register component providers, icon providers, callbacks, and config options here
		registration.registerComponentProvider(ChampionComponentProvider.INSTANCE, TooltipPosition.TAIL, LivingEntity.class);
//		registration.addBeforeRenderCallback(((iBoxElement, tooltipRect, guiGraphics, accessor, colorSetting) -> {
//			if (ChampionsOverlay.isRendering) {
//				tooltipRect.setY(ChampionsOverlay.startY + 38);
//			}
//			return false;
//		}));
	}
}
