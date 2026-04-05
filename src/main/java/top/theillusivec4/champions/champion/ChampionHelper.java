package top.theillusivec4.champions.champion;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.attachment.Attachments;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.AffixEffectComponents;
import top.theillusivec4.champions.champion.affix.Damage;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;
import top.theillusivec4.champions.champion.affix.effect.ConditionalEffect;
import top.theillusivec4.champions.champion.affix.effect.DamageImmunity;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.component.DataComponents;
import top.theillusivec4.champions.data.lang.LanguageKeys;
import top.theillusivec4.champions.data.lang.LanguageUtil;
import top.theillusivec4.champions.particle.ParticleTypes;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public final class ChampionHelper {
	private ChampionHelper() {
	}

	public static void addToTooltip(ItemStack itemStack, Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
		if (itemStack.has(top.theillusivec4.champions.component.DataComponents.AFFIX_CONTAINER_STORED)) {
			AffixContainer affixContainer = getAffixContainerStored(itemStack);
			int level = getLevel(itemStack);
			int color = getColor(itemStack);
			Component prefixName = getPrefix(itemStack);
			boolean boss = isBoss(itemStack);

			consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_LEVEL).withStyle(ChatFormatting.GRAY)
					.append(ChampionHelper.getLevelComponent(level)));

			consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_COLOR).withStyle(ChatFormatting.GRAY)
					.append(ChampionHelper.getColorComponent(color)));

			consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_PREFIX_NAME).withStyle(ChatFormatting.GRAY)
					.append(prefixName));

			consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_BOSS).withStyle(ChatFormatting.GRAY)
					.append(LanguageUtil.getBossStatusComponent(boss)));

			consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_AFFIXES).withStyle(ChatFormatting.GRAY));
			for (Holder<Affix> affix : affixContainer.getAffixList()) {
				consumer.accept(CommonComponents.space().append(affix.value().description()));
			}
		}
	}

	public static ItemStack getSpawnEgg(Entity entity) {
		EntityType<?> entityType = entity.getType();
		Identifier id = EntityType.getKey(entityType).withSuffix("_spawn_egg");
		Item item = BuiltInRegistries.ITEM.getValue(id);
		// 具有默认值的注册表
		//noinspection ConstantValue
		if (item != null && item != Items.AIR) {
			ItemStack itemStack = new ItemStack(item);
			if (entity.hasData(Attachments.PREFIX)) {
				itemStack.set(DataComponents.PREFIX, entity.getData(Attachments.PREFIX).orElseThrow());
			}
			if (entity.hasData(Attachments.LEVEL)) {
				itemStack.set(DataComponents.LEVEL, entity.getData(Attachments.LEVEL).orElseThrow());
			}
			if (entity.hasData(Attachments.COLOR)) {
				itemStack.set(DataComponents.COLOR, entity.getData(Attachments.COLOR).orElseThrow());
			}
			if (entity.hasData(Attachments.AFFIX_CONTAINER)) {
				itemStack.set(DataComponents.AFFIX_CONTAINER_STORED, entity.getData(Attachments.AFFIX_CONTAINER).orElseThrow());
			}
			if (entity.hasData(Attachments.BOSS)) {
				itemStack.set(DataComponents.BOSS, entity.getData(Attachments.BOSS).orElseThrow());
			}

			return itemStack;
		}

		return ItemStack.EMPTY;
	}

	public static AffixContainer getAffixContainerStored(ItemStack itemStack) {
		return itemStack.getOrDefault(DataComponents.AFFIX_CONTAINER_STORED, AffixContainer.EMPTY);
	}

	public static int getColor(ItemStack itemStack) {
		return itemStack.getOrDefault(DataComponents.COLOR, byLevelColor(getLevel(itemStack)));
	}

	public static Component getPrefix(ItemStack itemStack) {
		return itemStack.getOrDefault(DataComponents.PREFIX, byLevelPrefix(getLevel(itemStack)));
	}

	public static int getLevel(ItemStack itemStack) {
		return itemStack.getOrDefault(DataComponents.LEVEL, 1);
	}

	public static void setColor(ItemStack itemStack, int color) {
		itemStack.set(DataComponents.COLOR, color);
	}

	public static void setColor(Entity entity, int color) {
		entity.setData(Attachments.COLOR, Optional.of(color));
	}

	public static Component getPrefix(Entity entity) {
		return entity.getExistingData(Attachments.PREFIX).flatMap(Function.identity()).orElse(byLevelPrefix(getLevel(entity)));
	}

	public static Component byLevelPrefix(int level) {
		return Component.translatable("champion.prefix." + level);
	}

	public static Component getName(Entity entity) {
		return getPrefix(entity).copy().append(CommonComponents.space()).append(entity.getDisplayName()).withColor(getColor(entity));
	}

	public static boolean canDisplayHealthOverlay(Entity entity) {
		return !getAffixContainer(entity).isEmpty() && !isBoss(entity);
	}

	public static boolean isBoss(ItemStack itemStack) {
		return itemStack.getOrDefault(DataComponents.BOSS, false);
	}

	public static boolean isBoss(Entity entity) {
		return entity.getExistingData(Attachments.BOSS).flatMap(Function.identity()).orElse(false);
	}

	public static void setBoss(ItemStack itemStack, boolean boss) {
		itemStack.set(DataComponents.BOSS, boss);
	}

	public static void setBoss(LivingEntity entity, boolean boss) {
		entity.setData(Attachments.BOSS, Optional.of(boss));
		if (boss && !entity.hasData(Attachments.CHAMPION_EVENT)) {
			getOrCreateChampionEvent(entity);
		} else if (!boss && entity.hasData(Attachments.CHAMPION_EVENT)) {
			entity.removeData(Attachments.CHAMPION_EVENT);
		}
	}

	public static void setPrefix(Entity entity, Component prefix) {
		entity.setData(Attachments.PREFIX, Optional.of(prefix));
	}

	public static void updateFromItemStack(ServerLevel level, Entity entity, ItemStack from) {
		Component prefix = from.get(DataComponents.PREFIX);
		if (prefix != null) {
			setPrefix(entity, prefix);
		}
		int lvl = getLevel(entity);
		int lvl1 = from.getOrDefault(DataComponents.LEVEL, lvl);
		if (lvl1 != lvl) {
			setLevel(entity, lvl);
		}
		int color = getColor(entity);
		int color1 = from.getOrDefault(DataComponents.COLOR, color);
		if (color1 != color) {
			setColor(entity, color);
		}
		AffixContainer container = getAffixContainer(entity);
		AffixContainer container1 = from.getOrDefault(DataComponents.AFFIX_CONTAINER_STORED, AffixContainer.EMPTY);
		if (!Objects.equals(container1, container)) {
			updateAffixContainer(level, entity, mutable -> {
				mutable.clear();
				mutable.addAll(container1.getAffixList());
			});
		}
		if (entity instanceof LivingEntity livingEntity) {
			boolean boss = isBoss(entity);
			boolean boss1 = from.getOrDefault(DataComponents.BOSS, false);
			if (boss1 != boss) {
				setBoss(livingEntity, boss1);
			}
		}

	}

	public static void doFinalizeSpawn(ServerLevel level, Mob mob, double x, double y, double z, DifficultyInstance difficultyInstance, EntitySpawnReason reason) {
		RandomSource random = level.getRandom();
		if (reason != EntitySpawnReason.SPAWN_ITEM_USE && random.nextFloat() < difficultyInstance.getSpecialMultiplier()) {
			int championLevel = ChampionHelper.calculateChampionLevel(level.getRandom(), difficultyInstance);
			setLevel(mob, championLevel);
			List<Holder<Affix>> list = ChampionHelper.selectAffixes(mob, championLevel, level.registryAccess().lookupOrThrow(Registries.AFFIX).listElements());
			AffixContainer.Mutable mutable = getAffixContainer(mob).toMutable();
			list.forEach(mutable::add);
			setAffixContainer(mob, mutable.toImmutable());
		}
	}

	public static void setAffixContainer(Entity entity, AffixContainer container) {
		entity.setData(Attachments.AFFIX_CONTAINER, Optional.of(container));
	}

	public static void updateFromEntity(ServerLevel level, Entity entity, Entity from) {
		Component prefix = getPrefix(entity);
		Component prefix1 = getPrefix(from);
		if (Objects.equals(prefix, prefix1)) {
			setPrefix(entity, prefix1);
		}
		int lvl = getLevel(entity);
		int lvl1 = getLevel(from);
		if (lvl1 != lvl) {
			setLevel(entity, lvl);
		}
		int color = getColor(entity);
		int color1 = getColor(from);
		if (color1 != color) {
			setColor(entity, color);
		}
		AffixContainer container = getAffixContainer(entity);
		AffixContainer container1 = getAffixContainer(from);
		if (!Objects.equals(container1, container)) {
			updateAffixContainer(level, entity, mutable -> {
				mutable.clear();
				mutable.addAll(container1.getAffixList());
			});
		}
		if (entity instanceof LivingEntity livingEntity) {
			boolean boss = isBoss(entity);
			boolean boss1 = isBoss(from);
			if (boss1 != boss) {
				setBoss(livingEntity, boss1);
			}
		}
	}

	public static @Nullable ServerChampionBossEvent getChampionEvent(LivingEntity entity) {
		if (entity.hasData(Attachments.CHAMPION_EVENT) && entity.getData(Attachments.CHAMPION_EVENT).isPresent()) {
			return entity.getData(Attachments.CHAMPION_EVENT).orElseThrow();
		}
		return null;
	}

	public static ServerChampionBossEvent getOrCreateChampionEvent(LivingEntity entity) {
		var event = getChampionEvent(entity);
		if (event == null) {
			event = new ServerChampionBossEvent(Mth.createInsecureUUID(entity.getRandom()), getName(entity), entity.getHealth() / entity.getMaxHealth(), getLevel(entity), getColor(entity), getAffixList(entity));
			entity.setData(Attachments.CHAMPION_EVENT, Optional.of(event));
		}
		return event;
	}

	public static int getColor(Entity entity) {
		return entity.getExistingData(Attachments.COLOR).flatMap(Function.identity()).orElse(byLevelColor(getLevel(entity)));
	}

	public static AffixContainer getAffixContainer(Entity entity) {
		return entity.getExistingData(Attachments.AFFIX_CONTAINER).flatMap(Function.identity()).orElse(AffixContainer.EMPTY);
	}

	public static void runIteration(Entity entity, Consumer<Holder<Affix>> consumer) {
		getAffixContainer(entity).visit(consumer);
	}

	public static int getLevel(Entity entity) {
		return entity.getExistingData(Attachments.LEVEL).flatMap(Function.identity()).orElse(1);
	}

	public static void runLocationChangedEffects(ServerLevel level, Entity entity, Vec3 origin, boolean becameActive) {
		runIteration(entity, affix -> affix.value().runLocationChangedEffects(level, getLevel(entity), entity, origin, becameActive));
	}

	public static void stopLocationChangedEffects(ServerLevel level, Entity entity, Vec3 origin) {
		runIteration(entity, affix -> affix.value().stopLocationChangedEffects(level, getLevel(entity), entity, origin));
	}

	public static void forEachModifier(Entity entity, BiConsumer<Holder<Attribute>, AttributeModifier> action) {
		runIteration(entity, affix -> affix.value().forEachModifier(getLevel(entity), action));
	}

	public static void filterCompatibleAffixes(List<Holder<Affix>> affixes, Holder<Affix> target) {
		affixes.removeIf(e -> !Affix.areCompatible(e, target));
	}

	public static List<Holder<Affix>> getAffixList(Entity entity) {
		return getAffixContainer(entity).getAffixList();
	}

	public static boolean isImmuneToDamage(ServerLevel serverLevel, Entity victim, DamageSource source) {
		LootContext context = createDamageImmunityContext(serverLevel, victim, getLevel(victim), source, null, null, null);
		for (Holder<Affix> affix : getAffixList(victim)) {
			for (ConditionalEffect<DamageImmunity> effect : affix.value().getEffects(AffixEffectComponents.DAMAGE_IMMUNITY)) {
				if (effect.matches(context)) {
					return true;
				}
			}
		}

		return false;
	}

	public static float getDamageProtection(ServerLevel level, Entity victim, DamageSource source) {
		MutableFloat mutableFloat = new MutableFloat(0.0f);
		runIteration(victim, affix -> affix.value().modifyDamageProtection(level, getLevel(victim), victim, source, mutableFloat));
		return mutableFloat.floatValue();
	}

	public static float modifyKnockback(ServerLevel level, Entity victim, DamageSource source, float knockback) {
		MutableFloat mutableFloat = new MutableFloat(knockback);
		runIteration(victim, affix -> affix.value().modifyKnockback(level, getLevel(victim), victim, source, mutableFloat));
		return mutableFloat.floatValue();
	}

	public static float modifyDamage(ServerLevel level, Entity victim, DamageSource source, float amount) {
		MutableFloat mutableFloat = new MutableFloat(amount);
		runIteration(victim, affix -> affix.value().modifyDamage(level, getLevel(victim), victim, source, mutableFloat));
		return mutableFloat.floatValue();
	}

	public static void updateLatestDamage(Entity entity, DamageSource source, float amount) {
		entity.setData(Attachments.DAMAGE_TYPE, Optional.of(source.typeHolder()));
		entity.setData(Attachments.DAMAGE_AMOUNT, Optional.of(amount));
	}

	public static void doParticlesEffects(Entity entity) {
		if (!getAffixContainer(entity).isEmpty()) {
			RandomSource randomSource = entity.getRandom();
			Vec3 position = entity.position();
			double x = position.x() + (randomSource.nextDouble() - 0.5) * entity.getBbWidth();
			double y = position.y() + randomSource.nextDouble() * entity.getBbHeight();
			double z = position.z() + (randomSource.nextDouble() - 0.5) * entity.getBbWidth();
			int color = getColor(entity);
			entity.level().addParticle(ParticleTypes.champion(color), x, y, z, 1.0f, 1.0f, 1.0f);
		}
	}

	public static void updateChampionEvent(ServerLevel level, Entity entity, ChampionEventOperation operation) {
		if (entity instanceof LivingEntity livingEntity) {
			var event = getChampionEvent(livingEntity);
			if (event != null) {
				if (operation == ChampionEventOperation.PROGRESS) {
					event.setProgress(livingEntity.getHealth() / livingEntity.getMaxHealth());
				} else if (operation == ChampionEventOperation.PLAYERS) {
					for (ServerPlayer player : level.players()) {
						if (player.blockPosition().distSqr(entity.blockPosition()) <= 3025.0) {
							event.addPlayer(player);
						} else {
							event.removePlayer(player);
						}
					}
				} else {
					event.removeAllPlayers();
				}
			}
		}
	}

	public static float modifyHeal(ServerLevel level, Entity victim, float amount) {
		MutableFloat mutableFloat = new MutableFloat(amount);
		runIteration(victim, affix -> affix.value().modifyHeal(level, getLevel(victim), victim, mutableFloat));
		return mutableFloat.floatValue();
	}

	public static void doPostAttackEffects(ServerLevel level, Entity victim, DamageSource source) {
		runIteration(victim, affix -> affix.value().doPostAttack(level, getLevel(victim), AffixTarget.VICTIM, victim, source));
		Entity direct = source.getDirectEntity();
		if (direct != null) {
			runIteration(direct, affix -> affix.value().doPostAttack(level, getLevel(direct), AffixTarget.DAMAGING_ENTITY, victim, source));
		}
		Entity attacker = source.getEntity();
		if (attacker != null) {
			runIteration(attacker, affix -> affix.value().doPostAttack(level, getLevel(attacker), AffixTarget.DAMAGING_ENTITY, victim, source));
		}
	}

	public static void tickEffects(ServerLevel level, Entity entity) {
		runIteration(entity, affix -> affix.value().tickEffects(level, getLevel(entity), entity));
	}

	public static void targetEffects(ServerLevel level, Entity entity) {
		if (entity instanceof Mob mob && mob.getTarget() != null) {
			runIteration(entity, affix -> affix.value().targetEffects(level, getLevel(entity), mob, mob.getTarget()));
		}
	}

	private static LootContext createDamageImmunityContext(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, @Nullable Damage damage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
		LootParams params = new LootParams.Builder(serverLevel).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.DAMAGE_SOURCE, damageSource).withParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL, level).withOptionalParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE, damage).withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity).withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity).create(top.theillusivec4.champions.world.loot.parameters.LootContextParamSets.DAMAGE_IMMUNITY);
		return new LootContext.Builder(params).create(Optional.empty());
	}

	public static List<Holder<Affix>> getAvailableAffixResults(Entity entity, Stream<? extends Holder<Affix>> source) {
		List<Holder<Affix>> results = Lists.newArrayList();
		source.filter(affix -> affix.value().isSupportedEntityType(entity.getType())).forEach(results::add);
		return results;
	}

	/**
	 * 计算等级, 区域难度四舍五入：[1, 7], 减半得到基值：[1, 3], 生成不小于该值不超过8的随机数
	 * 最小返回1，最大返回5
	 *
	 * @param random             随机
	 * @param difficultyInstance 难度
	 * @return 等级
	 */
	public static int calculateChampionLevel(RandomSource random, DifficultyInstance difficultyInstance) {
		int originLevel = Math.round(difficultyInstance.getEffectiveDifficulty());
		return Math.min(random.nextInt(Math.max(originLevel / 2, 1), 8), 5);
	}

	public static List<Holder<Affix>> selectAffixes(Entity entity, int championLevel, Stream<? extends Holder<Affix>> source) {
		List<Holder<Affix>> results = Lists.newArrayList();
		List<Holder<Affix>> list = getAvailableAffixResults(entity, source);
		Collections.shuffle(list);

		int i = 0;
		for (Holder<Affix> affix : list) {
			if (i >= championLevel) {
				break;
			} else if (isAffixCompatible(results, affix)) {
				results.add(affix);
				i++;
			}
		}

		return results;
	}

	public static int byLevelColor(int level) {
		return switch (level) {
			case 1 -> TextColor.parseColor("#FFC0CB").getOrThrow().getValue();
			case 2 -> TextColor.parseColor("#FFFF00").getOrThrow().getValue();
			case 3 -> TextColor.parseColor("#FF9900").getOrThrow().getValue();
			case 4 -> TextColor.parseColor("#66FFFF").getOrThrow().getValue();
			case 5 -> TextColor.parseColor("#CC33FF").getOrThrow().getValue();
			default -> Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.WHITE)).getValue();
		};
	}

	@Deprecated
	public static Optional<Holder<Rank>> selectRank(Entity entity, int championLevel, Stream<Holder<Rank>> source) {
		return WeightedRandom.getRandomItem(entity.getRandom(), source.filter(rank1 -> rank1.value().matches(championLevel)).toList(), rank2 -> rank2.value().weight());
	}

	public static Component getPrefixComponent(int level) {
		return LanguageUtil.getPrefixComponent(level);
	}

	public static Component getLevelComponent(int level) {
		return LanguageUtil.getLevelComponent(level);
	}

	public static Component getColorComponent(int color) {
		return LanguageUtil.getColorComponent(color);
	}

	public static boolean isAffixCompatible(Collection<Holder<Affix>> affixes, Holder<Affix> target) {
		for (Holder<Affix> affix : affixes) {
			if (!Affix.areCompatible(affix, target)) {
				return false;
			}
		}

		return true;
	}

	public static void setLevel(ItemStack itemStack, int lvl) {
		if (lvl > 1) {
			itemStack.set(DataComponents.LEVEL, lvl);
		}
	}

	public static void setLevel(Entity entity, int lvl) {
		entity.setData(Attachments.LEVEL, Optional.of(lvl));
	}

	public static void updateAffixContainerStored(ItemStack itemStack, Consumer<AffixContainer.Mutable> updater) {
		AffixContainer.Mutable mutable = itemStack.getOrDefault(DataComponents.AFFIX_CONTAINER_STORED, AffixContainer.EMPTY).toMutable();
		updater.accept(mutable);
		itemStack.set(DataComponents.AFFIX_CONTAINER_STORED, mutable.toImmutable());
	}

	public static void updateAffixContainer(ServerLevel level, Entity entity) {
		updateAffixContainer(level, entity, mutable -> {
		});
	}

	public static void updateAffixContainer(ServerLevel level, Entity entity, Consumer<AffixContainer.Mutable> updater) {
		if (entity instanceof LivingEntity livingEntity) {
			forEachModifier(entity, (attribute, modifier) -> {
				AttributeInstance attributeModifier = livingEntity.getAttribute(attribute);
				if (attributeModifier != null) {
					attributeModifier.removeModifier(modifier);
				}
			});
		}
		stopLocationChangedEffects(level, entity, entity.position());
		AffixContainer.Mutable mutable = getAffixContainer(entity).toMutable();
		updater.accept(mutable);
		AffixContainer affixContainer = mutable.toImmutable();
		entity.setData(Attachments.AFFIX_CONTAINER, Optional.of(affixContainer));
		if (entity instanceof LivingEntity livingEntity) {
			forEachModifier(entity, (attribute, modifier) -> {
				AttributeInstance attributeModifier = livingEntity.getAttribute(attribute);
				if (attributeModifier != null) {
					attributeModifier.addTransientModifier(modifier);
				}
			});
		}
		runLocationChangedEffects(level, entity, entity.position(), true);
	}

	public enum ChampionEventOperation {
		PROGRESS,
		PLAYERS,
		REMOVE_ALL_PLAYERS;
	}
}
