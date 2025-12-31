package top.theillusivec4.champions.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.registry.ModDataComponents;
import top.theillusivec4.champions.common.util.ChampionBuilder;
import top.theillusivec4.champions.common.util.Utils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;

public class ChampionEggItem extends EggItem {

  private static final String ID_TAG = "Id";
  private static final String ENTITY_TAG = "EntityTag";
  private static final String TIER_TAG = "Tier";
  private static final String AFFIX_TAG = "Affix";
  private static final String CHAMPION_TAG = "Champion";

  public ChampionEggItem() {
    super(new Item.Properties().useItemDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, Utils.getLocation("champion_egg"))));
  }

  public static Optional<EntityType<?>> getType(ItemStack stack) {

    Optional<CompoundTag> entityTag = getTagOrEmpty(stack, ENTITY_TAG);
    if (entityTag.isPresent()) {
      var id = entityTag.get().getString(ID_TAG);

      if (id.isPresent()) {
        return BuiltInRegistries.ENTITY_TYPE.getOptional(Identifier.parse(id.get()));
      }
    }
    return Optional.empty();
  }

  public static Optional<CompoundTag> getTagOrEmpty(ItemStack stack, String tagKey) {
    if (stack.has(ModDataComponents.ENTITY_TAG_COMPONENT)) {
      CompoundTag entityTag = stack.get(ModDataComponents.ENTITY_TAG_COMPONENT);
      if (entityTag != null) {
        return entityTag.getCompound(tagKey);
      }
    }
    return Optional.empty();
  }

  public static void read(IChampion champion, ItemStack stack) {
    Optional<CompoundTag> tag = getTagOrEmpty(stack, CHAMPION_TAG);

    tag.ifPresent(entityTag -> {
      int tier = entityTag.getIntOr(TIER_TAG, -1);
      ListTag affixTag = entityTag.getListOrEmpty(AFFIX_TAG);
      List<IAffix> affixes = new ArrayList<>();
      affixTag.forEach(affix -> Champions.API.getAffix(affix.asString().orElseThrow()).ifPresent(affixes::add));
      ChampionBuilder.spawnPreset(champion, tier, affixes);
    });
  }

  public static void write(
    ItemStack stack, Identifier entityId, int tier,
    Collection<IAffix> affixes) {
    CompoundTag tag = stack.getOrDefault(ModDataComponents.ENTITY_TAG_COMPONENT, new CompoundTag());

    CompoundTag entityTag = new CompoundTag();
    entityTag.putString(ID_TAG, entityId.toString());
    tag.put(ENTITY_TAG, entityTag);

    CompoundTag tierTag = new CompoundTag();
    tierTag.putInt(TIER_TAG, tier);
    ListTag listNBT = new ListTag();
    affixes.forEach(affix -> listNBT.add(StringTag.valueOf(affix.getIdentifier().toString())));
    tierTag.put(AFFIX_TAG, listNBT);
    tag.put(CHAMPION_TAG, tierTag);
    stack.set(ModDataComponents.ENTITY_TAG_COMPONENT, tag);
  }

  @Nonnull
  @Override
  public Component getName(@Nonnull ItemStack stack) {
    int tier = 0;
    Optional<EntityType<?>> type = getType(stack);
    Optional<CompoundTag> entityTag = getTagOrEmpty(stack, CHAMPION_TAG);
    if (entityTag.isPresent()) {
      tier = entityTag.get().getIntOr(TIER_TAG, -1);
    }

    MutableComponent root = Component.translatable("rank.champions.title." + tier);
    root.append(" ");
    root.append(type.map(EntityType::getDescription).orElse(EntityType.ZOMBIE.getDescription()));
    root.append(" ");
    root.append(this.getName());
    return root;
  }

  @Override
  @ParametersAreNonnullByDefault
  @SuppressWarnings("deprecation")
  public void appendHoverText(ItemStack stack, TooltipContext pContext, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag flagIn) {
    boolean hasAffix = false;
    Optional<CompoundTag> entityTag = getTagOrEmpty(stack, CHAMPION_TAG);
    if (entityTag.isPresent()) {

      ListTag affixTag = entityTag.get().getListOrEmpty(AFFIX_TAG);

      if (!affixTag.isEmpty()) {
        hasAffix = true;
      }

      affixTag.forEach(affix -> Champions.API.getAffix(affix.asString().orElse("")).ifPresent(
        affix1 -> {
          final MutableComponent component =
            Component.translatable(affix1.toLanguageKey());
          component.setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));
          componentConsumer.accept(component);
        }));

    }

    if (!hasAffix) {
      final MutableComponent component = Component.translatable("item.champions.egg.tooltip");
      component.setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA));
      componentConsumer.accept(component);
    }
  }

  @Nonnull
  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level world = context.getLevel();

    if (!world.isClientSide() && world instanceof ServerLevel) {
      ItemStack itemstack = context.getItemInHand();
      BlockPos blockpos = context.getClickedPos();
      Direction direction = context.getClickedFace();
      BlockState blockstate = world.getBlockState(blockpos);
      BlockPos blockpos1;

      if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
        blockpos1 = blockpos;
      } else {
        blockpos1 = blockpos.relative(direction);
      }
      var entityType = getType(itemstack);
      entityType.ifPresent(type -> {
        var entity = type
          .create((ServerLevel) world, null, blockpos1,
            EntitySpawnReason.SPAWN_ITEM_USE, true,
            !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);

        if (entity instanceof LivingEntity livingEntity) {
          ChampionAttachment.getAttachment(livingEntity).ifPresent(iChampion -> {
            read(iChampion, itemstack);
            world.addFreshEntity(entity);
            itemstack.shrink(1);
          });
        }
      });
    }
    return InteractionResult.SUCCESS;
  }

  @Nonnull
  @Override
  public InteractionResult use(Level worldIn, Player playerIn,
                               @Nonnull InteractionHand handIn) {
    ItemStack itemstack = playerIn.getItemInHand(handIn);

    if (worldIn.isClientSide()) {
      return InteractionResult.PASS;
    } else if (worldIn instanceof ServerLevel) {
      BlockHitResult povHitResult = getPlayerPOVHitResult(worldIn, playerIn,
        ClipContext.Fluid.SOURCE_ONLY);

      if (povHitResult.getType() != HitResult.Type.BLOCK) {
        return InteractionResult.PASS;
      } else {
        BlockPos blockpos = povHitResult.getBlockPos();

        if (!(worldIn.getFluidState(blockpos).getType() instanceof FlowingFluid)) {
          return InteractionResult.PASS;
        } else if (worldIn.mayInteract(playerIn, blockpos) && playerIn
          .mayUseItemAt(blockpos, povHitResult.getDirection(), itemstack)) {
          Optional<EntityType<?>> entityType = getType(itemstack);
          return entityType.map(type -> {
            Entity entity = type
              .create((ServerLevel) worldIn, null, blockpos,
                EntitySpawnReason.SPAWN_ITEM_USE, false, false);

            if (entity instanceof LivingEntity) {
              ChampionAttachment.getAttachment(entity).ifPresent(iChampion -> read(iChampion, itemstack));
              worldIn.addFreshEntity(entity);

              if (!playerIn.getAbilities().invulnerable) {
                itemstack.shrink(1);
              }
              playerIn.awardStat(Stats.ITEM_USED.get(this));
              return InteractionResult.SUCCESS.heldItemTransformedTo(itemstack);
            } else {
              return InteractionResult.PASS;
            }
          }).orElse(InteractionResult.PASS);
        } else {
          return InteractionResult.FAIL;
        }
      }
    }
    return InteractionResult.FAIL;
  }
}
