package top.theillusivec4.champions.server.commands;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class ChampionsCommands {
  private ChampionsCommands() {
  }

  public static void register() {
    NeoForge.EVENT_BUS.addListener(ChampionsCommands::registerCommands);
  }

  private static void registerCommands(RegisterCommandsEvent event) {

  }

}
