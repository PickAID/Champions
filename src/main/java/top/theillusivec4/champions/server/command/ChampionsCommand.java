package top.theillusivec4.champions.server.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.item.ChampionEggItem;
import top.theillusivec4.champions.common.registry.ModItems;
import top.theillusivec4.champions.common.util.ChampionBuilder;
import top.theillusivec4.champions.common.util.ChampionHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ChampionsCommand {

  public static final SuggestionProvider<CommandSourceStack> AFFIXES = SuggestionProviders
    .register(Champions.getLocation("affixes"), (context, builder) -> SharedSuggestionProvider.suggestResource(
      AffixRegistry.AFFIX_REGISTRY.stream(), builder, IAffix::getIdentifier, affix -> Component.translatable(affix.toLanguageKey())));

  public static final SuggestionProvider<CommandSourceStack> MONSTER_ENTITIES = SuggestionProviders
    .register(Champions.getLocation("monster_entities"),
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
      .requires(player -> player.hasPermission(opPermissionLevel));

    championsCommand.then(Commands.literal("egg").then(
      Commands.argument("entity", ResourceLocationArgument.id()).suggests(MONSTER_ENTITIES)
        .then(Commands.argument("tier", IntegerArgumentType.integer(1)).executes(
          context -> createEgg(context.getSource(),
            ResourceLocationArgument.getId(context, "entity"),
            IntegerArgumentType.getInteger(context, "tier"), new ArrayList<>())).then(
          Commands.argument("affixes", AffixArgumentType.affix()).suggests(AFFIXES).executes(
            context -> createEgg(context.getSource(),
              ResourceLocationArgument.getId(context, "entity"),
              IntegerArgumentType.getInteger(context, "tier"),
              AffixArgumentType.getAffixes(context, "affixes")))))));

    championsCommand.then(Commands.literal("summon").then(
      Commands.argument("entity", ResourceLocationArgument.id()).suggests(MONSTER_ENTITIES)
        .then(Commands.argument("tier", IntegerArgumentType.integer(1)).executes(
          context -> summon(context.getSource(),
            ResourceLocationArgument.getId(context, "entity"),
            IntegerArgumentType.getInteger(context, "tier"), new ArrayList<>())).then(
          Commands.argument("affixes", AffixArgumentType.affix()).suggests(AFFIXES).executes(
            context -> summon(context.getSource(),
              ResourceLocationArgument.getId(context, "entity"),
              IntegerArgumentType.getInteger(context, "tier"),
              AffixArgumentType.getAffixes(context, "affixes")))))));

    championsCommand.then(Commands.literal("summonpos").then(
      Commands.argument("pos", BlockPosArgument.blockPos()).then(
        Commands.argument("entity", ResourceLocationArgument.id())
          .suggests(MONSTER_ENTITIES).then(
            Commands.argument("tier", IntegerArgumentType.integer(1)).executes(
              context -> summon(context.getSource(),
                BlockPosArgument.getSpawnablePos(context, "pos"),
                ResourceLocationArgument.getId(context, "entity"),
                IntegerArgumentType.getInteger(context, "tier"), new ArrayList<>())).then(
              Commands.argument("affixes", AffixArgumentType.affix()).suggests(AFFIXES).executes(
                context -> summon(context.getSource(),
                  BlockPosArgument.getSpawnablePos(context, "pos"),
                  ResourceLocationArgument.getId(context, "entity"),
                  IntegerArgumentType.getInteger(context, "tier"),
                  AffixArgumentType.getAffixes(context, "affixes"))))))));

    dispatcher.register(championsCommand);
  }

  private static int summon(CommandSourceStack source, ResourceLocation resourceLocation, int tier,
                            Collection<IAffix> affixes) throws CommandSyntaxException {
    return summon(source, null, resourceLocation, tier, affixes);
  }

  private static int summon(CommandSourceStack source, @Nullable BlockPos pos,
                            ResourceLocation resourceLocation, int tier, Collection<IAffix> affixes)
    throws CommandSyntaxException {
    var entityType = getTypeOrThrow(resourceLocation);
    var sourceEntity = source.getPlayerOrException();

    Entity entity = entityType.create((ServerLevel) sourceEntity.level(), null,
      pos != null ? pos : new BlockPos(sourceEntity.blockPosition()), MobSpawnType.COMMAND,
      false, false);

    ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
      ChampionBuilder.spawnPreset(champion, tier, new ArrayList<>(affixes));
      source.getLevel().addFreshEntity(champion.getLivingEntity());
      source.sendSuccess(() -> Component.translatable("commands.champions.summon.success",
        Component.translatable("rank.champions.title." + tier).getString() + " " + Objects.requireNonNull(entity
          .getDisplayName()).getString()), false);
    });

    return Command.SINGLE_SUCCESS;
  }

  private static int createEgg(CommandSourceStack source, ResourceLocation resourceLocation,
                               int tier,
                               Collection<IAffix> affixes) throws CommandSyntaxException {
    var entityType = getTypeOrThrow(resourceLocation);
    var player = source.getPlayerOrException();

    ItemStack egg = createEgg(entityType, tier, affixes);
    ItemHandlerHelper.giveItemToPlayer(player, egg, 1);
    source.sendSuccess(() -> Component.translatable("commands.champions.egg.success", egg.getDisplayName()), false);

    return Command.SINGLE_SUCCESS;
  }

  public static ItemStack createEgg(EntityType<?> entityType,
                                    int tier,
                                    Collection<IAffix> affixes) {
    ItemStack egg = new ItemStack(ModItems.CHAMPION_EGG_ITEM.get());
    ChampionEggItem.write(egg, getEntityKey(entityType), tier, affixes);
    return egg;
  }

  private static ResourceLocation getEntityKey(EntityType<?> entityType) {
    return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
  }

  private static EntityType<?> getTypeOrThrow(ResourceLocation resourceLocation) throws CommandSyntaxException {
    return BuiltInRegistries.ENTITY_TYPE.getOptional(resourceLocation).orElseThrow(() -> UNKNOWN_ENTITY.create(resourceLocation));
  }
}
