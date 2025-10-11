package top.theillusivec4.champions.client.affix;

import net.minecraft.util.MovementInput;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.registry.ModMobEffects;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Champions.MODID)
public class ClientAffixEventsHandler {

	@SubscribeEvent
	public static void handleJailing(InputUpdateEvent evt) {
		if (evt.getEntityLiving().hasEffect(ModMobEffects.PARALYSIS_EFFECT_TYPE.get())) {
			MovementInput input = evt.getMovementInput();
			input.shiftKeyDown = false;
			input.jumping = false;
			input.forwardImpulse = 0;
			input.leftImpulse = 0;
		}
	}
}
