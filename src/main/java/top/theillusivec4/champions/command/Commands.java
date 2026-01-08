package top.theillusivec4.champions.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import top.theillusivec4.champions.Champions;

public final class Commands {
  public static void register() {
    NeoForge.EVENT_BUS.addListener(RegisterCommandsEvent.class, event -> {
      CommandBuildContext buildContext = event.getBuildContext();
      CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
      LiteralArgumentBuilder<CommandSourceStack> builder = net.minecraft.commands.Commands.literal(Champions.MODID).requires(net.minecraft.commands.Commands.hasPermission(net.minecraft.commands.Commands.LEVEL_ADMINS));
      SpawnEggCommand.register(builder, buildContext);

      dispatcher.register(builder);
//      SpawnEggCommand.register(dispatcher, buildContext);
    });
  }

  private Commands() {
  }
}
