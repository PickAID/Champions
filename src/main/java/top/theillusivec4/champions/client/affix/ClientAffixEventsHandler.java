package top.theillusivec4.champions.client.affix;

import net.minecraft.client.player.ClientInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.effect.MobEffects;

@Deprecated
@EventBusSubscriber(value = Dist.CLIENT, modid = Champions.MODID)
public class ClientAffixEventsHandler {

  @SubscribeEvent
  public static void handleJailing(MovementInputUpdateEvent event) {
    var player = event.getEntity();
    if (player.hasEffect(MobEffects.PARALYSIS_EFFECT_TYPE)) {
      ClientInput input = event.getInput();
      input.moveVector = Vec2.ZERO;
      input.keyPresses = Input.EMPTY;
    }
  }
}
