package top.theillusivec4.champions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import top.theillusivec4.champions.champion.ChampionHandler;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.registries.Registries;
import top.theillusivec4.champions.util.Utils;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public final class SpawnEggCommand {
  public static final String SUCCESS_KEY = "commands.champions_egg.success";
  public static final String FAILED_KEY = "commands.champions_egg.failed";
  public static final SuggestionProvider<CommandSourceStack> SPAWN_EGGS = SuggestionProviders.register(Utils.id("spawn_eggs"), SpawnEggCommand::suggest);
  private static final DynamicCommandExceptionType NOT_SPAWN_EGG = new DynamicCommandExceptionType(item -> Component.translatable(FAILED_KEY, item));

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
    dispatcher.register(Commands.literal("champions_egg").requires(Commands.hasPermission(Commands.LEVEL_ADMINS)).then(Commands.argument("players", EntityArgument.players()).then(Commands.argument("spawn_egg", ItemArgument.item(buildContext)).suggests(SPAWN_EGGS).then(Commands.argument("level", IntegerArgumentType.integer(1, 255)).then(Commands.argument("affixes", ResourceArgument.resource(buildContext, Registries.AFFIX)).executes(context -> giveSpawnEgg(context.getSource(), ItemArgument.getItem(context, "spawn_egg"), EntityArgument.getPlayers(context, "players"), IntegerArgumentType.getInteger(context, "level"), ResourceArgument.getResource(context, "affixes", Registries.AFFIX))))))));
  }

  public static void register(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext buildContext) {
    builder.then(Commands.literal("egg")
        .then(Commands.argument("players", EntityArgument.players())
          .then(Commands.argument("spawn_egg", ItemArgument.item(buildContext)).suggests(SPAWN_EGGS)
            .then(Commands.argument("level", IntegerArgumentType.integer(1, 255))
              .then(Commands.argument("affix", ResourceArgument.resource(buildContext, Registries.AFFIX))
                .executes(context -> giveSpawnEgg(context.getSource(), ItemArgument.getItem(context, "spawn_egg"), EntityArgument.getPlayers(context, "players"), IntegerArgumentType.getInteger(context, "level"), ResourceArgument.getResource(context, "affix", Registries.AFFIX))
                )
              )
            )
          )
        )
      )
      .then(Commands.literal("affix")
        .then(Commands.argument("players", EntityArgument.players())
          .then(Commands.argument("affix", ResourceArgument.resource(buildContext, Registries.AFFIX))
            .executes(context -> affix(context.getSource(), EntityArgument.getPlayers(context, "players"), ResourceArgument.getResource(context, "affix", Registries.AFFIX)))
          )
        )
      )
      .then(Commands.literal("level")
        .then(Commands.argument("players", EntityArgument.players())
          .then(Commands.argument("level", IntegerArgumentType.integer(1, 5))
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
    ;
  }

  private static CompletableFuture<Suggestions> suggest(final CommandContext<SharedSuggestionProvider> context, final SuggestionsBuilder builder) {
    return SharedSuggestionProvider.suggestResource(BuiltInRegistries.ITEM.stream().filter(SpawnEggCommand::isValidItem), builder, BuiltInRegistries.ITEM::getKey, Item::getName);
  }

  private static int rank(CommandSourceStack source, Collection<ServerPlayer> players, Holder.Reference<Rank> rank) {
    int i = 0;
    for (ServerPlayer player : players) {
      ItemStack itemStack = player.getMainHandItem();
      ChampionUtil.getHandler(itemStack, source.getLevel()).ifPresent(handler -> {
        handler.setRank(rank);
        handler.setDisplay(true);
      });
    }
    source.sendSuccess(() -> Component.translatable("commands.champions.rank.success", i), true);
    return i;
  }

  private static int affix(CommandSourceStack source, Collection<ServerPlayer> players, Holder.Reference<Affix> affix) {
    int i = 0;
    for (ServerPlayer player : players) {
      ItemStack itemStack = player.getMainHandItem();
      ChampionUtil.getHandler(itemStack, source.getLevel())
        .ifPresent(handler -> {
          handler.updateAffixes(mutable -> mutable.add(affix));
          handler.setDisplay(true);
        });
    }
    source.sendSuccess(() -> Component.translatable("commands.champions.affix.success", i), true);
    return i;
  }

  private static int level(CommandSourceStack source, Collection<ServerPlayer> players, int level) {
    int i = 0;
    for (ServerPlayer player : players) {
      ItemStack itemStack = player.getMainHandItem();
      ChampionUtil.getHandler(itemStack, source.getLevel())
        .ifPresent(handler -> {
          handler.setLevel(level);
          handler.setDisplay(true);
        });
//      if (isValidItem(itemStack.getItem())) {
//        itemStack.set(DataComponents.LEVEL, level);
//        itemStack.set(DataComponents.DISPLAY, true);
//      }
    }
    source.sendSuccess(() -> Component.translatable("commands.champions.level.success", i), true);
    return i;
  }

  private static int giveSpawnEgg(CommandSourceStack source, ItemInput itemInput, Collection<ServerPlayer> players, int level, Holder.Reference<Affix> affix) throws CommandSyntaxException {
    Item item = itemInput.getItem();
    if (isValidItem(item)) {
      for (Player player : players) {
        ResourceHandler<ItemResource> resourceHandler = player.getCapability(Capabilities.Item.ENTITY);
        if (resourceHandler != null) {
          try (Transaction transaction = Transaction.openRoot()) {
            ItemStack itemStack = new ItemStack(item);
            ChampionHandler championHandler = ChampionUtil.getHandler(itemStack, source.getLevel()).orElseThrow();
            championHandler.updateAffixes(mutable -> mutable.add(affix));
            championHandler.setDisplay(true);

//            itemStack.set(DataComponents.AFFIXES, new Affixes(List.of(affix)));
//            itemStack.set(DataComponents.DISPLAY, true);

            if (resourceHandler.insert(ItemResource.of(itemStack), 1, transaction) == 1) {
              transaction.commit();
            } else {
              ItemEntity drop = player.drop(itemStack, false);
              if (drop != null) {
                drop.setNoPickUpDelay();
                drop.setTarget(player.getUUID());
              }
            }
          }
        }
        source.sendSuccess(() -> Component.translatable(SUCCESS_KEY, player.getDisplayName()), true);
      }
    } else {
      throw NOT_SPAWN_EGG.create(item.getName());
    }

    return players.size();
  }

  private static boolean isValidItem(Item item) {
//    if (item instanceof SpawnEggItem spawnEggItem) {
//      TypedEntityData<EntityType<?>> entityType = spawnEggItem.components().get(net.minecraft.core.component.DataComponents.ENTITY_DATA);
//      return entityType != null && top.theillusivec4.champions.capabilities.Capabilities.ChampionHandlers.isImplemented(entityType.type());
//    }

//    return false;
    return top.theillusivec4.champions.capabilities.Capabilities.ChampionHandlers.isImplemented(item);
  }

  private SpawnEggCommand() {
  }
}
