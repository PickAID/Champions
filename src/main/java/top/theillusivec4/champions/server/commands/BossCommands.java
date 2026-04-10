package top.theillusivec4.champions.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.champion.ChampionHelper;

import java.util.Collection;

public final class BossCommands {
  private static final SimpleCommandExceptionType NO_ENTITIES = new SimpleCommandExceptionType(Component.translatable("commands.champions.boss.failed.no_entities"));

  private BossCommands() {
  }

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
    dispatcher.register(Commands.literal(ChampionsMod.MOD_ID)
      .then(Commands.literal("boss")
        .then(Commands.literal("set")
          .then(Commands.argument("entities", EntityArgument.entities())
            .executes(context -> set(context.getSource(), EntityArgument.getEntities(context, "entities")))
          )
        )
        .then(Commands.literal("color")
          .then(Commands.argument("entities", EntityArgument.entities())
            .then(Commands.argument("color", ColorArgument.color())
              .executes(context -> color(context.getSource(), EntityArgument.getEntities(context, "entities"), ColorArgument.getColor(context, "color")))
            )
          )
        )
      )
    );
  }

  private static int set(CommandSourceStack source, Collection<? extends Entity> entities) throws CommandSyntaxException {
    int i = 0;
    for (Entity entity : entities) {
      ChampionHelper.setBoss(entity);
      i++;
    }

    if (i == 1) {
      source.sendSuccess(() -> Component.translatable(
        "commands.champions.boss.set.single",
        entities.stream().findFirst().orElseThrow().getDisplayName()
      ), false);
    } else if (i > 1) {
      source.sendSuccess(() -> Component.translatable(
        "commands.champions.boss.set.multiple",
        entities.size()
      ), false);
    } else {
      throw NO_ENTITIES.create();
    }

    return i;
  }

  private static int color(CommandSourceStack source, Collection<? extends Entity> entities, ChatFormatting color) throws CommandSyntaxException {
    int i = 0;
    TextColor color1 = TextColor.fromLegacyFormat(color);
    if (color1 != null) {
      for (Entity entity : entities) {
        ChampionHelper.setNameColor(entity, color1);
        i++;
      }
    }

    if (i == 1) {
      source.sendSuccess(() -> Component.translatable(
        "commands.champions.boss.color.single",
        entities.stream().findFirst().orElseThrow().getDisplayName()
      ), false);
    } else if (i > 1) {
      source.sendSuccess(() -> Component.translatable(
        "commands.champions.boss.color.multiple",
        entities.size()
      ), false);
    } else {
      throw NO_ENTITIES.create();
    }

    return i;
  }
}
