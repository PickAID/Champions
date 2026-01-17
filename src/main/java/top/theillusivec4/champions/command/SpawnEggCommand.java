package top.theillusivec4.champions.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.data.lang.LanguageKeys;
import top.theillusivec4.champions.data.lang.LanguageUtil;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.champion.ChampionDefaultConfigs;

import java.util.Collection;

public final class SpawnEggCommand {

  public static void register(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext buildContext) {
    builder
      .then(Commands.literal("affix")
        .then(Commands.argument("players", EntityArgument.players())
          .then(Commands.argument("affix", ResourceArgument.resource(buildContext, Registries.AFFIX))
            .executes(context -> affix(context.getSource(), EntityArgument.getPlayers(context, "players"), ResourceArgument.getResource(context, "affix", Registries.AFFIX)))
          )
        )
      )
      .then(Commands.literal("level")
        .then(Commands.argument("players", EntityArgument.players())
          .then(Commands.argument("level", IntegerArgumentType.integer(ChampionDefaultConfigs.MIN_LEVEL, ChampionDefaultConfigs.MAX_LEVEL))
            .executes(context -> level(context.getSource(), EntityArgument.getPlayers(context, "players"), IntegerArgumentType.getInteger(context, "level")))
          )
        )
      )
      .then(Commands.literal("rank")
        .then(Commands.argument("players", EntityArgument.players())
          .then(Commands.argument("rank", ResourceArgument.resource(buildContext, Registries.RANK))
            .executes(context -> rank(context.getSource(), EntityArgument.getPlayers(context, "players"), ResourceArgument.getResource(context, "rank", Registries.RANK)))
          )
        )
      )
      .then(Commands.literal("boss")
        .then(Commands.argument("players", EntityArgument.players())
          .then(Commands.argument("boss", BoolArgumentType.bool())
            .executes(context -> boss(context.getSource(), EntityArgument.getPlayers(context, "players"), BoolArgumentType.getBool(context, "boss")))
          )
        )
      )
      .then(Commands.literal("color")
        .then(Commands.argument("players", EntityArgument.players())
          .then(Commands.argument("color", IntegerArgumentType.integer())
            .executes(context -> color(context.getSource(), EntityArgument.getPlayers(context, "players"), IntegerArgumentType.getInteger(context, "color")))
          )
        )
      )
    ;
  }

  private static int rank(CommandSourceStack source, Collection<ServerPlayer> players, Holder.Reference<Rank> rank) {
    int i = 0;
    for (ServerPlayer player : players) {
      ItemStack itemStack = player.getMainHandItem();
    }
    source.sendSuccess(() -> Component.translatable(LanguageKeys.COMMANDS_RANK_SUCCESS_KEY, rank.value().description()), true);
    return i;
  }

  private static int affix(CommandSourceStack source, Collection<ServerPlayer> players, Holder.Reference<Affix> affix) {
    int i = 0;
    for (ServerPlayer player : players) {
      ItemStack itemStack = player.getMainHandItem();
      ChampionUtil.getHandler(itemStack)
        .ifPresent(handler -> handler.updateAffixes(mutable -> mutable.add(affix)));
    }
    source.sendSuccess(() -> Component.translatable(LanguageKeys.COMMANDS_AFFIX_SUCCESS_KEY, affix.value().description()), true);
    return i;
  }

  private static int level(CommandSourceStack source, Collection<ServerPlayer> players, int level) {
    int i = 0;
    for (ServerPlayer player : players) {
      ItemStack itemStack = player.getMainHandItem();
      ChampionUtil.getHandler(itemStack)
        .ifPresent(handler -> handler.setLevel(level));
      i++;
    }

    source.sendSuccess(() -> Component.translatable(LanguageKeys.COMMANDS_LEVEL_SUCCESS_KEY, level), true);
    return i;
  }

  private static int boss(CommandSourceStack source, Collection<ServerPlayer> players, boolean boss) {
    int i = 0;
    for (ServerPlayer player : players) {
      ItemStack itemStack = player.getMainHandItem();
      ChampionUtil.getHandler(itemStack)
        .ifPresent(handler -> handler.setBoss(boss));
      i++;
    }

    source.sendSuccess(() -> Component.translatable(LanguageKeys.COMMANDS_BOSS_SUCCESS_KEY, boss), true);
    return i;
  }

  private static int color(CommandSourceStack source, Collection<ServerPlayer> players, int color) {
    int i = 0;
    for (ServerPlayer player : players) {
      ItemStack itemStack = player.getMainHandItem();
      ChampionUtil.getHandler(itemStack)
        .ifPresent(handler -> handler.setColor(TextColor.fromRgb(ARGB.opaque(color))));
      i++;
    }

    source.sendSuccess(() -> Component.translatable(LanguageKeys.COMMANDS_COLOR_SUCCESS_KEY, LanguageUtil.getColorComponent(color)), true);
    return i;
  }

  private SpawnEggCommand() {
  }
}
