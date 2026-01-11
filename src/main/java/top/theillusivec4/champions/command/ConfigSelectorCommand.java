package top.theillusivec4.champions.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.data.lang.LanguageKeys;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.server.champion.config.ChampionConfigSelector;
import top.theillusivec4.champions.server.champion.config.ChampionConfigSelectorHolder;

import java.util.concurrent.CompletableFuture;

public final class ConfigSelectorCommand {
  private static final SimpleCommandExceptionType ERROR_NO_CONFIG_SELECTOR_ON_CLIENT = new SimpleCommandExceptionType(Component.translatable(LanguageKeys.COMMANDS_ERROR_NO_CONFIG_SELECTOR_ON_CLIENT_KEY));
  private static final DynamicCommandExceptionType ERROR_INVALID_CONFIG_SELECTOR = new DynamicCommandExceptionType(object -> Component.translatable(LanguageKeys.COMMANDS_ERROR_INVALID_CONFIG_SELECTOR_KEY, object));

  public static void register(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext buildContext) {
    builder.then(Commands.literal("config_selector")
      .then(Commands.argument("config_selector", ResourceKeyArgument.key(Registries.CHAMPION_CONFIG_SELECTOR))
        .suggests(ConfigSelectorCommand::suggestion)
        .then(Commands.argument("target", EntityArgument.entity())
          .executes(context -> applyConfig(context.getSource(), getConfigSelector(context, "config_selector"), EntityArgument.getEntity(context, "target")))
        )
      )
    );
  }

  private static CompletableFuture<Suggestions> suggestion(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
    return SharedSuggestionProvider.suggestResource(Champions.getInstance().getChampionConfigSelectorManager().getKeys(), builder);
  }

  private static ChampionConfigSelector getConfigSelector(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
    if (context.getSource().getUnsidedLevel().isClientSide()) {
      throw ERROR_NO_CONFIG_SELECTOR_ON_CLIENT.create();
    } else {
      ResourceKey<ChampionConfigSelector> key = ResourceKeyArgument.getRegistryKey(context, name, Registries.CHAMPION_CONFIG_SELECTOR, ERROR_INVALID_CONFIG_SELECTOR);
      ChampionConfigSelectorHolder selectorHolder = Champions.getInstance().getChampionConfigSelectorManager().byKey(key);
      if (selectorHolder == null) {
        throw ERROR_INVALID_CONFIG_SELECTOR.create(key.identifier().toString());
      } else {
        return selectorHolder.value();
      }
    }
  }

  private static int applyConfig(CommandSourceStack source, ChampionConfigSelector configSelector, Entity target) {
    return ChampionUtil.getHandler(target).map(handler -> {
      ServerLevel serverLevel = source.getLevel();
      configSelector.select(serverLevel, target, null).ifPresent(handler::applyConfig);
      source.sendSuccess(() -> Component.translatable(LanguageKeys.COMMANDS_CONFIG_SELECTOR_SUCCESS_KEY), true);
      return 1;
    }).orElse(0);
  }


  private ConfigSelectorCommand() {
  }
}
