package top.theillusivec4.champions.deprecated.server.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.PlayerInventoryWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.api.AffixRegistry;
import top.theillusivec4.champions.deprecated.api.affix.IAffix;
import top.theillusivec4.champions.deprecated.common.capabilities.ChampionAttachment;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.deprecated.common.item.ChampionEggItem;
import top.theillusivec4.champions.items.Items;
import top.theillusivec4.champions.deprecated.common.util.ChampionBuilder;
import top.theillusivec4.champions.deprecated.common.util.ChampionHelper;
import top.theillusivec4.champions.util.Utils;
import top.theillusivec4.champions.deprecated.server.command.argument.AffixArgumentType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Deprecated
public class ChampionsCommand {

  public static final SuggestionProvider<CommandSourceStack> AFFIXES = SuggestionProviders
    .register(Utils.id("affixes"), (context, builder) -> SharedSuggestionProvider.suggestResource(
      AffixRegistry.AFFIX_REGISTRY.stream().filter(IAffix::isEnabled), builder, IAffix::getIdentifier, affix -> Component.translatable(affix.toLanguageKey())));

  public static final SuggestionProvider<CommandSourceStack> MONSTER_ENTITIES = SuggestionProviders
    .register(Utils.id("monster_entities"),
      (context, builder) -> SharedSuggestionProvider.suggestResource(
        BuiltInRegistries.ENTITY_TYPE.stream()
          .filter(type -> {
            if (ChampionsConfig.allowChampionsList) {
              return ChampionHelper.isValidChampionEntityType(type);
            }
            return type.getCategory() == MobCategory.MONSTER;
          }),
        builder, EntityType::getKey,
        (type) -> Component.translatable(
          Util.makeDescriptionId("entity", EntityType.getKey(type)))));


  private static final DynamicCommandExceptionType UNKNOWN_ENTITY = new DynamicCommandExceptionType(
    type -> Component.translatable("command.champions.egg.unknown_entity", type));

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    int opPermissionLevel = 2;
    LiteralArgumentBuilder<CommandSourceStack> championsCommand = Commands.literal("champions")
      .requires(Commands.hasPermission(Commands.LEVEL_ADMINS));

    championsCommand.then(Commands.literal("egg").then(
      Commands.argument("entity", IdentifierArgument.id()).suggests(MONSTER_ENTITIES)
        .then(Commands.argument("tier", IntegerArgumentType.integer(1)).executes(
          context -> createEgg(context.getSource(),
            IdentifierArgument.getId(context, "entity"),
            IntegerArgumentType.getInteger(context, "tier"), new ArrayList<>())).then(
          Commands.argument("affixes", AffixArgumentType.affix()).suggests(AFFIXES).executes(
            context -> createEgg(context.getSource(),
              IdentifierArgument.getId(context, "entity"),
              IntegerArgumentType.getInteger(context, "tier"),
              AffixArgumentType.getAffixes(context, "affixes")))))));

    championsCommand.then(Commands.literal("summon").then(
      Commands.argument("entity", IdentifierArgument.id()).suggests(MONSTER_ENTITIES)
        .then(Commands.argument("tier", IntegerArgumentType.integer(1)).executes(
          context -> summon(context.getSource(),
            IdentifierArgument.getId(context, "entity"),
            IntegerArgumentType.getInteger(context, "tier"), new ArrayList<>())).then(
          Commands.argument("affixes", AffixArgumentType.affix()).suggests(AFFIXES).executes(
            context -> summon(context.getSource(),
              IdentifierArgument.getId(context, "entity"),
              IntegerArgumentType.getInteger(context, "tier"),
              AffixArgumentType.getAffixes(context, "affixes")))))));

    championsCommand.then(Commands.literal("summonpos").then(
      Commands.argument("pos", BlockPosArgument.blockPos()).then(
        Commands.argument("entity", IdentifierArgument.id())
          .suggests(MONSTER_ENTITIES).then(
            Commands.argument("tier", IntegerArgumentType.integer(1)).executes(
              context -> summon(context.getSource(),
                BlockPosArgument.getSpawnablePos(context, "pos"),
                IdentifierArgument.getId(context, "entity"),
                IntegerArgumentType.getInteger(context, "tier"), new ArrayList<>())).then(
              Commands.argument("affixes", AffixArgumentType.affix()).suggests(AFFIXES).executes(
                context -> summon(context.getSource(),
                  BlockPosArgument.getSpawnablePos(context, "pos"),
                  IdentifierArgument.getId(context, "entity"),
                  IntegerArgumentType.getInteger(context, "tier"),
                  AffixArgumentType.getAffixes(context, "affixes"))))))));

    dispatcher.register(championsCommand);
  }

  private static int summon(CommandSourceStack source, Identifier resourceLocation, int tier,
                            Collection<IAffix> affixes) throws CommandSyntaxException {
    return summon(source, null, resourceLocation, tier, affixes);
  }

  private static int summon(CommandSourceStack source, @Nullable BlockPos pos,
                            Identifier resourceLocation, int tier, Collection<IAffix> affixes)
    throws CommandSyntaxException {
    var entityType = getTypeOrThrow(resourceLocation);
    var sourceEntity = source.getPlayerOrException();

    Entity entity = entityType.create(sourceEntity.level(), null,
      pos != null ? pos : new BlockPos(sourceEntity.blockPosition()), EntitySpawnReason.COMMAND,
      false, false);

    ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
      ChampionBuilder.spawnPreset(champion, tier, new ArrayList<>(affixes));
      var livingEntity = champion.getLivingEntity();
      source.getLevel().addFreshEntity(livingEntity);
      source.sendSuccess(() -> Component.translatable("commands.champions.summon.success",
        Component.translatable("rank.champions.title." + tier).getString() + " " + livingEntity
          .getName().getString()), false);
    });

    return Command.SINGLE_SUCCESS;
  }

  private static int createEgg(CommandSourceStack source, Identifier resourceLocation,
                               int tier,
                               Collection<IAffix> affixes) throws CommandSyntaxException {
    var entityType = getTypeOrThrow(resourceLocation);
    var player = source.getPlayerOrException();

    ItemStack egg = createEgg(entityType, tier, affixes);
    if (!insertEggToPlayer(player, egg)) {
      Champions.LOGGER.debug("Player inventory is full");
    }
    source.sendSuccess(() -> Component.translatable("commands.champions.egg.success", egg.getDisplayName()), false);

    return Command.SINGLE_SUCCESS;
  }

  private static boolean insertEggToPlayer(Player player, ItemStack itemStack){
    PlayerInventoryWrapper wrapper = PlayerInventoryWrapper.of(player);
    try (Transaction transaction = Transaction.openRoot()){
      int inserted = wrapper.insert(ItemResource.of(itemStack), 1, transaction);
      if (inserted == 1) {
        transaction.commit();
        return true;
      }
    }
    return false;
  }

  public static ItemStack createEgg(EntityType<?> entityType,
                                    int tier,
                                    Collection<IAffix> affixes) {
    ItemStack egg = new ItemStack(Items.CHAMPION_EGG_ITEM.get());

    // Get entity's egg model data
    // then applies to champions egg
    applyItemModelOrFallback(egg, entityType);

    ChampionEggItem.write(egg, getEntityKey(entityType), tier, affixes);
    return egg;
  }

  /**
   * Apply itemModel from an entity type<br/>
   * if entity hasn't itemModel, then fallback to Zombie ItemModel
   *
   * @param championEgg  the champion egg used to summon champion, will copy model data from entity type
   * @param hasItemModel the entity type that we will look up SpawnEggItem model data
   */
  private static void applyItemModelOrFallback(ItemStack championEgg, EntityType<?> hasItemModel) {
    var entityNormalSpawnEgg = SpawnEggItem.byId(hasItemModel);
    if (entityNormalSpawnEgg != null) {
      ItemStack entityEggStack = new ItemStack(entityNormalSpawnEgg);
      Identifier itemModelLocation = entityEggStack.get(DataComponents.ITEM_MODEL);
      if (itemModelLocation == null) {
        itemModelLocation = Objects.requireNonNull(SpawnEggItem.byId(EntityType.ZOMBIE)).getDefaultInstance().get(DataComponents.ITEM_MODEL);
      }
      championEgg.set(DataComponents.ITEM_MODEL, itemModelLocation);
    }
  }

  private static Identifier getEntityKey(EntityType<?> entityType) {
    return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
  }

  private static EntityType<?> getTypeOrThrow(Identifier resourceLocation) throws CommandSyntaxException {
    return BuiltInRegistries.ENTITY_TYPE.getOptional(resourceLocation).orElseThrow(() -> UNKNOWN_ENTITY.create(resourceLocation));
  }
}
