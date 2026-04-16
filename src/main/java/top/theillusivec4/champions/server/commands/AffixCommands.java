package top.theillusivec4.champions.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.world.entity.affix.Affix;
import top.theillusivec4.champions.world.entity.affix.AffixHelper;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;

import java.util.Collection;

public final class AffixCommands {
  private AffixCommands() {
  }

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
    dispatcher.register(Commands.literal(Champions.MOD_ID)
      .then(Commands.literal("affix")
        .then(Commands.literal("add")
          .then(Commands.argument("entities", EntityArgument.entities())
            .then(Commands.argument("affix", ResourceArgument.resource(buildContext, ChampionsRegistries.AFFIX))
              .then(Commands.argument("level", IntegerArgumentType.integer(1, 255))
                .executes(context -> add(
                    context.getSource(),
                    EntityArgument.getEntities(context, "entities"),
                    ResourceArgument.getResource(context, "affix", ChampionsRegistries.AFFIX),
                    IntegerArgumentType.getInteger(context, "level")
                  )
                )
              )
            )
          )
        )
        .then(Commands.literal("remove")
          .then(Commands.argument("affix", ResourceArgument.resource(buildContext, ChampionsRegistries.AFFIX))
            .executes(context -> remove(context.getSource(), EntityArgument.getEntities(context, "entities"), ResourceArgument.getResource(context, "affix", ChampionsRegistries.AFFIX)))
          )
        )
      )
    );
  }

  private static int add(CommandSourceStack source, Collection<? extends Entity> entities, Holder<Affix> affix, int level) {
    int i = 0;
    for (Entity entity : entities) {
      AffixHelper.update(entity, affixes -> affixes.set(affix, level));
      i++;
    }

    if (entities.size() == 1) {
      source.sendSuccess(() -> Component.translatable(
          "commands.champions.affix.add.success.single",
          affix.value().description(),
          entities.stream().findFirst().orElseThrow().getDisplayName()
        ),
        false
      );
    } else {
      source.sendSuccess(() -> Component.translatable(
          "commands.champions.affix.add.success.multiple",
          entities.size(),
          affix.value().description()
        ),
        false
      );
    }

    return i;
  }

  private static int remove(CommandSourceStack source, Collection<? extends Entity> entities, Holder<Affix> affix) {
    int i = 0;
    for (Entity entity : entities) {
      AffixHelper.update(entity, affixes -> affixes.remove(affix));
      i++;
    }

    if (entities.size() == 1) {
      source.sendSuccess(() -> Component.translatable(
          "commands.champions.affix.remove.success.single",
          affix.value().description(),
          entities.stream().findFirst().orElseThrow().getDisplayName()
        ),
        false
      );
    } else {
      source.sendSuccess(() -> Component.translatable(
          "commands.champions.affix.remove.success.multiple",
          entities.size(),
          affix.value().description()
        ),
        false
      );
    }

    return i;
  }
}
