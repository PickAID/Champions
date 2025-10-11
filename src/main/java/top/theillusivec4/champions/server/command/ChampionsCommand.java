package top.theillusivec4.champions.server.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.command.arguments.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.item.ChampionEggItem;
import top.theillusivec4.champions.common.registry.ModItems;
import top.theillusivec4.champions.common.util.ChampionBuilder;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.common.util.Utils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ChampionsCommand {

    public static final SuggestionProvider<CommandSource> AFFIXES = SuggestionProviders
            .register(Utils.getLocation("affixes"), (context, builder) -> ISuggestionProvider.suggestResource(
                    AffixRegistry.getRegistry().getValues().stream().filter(IAffix::isEnabled), builder, IAffix::getIdentifier, affix -> Utils.translatable(affix.toLanguageKey())));

    public static final SuggestionProvider<CommandSource> MONSTER_ENTITIES = SuggestionProviders
            .register(Utils.getLocation("monster_entities"),
                    (context, builder) -> ISuggestionProvider.suggestResource(
                            ForgeRegistries.ENTITIES.getValues().stream()
                                    .filter(ChampionHelper::isValidChampionEntityType),
                            builder, EntityType::getKey,
                            (type) -> Utils.translatable(
                                    Util.makeDescriptionId("entity", EntityType.getKey(type)))));


    private static final DynamicCommandExceptionType UNKNOWN_ENTITY = new DynamicCommandExceptionType(
            type -> Utils.translatable("command.champions.egg.unknown_entity", type));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        int opPermissionLevel = 2;
        LiteralArgumentBuilder<CommandSource> championsCommand = Commands.literal("champions")
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
                                                        BlockPosArgument.getLoadedBlockPos(context, "pos"),
                                                        ResourceLocationArgument.getId(context, "entity"),
                                                        IntegerArgumentType.getInteger(context, "tier"), new ArrayList<>())).then(
                                                Commands.argument("affixes", AffixArgumentType.affix()).suggests(AFFIXES).executes(
                                                        context -> summon(context.getSource(),
                                                                BlockPosArgument.getLoadedBlockPos(context, "pos"),
                                                                ResourceLocationArgument.getId(context, "entity"),
                                                                IntegerArgumentType.getInteger(context, "tier"),
                                                                AffixArgumentType.getAffixes(context, "affixes"))))))));

        dispatcher.register(championsCommand);
    }

    private static int summon(CommandSource source, ResourceLocation resourceLocation, int tier,
                              Collection<IAffix> affixes) throws CommandSyntaxException {
        return summon(source, null, resourceLocation, tier, affixes);
    }

    private static int summon(CommandSource source, @Nullable BlockPos pos,
                              ResourceLocation resourceLocation, int tier, Collection<IAffix> affixes)
            throws CommandSyntaxException {
        EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(resourceLocation);


        if (entityType == null) {
            throw UNKNOWN_ENTITY.create(resourceLocation);
        } else {
            final Entity sourceEntity = source.getEntity();

            if (sourceEntity != null) {
	            Entity entity = entityType.create((ServerWorld) sourceEntity.level, null, null, null,
			            pos != null ? pos : new BlockPos(sourceEntity.blockPosition()), SpawnReason.COMMAND,
			            false, false);

                if (entity instanceof LivingEntity) {
                    ChampionCapability.getCapability(entity).ifPresent(
                            champion -> ChampionBuilder.spawnPreset(champion, tier, new ArrayList<>(affixes)));
                    source.getLevel().addFreshEntity(entity);
                    source.sendSuccess(Utils.translatable("commands.champions.summon.success",
		                    Utils.translatable("rank.champions.title." + tier).getString() + " " + entity
                                    .getDisplayName().getString()), false);
                }
            }
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int createEgg(CommandSource source, ResourceLocation resourceLocation,
                                 int tier,
                                 Collection<IAffix> affixes) throws CommandSyntaxException {
        EntityType<?> entityType = getTypeOrThrow(resourceLocation);
        ServerPlayerEntity player = source.getPlayerOrException();

        ItemStack egg = createEgg(entityType, tier, affixes);
        ItemHandlerHelper.giveItemToPlayer(player, egg, 1);
        source.sendSuccess(Utils.translatable("commands.champions.egg.success", egg.getDisplayName()), false);

        return Command.SINGLE_SUCCESS;
    }

    public static ItemStack createEgg(EntityType<?> entityType,
                                      int tier,
                                      Collection<IAffix> affixes) {
        ItemStack egg = new ItemStack(ModItems.CHAMPION_EGG_ITEM.get());
        ChampionEggItem.write(egg, getEntityKey(entityType), tier, affixes);
        return egg;
    }

    private static EntityType<?> getTypeOrThrow(ResourceLocation resourceLocation) throws CommandSyntaxException {
        return Optional.ofNullable(ForgeRegistries.ENTITIES.getValue(resourceLocation)).orElseThrow(() -> UNKNOWN_ENTITY.create(resourceLocation));
    }

    private static ResourceLocation getEntityKey(EntityType<?> entityType) {
        return Optional.ofNullable(ForgeRegistries.ENTITIES.getKey(entityType)).orElse(new ResourceLocation("pig"));
    }
}
