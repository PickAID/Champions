package top.theillusivec4.champions.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
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
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.common.item.DataComponentTypes;
import top.theillusivec4.champions.common.item.components.ItemAffixes;
import top.theillusivec4.champions.common.registries.Registries;
import top.theillusivec4.champions.common.util.Utils;

import java.util.Collection;
import java.util.List;

public final class SpawnEggCommand {
  public static final String SUCCESS_KEY = "commands.champions_egg.success";
  public static final String FAILED_KEY = "commands.champions_egg.failed";
  public static final SuggestionProvider<CommandSourceStack> SPAWN_EGGS = SuggestionProviders.register(Utils.id("spawn_eggs"), (context, builder) -> SharedSuggestionProvider.suggestResource(BuiltInRegistries.ITEM.stream().filter(item -> item instanceof SpawnEggItem), builder, BuiltInRegistries.ITEM::getKey, Item::getName));
  private static final DynamicCommandExceptionType NOT_SPAWN_EGG = new DynamicCommandExceptionType(item -> Component.translatable(FAILED_KEY, item));

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext builder) {
    dispatcher.register(Commands.literal("champions_egg").requires(Commands.hasPermission(Commands.LEVEL_ADMINS)).then(Commands.argument("players", EntityArgument.players()).then(Commands.argument("spawn_egg", ItemArgument.item(builder)).suggests(SPAWN_EGGS).then(Commands.argument("level", IntegerArgumentType.integer(1, 255)).then(Commands.argument("affixes", ResourceArgument.resource(builder, Registries.AFFIX)).executes(context -> giveSpawnEgg(context.getSource(), ItemArgument.getItem(context, "spawn_egg"), EntityArgument.getPlayers(context, "players"), IntegerArgumentType.getInteger(context, "level"), ResourceArgument.getResource(context, "affixes", Registries.AFFIX))))))

      )

    );
  }

  private static int giveSpawnEgg(CommandSourceStack source, ItemInput itemInput, Collection<ServerPlayer> players, int level, Holder.Reference<Affix> affix) throws CommandSyntaxException {
    Item item = itemInput.getItem();
    if (item instanceof SpawnEggItem) {
      for (Player player : players) {
        ResourceHandler<ItemResource> handler = player.getCapability(Capabilities.Item.ENTITY);
        if (handler != null) {
          try (Transaction transaction = Transaction.openRoot()) {
            ItemStack itemStack = new ItemStack(item);
            itemStack.set(DataComponentTypes.ITEM_AFFIXES, new ItemAffixes(level, List.of(affix)));

            if (handler.insert(ItemResource.of(itemStack), 1, transaction) == 1) {
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

  private SpawnEggCommand() {
  }
}
