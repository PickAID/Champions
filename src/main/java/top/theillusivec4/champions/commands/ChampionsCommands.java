package top.theillusivec4.champions.commands;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.server.commands.AffixCommands;
import top.theillusivec4.champions.server.commands.BossCommands;

@EventBusSubscriber(modid = Champions.MOD_ID)
public final class ChampionsCommands {
  private ChampionsCommands() {
  }

  @SubscribeEvent
  private static void registerCommands(RegisterCommandsEvent event) {
    var dispatcher = event.getDispatcher();
    var buildContext = event.getBuildContext();
    AffixCommands.register(dispatcher, buildContext);
    BossCommands.register(dispatcher, buildContext);
  }

}
