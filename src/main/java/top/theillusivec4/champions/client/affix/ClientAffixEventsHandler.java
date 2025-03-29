package top.theillusivec4.champions.client.affix;

import net.minecraft.client.player.ClientInput;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.registry.ModMobEffects;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME, modid = Champions.MODID)
public class ClientAffixEventsHandler {

  @SubscribeEvent
  public static void handleJailing(MovementInputUpdateEvent event) {
    var player = event.getEntity();
    if (player.hasEffect(ModMobEffects.PARALYSIS_EFFECT_TYPE)) {
      ClientInput input = event.getInput();
      event.getInput().moveVector = Vec2.ZERO;
      input.keyPresses = Input.EMPTY;
    }
  }
}
