package top.theillusivec4.champions.common.item;


import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.util.ChampionBuilder;
import top.theillusivec4.champions.common.util.Utils;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.*;

public class ChampionEggItem extends EggItem {

    private static final String ID_TAG = "Id";
    private static final String ENTITY_TAG = "EntityTag";
    private static final String TIER_TAG = "Tier";
    private static final String AFFIX_TAG = "Affix";
    private static final String CHAMPION_TAG = "Champion";

    public ChampionEggItem() {
        super(new Item.Properties());
    }

    public static int getColor(ItemStack stack, int tintIndex) {
        SpawnEggItem eggItem =
                ForgeSpawnEggItem.fromEntityType(getType(stack).orElse(EntityType.ZOMBIE));
        return eggItem != null ? eggItem.getColor(tintIndex) : 0;
    }

    public static Optional<EntityType<?>> getType(ItemStack stack) {

        Optional<CompoundNBT> entityTag = getTagOrEmpty(stack, ENTITY_TAG);
        if (entityTag.isPresent()) {
            String id = entityTag.get().getString(ID_TAG);

            if (!id.isEmpty()) {
                return Optional.ofNullable(ForgeRegistries.ENTITIES.getValue(ResourceLocation.tryParse(id)));
            }
        }
        return Optional.empty();
    }

    public static void read(IChampion champion, ItemStack stack) {
        Optional<CompoundNBT> tag = getTagOrEmpty(stack, CHAMPION_TAG);
        tag.ifPresent(entityTag -> {
            int tier = entityTag.getInt(TIER_TAG);
	        ListNBT affixTag = entityTag.getList(AFFIX_TAG, Constants.NBT.TAG_STRING);
            List<IAffix> affixes = new ArrayList<>();
            affixTag.forEach(affix -> Champions.API.getAffix(affix.getAsString()).ifPresent(affixes::add));
            ChampionBuilder.spawnPreset(champion, tier, affixes);
        });
    }

    public static void write(
            ItemStack stack, ResourceLocation entityId, int tier,
            Collection<IAffix> affixes) {
        CompoundNBT tag = stack.hasTag() ? stack.getTag() : new CompoundNBT();
        assert tag != null;

        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putString(ID_TAG, entityId.toString());
        tag.put(ENTITY_TAG, compoundNBT);

        CompoundNBT tierTag = new CompoundNBT();
        tierTag.putInt(TIER_TAG, tier);
        ListNBT listNBT = new ListNBT();
        affixes.forEach(affix -> listNBT.add(StringNBT.valueOf(affix.toString())));
        tierTag.put(AFFIX_TAG, listNBT);
        tag.put(CHAMPION_TAG, tierTag);
        stack.setTag(tag);
    }

    public static Optional<CompoundNBT> getTagOrEmpty(ItemStack stack, String tagKey) {
        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            CompoundNBT entityTag = stack.getTagElement(tagKey);
            if (entityTag != null) {
                return Optional.of(entityTag);
            }
        }
        return Optional.empty();
    }

    @Nonnull
    @Override
    public ITextComponent getName(@Nonnull ItemStack stack) {
        int tier = 0;
        Optional<EntityType<?>> type = getType(stack);
        Optional<CompoundNBT> entityTag = getTagOrEmpty(stack, CHAMPION_TAG);
        if (entityTag.isPresent()) {
            tier = entityTag.get().getInt(TIER_TAG);
        }

	    IFormattableTextComponent root = Utils.translatable("rank.champions.title." + tier);
        root.append(" ");
        root.append(type.map(EntityType::getDescription).orElse(EntityType.ZOMBIE.getDescription()));
        root.append(" ");
        root.append(this.getDescription());
        return root;
    }

    @Override
    @ParametersAreNullableByDefault
    public void appendHoverText(ItemStack stack, World worldIn,
                               List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        boolean hasAffix = false;
        Optional<CompoundNBT> entityTag = getTagOrEmpty(stack, CHAMPION_TAG);
        if (entityTag.isPresent()) {

	        ListNBT affixTag = entityTag.get().getList(AFFIX_TAG, Constants.NBT.TAG_STRING);

            if (!affixTag.isEmpty()) {
                hasAffix = true;
            }

            affixTag.forEach(affix -> Champions.API.getAffix(affix.getAsString()).ifPresent(
                    affix1 -> {
                        final TranslationTextComponent component =
		                        Utils.translatable(affix1.toLanguageKey());
                        component.setStyle(Style.EMPTY.withColor(TextFormatting.GRAY));
                        tooltip.add(component);
                    }));

        }

        if (!hasAffix) {
            final TranslationTextComponent component = Utils.translatable("item.champions.egg.tooltip");
            component.setStyle(Style.EMPTY.withColor(TextFormatting.AQUA));
            tooltip.add(component);
        }
    }

    @Nonnull
    @Override
    public ActionResultType useOn(ItemUseContext context) {
	    World world = context.getLevel();

        if (!world.isClientSide() && world instanceof ServerWorld) {
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
            Optional<EntityType<?>> entitytype = getType(itemstack);
            entitytype.ifPresent(type -> {
                Entity entity = type
                        .create((ServerWorld) world, itemstack.getTag(), null,null, blockpos1,
                                SpawnReason.SPAWN_EGG, true,
                                !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);

                if (entity instanceof LivingEntity) {
                    ChampionCapability.getCapability(entity)
                            .ifPresent(champion -> read(champion, itemstack));
                    world.addFreshEntity(entity);
                    itemstack.shrink(1);
                }
            });
        }
        return ActionResultType.SUCCESS;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn,
                                       @Nonnull Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        if (worldIn.isClientSide()) {
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        } else if (worldIn instanceof ServerWorld) {
            BlockRayTraceResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn,
		            RayTraceContext.FluidMode.SOURCE_ONLY);

            if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
                return new ActionResult<>(ActionResultType.PASS, itemstack);
            } else {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if (!(worldIn.getFluidState(blockpos).getType() instanceof FlowingFluid)) {
                    return new ActionResult<>(ActionResultType.PASS, itemstack);
                } else if (worldIn.mayInteract(playerIn, blockpos) && playerIn
                        .mayUseItemAt(blockpos, raytraceresult.getDirection(), itemstack)) {
                    Optional<EntityType<?>> entityType = getType(itemstack);
                    return entityType.map(type -> {
                        Entity entity = type
                                .create((ServerWorld) worldIn, itemstack.getTag(), null,null, blockpos,
                                        SpawnReason.SPAWN_EGG, false, false);

                        if (entity instanceof LivingEntity) {
                            ChampionCapability.getCapability(entity)
                                    .ifPresent(champion -> read(champion, itemstack));
                            worldIn.addFreshEntity(entity);

                            if (!playerIn.abilities.invulnerable) {
                                itemstack.shrink(1);
                            }
                            playerIn.awardStat(Stats.ITEM_USED.get(this));
                            return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
                        } else {
                            return new ActionResult<>(ActionResultType.PASS, itemstack);
                        }
                    }).orElse(new ActionResult<>(ActionResultType.PASS, itemstack));
                } else {
                    return new ActionResult<>(ActionResultType.FAIL, itemstack);
                }
            }
        }
        return new ActionResult<>(ActionResultType.FAIL, itemstack);
    }
}
