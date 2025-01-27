package top.theillusivec4.champions.client.affix;

import net.minecraft.client.player.Input;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.registry.ModMobEffects;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = Champions.MODID)
public class ClientAffixEventsHandler {

    @SubscribeEvent
    public void handleJailing(MovementInputUpdateEvent evt) {
        if (evt.getEntity().hasEffect(ModMobEffects.PARALYSIS_EFFECT_TYPE.get())) {
            Input input = evt.getInput();
            input.shiftKeyDown = false;
            input.jumping = false;
            input.forwardImpulse = 0;
            input.leftImpulse = 0;
        }
    }
}
