package top.theillusivec4.champions.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class Commands {
  public static void register() {
    NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, event -> {
      CommandBuildContext builder = event.getBuildContext();
      CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
      SpawnEggCommand.register(dispatcher, builder);
    });
  }

  private Commands() {
  }
}
