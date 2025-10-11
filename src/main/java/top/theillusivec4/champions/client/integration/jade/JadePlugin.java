package top.theillusivec4.champions.client.integration.jade;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
	public static final ResourceLocation STAR_RENDERER = StarElement.getTexture();
	@Override
	public void register(IRegistrar registration) {
		// register renderer
		registration.registerTooltipRenderer(STAR_RENDERER, new StarElement());
		// register data provider
		// change name
		registration.registerComponentProvider(ChampionComponentProvider.INSTANCE, TooltipPosition.HEAD,  LivingEntity.class);
		// add icon and affix
		registration.registerComponentProvider(ChampionComponentProvider.INSTANCE, TooltipPosition.BODY,  LivingEntity.class);
	}
}
