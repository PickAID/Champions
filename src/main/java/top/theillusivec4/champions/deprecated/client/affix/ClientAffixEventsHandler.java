package top.theillusivec4.champions.deprecated.client.affix;

import net.minecraft.client.player.Input;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import top.theillusivec4.champions.deprecated.common.registry.ModMobEffects;

public class ClientAffixEventsHandler {

  @SubscribeEvent
  public static void handleJailing(MovementInputUpdateEvent evt) {
    if (evt.getEntity().hasEffect(ModMobEffects.PARALYSIS_EFFECT_TYPE)) {
      Input input = evt.getInput();
      input.shiftKeyDown = false;
      input.jumping = false;
      input.forwardImpulse = 0;
      input.leftImpulse = 0;
    }
  }
}
