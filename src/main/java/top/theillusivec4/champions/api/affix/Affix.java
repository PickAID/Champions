package top.theillusivec4.champions.api.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.api.affix.effect.*;
import top.theillusivec4.champions.core.attachments.ChampionsAttachments;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.LootContextFactory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public record Affix(
		Component description,
		Affix.AffixDefinition definition,
		HolderSet<Affix> exclusiveSet,
		DataComponentMap effects
) {
	public static final Codec<Affix> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ComponentSerialization.CODEC.fieldOf("description").forGetter(Affix::description),
			AffixDefinition.MAP_CODEC.forGetter(Affix::definition),
			RegistryCodecs.homogeneousList(ChampionsRegistries.AFFIX).optionalFieldOf("exclusive_set", HolderSet.empty()).forGetter(Affix::exclusiveSet),
			AffixEffectComponents.CODEC.fieldOf("effects").forGetter(Affix::effects)
	).apply(instance, Affix::new));
	public static final Codec<Holder<Affix>> REFERENCE_CODEC = RegistryFileCodec.create(ChampionsRegistries.AFFIX, DIRECT_CODEC);
	public static final Codec<HolderSet<Affix>> LIST_CODEC = RegistryCodecs.homogeneousList(ChampionsRegistries.AFFIX, DIRECT_CODEC);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Affix>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ChampionsRegistries.AFFIX);

	public static <T> void applyConditionalEffects(List<ConditionalEffect<T>> effects, LootContext context, Consumer<T> consumer) {
		for (ConditionalEffect<T> effect : effects) {
			effect.apply(context, consumer);
		}
	}

	public static boolean areCompatible(Holder<Affix> affix, Holder<Affix> other) {
		return !affix.value().equals(other.value()) && !affix.value().exclusiveSet.contains(other) && !other.value().exclusiveSet.contains(affix);
	}

	public static boolean areCompatible(AffixInstance affix, AffixInstance other) {
		return areCompatible(affix.affix(), other.affix());
	}

	public static MutableComponent getFullName(Holder<Affix> affix, int level) {
		MutableComponent component = affix.value().description.copy();
		if (level != 1 || affix.value().getMaxLevel() != 1) {
			ComponentUtils.mergeStyles(component, Style.EMPTY.withColor(ChatFormatting.GRAY))
					.append(CommonComponents.SPACE)
					.append(Component.translatable("champions.affix.level." + level));
		}

		return component;
	}

	public static AffixDefinition definition(@Nullable HolderSet<EntityType<?>> supportedEntityTypes, int maxLevel, int weight) {
		return definition(
				supportedEntityTypes,
				maxLevel,
				weight,
				dynamicCost(3, 3),
				dynamicCost(5, 5)
		);
	}

	public static Affix.Cost constantCost(int cost) {
		return new Affix.Cost(cost, 0);
	}

	public static Affix.Cost dynamicCost(int base, int perLevel) {
		return new Affix.Cost(base, perLevel);
	}

	public static AffixDefinition definition(@Nullable HolderSet<EntityType<?>> supportedEntityTypes, int maxLevel, int weight, Affix.Cost minCost, Affix.Cost maxCost) {
		return new AffixDefinition(
				Optional.ofNullable(supportedEntityTypes),
				maxLevel,
				weight,
				minCost,
				maxCost
		);
	}

	public static Affix.Builder affix(AffixDefinition definition) {
		return new Builder(definition);
	}

	public int getMinCost(int level) {
		return this.definition.minCost.calculate(level);
	}

	public int getMaxCost(int level) {
		return this.definition.maxCost.calculate(level);
	}

	public boolean isSupported(Entity entity) {
		return this.isSupported(entity.getType());
	}

	public boolean isSupported(EntityType<?> entityType) {
		return this.definition.supportedEntityTypes.map(holders -> holders.contains(entityType.builtInRegistryHolder())).orElse(true);
	}

	public int getMaxLevel() {
		return this.definition.maxLevel();
	}

	public int getMinLevel() {
		return 1;
	}

	public boolean isSupportedEntityType(EntityType<?> entityType) {
		return this.definition.supportedEntityTypes.isEmpty() || this.definition.supportedEntityTypes.get().contains(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entityType));
	}

	public void modifyKnockback(ServerLevel level, int affixLevel, Entity victim, DamageSource source, MutableFloat knockback) {
		LootContext context = LootContextFactory.affixedDamage(level, victim, source, affixLevel, source.getDirectEntity(), source.getEntity());
		RandomSource random = level.getRandom();
		applyConditionalEffects(this.getEffects(AffixEffectComponents.KNOCKBACK), context, effect -> knockback.setValue(effect.process(affixLevel, random, knockback.floatValue())));
	}

	public void modifyDamage(ServerLevel level, int affixLevel, Entity victim, DamageSource source, MutableFloat damage) {
		LootContext context = LootContextFactory.affixedDamage(level, victim, source, affixLevel, source.getDirectEntity(), source.getEntity());
		RandomSource random = level.getRandom();
		applyConditionalEffects(this.getEffects(AffixEffectComponents.DAMAGE), context, effect -> damage.setValue(effect.process(affixLevel, random, damage.floatValue())));
	}

	public void forEachModifier(int affixLevel, BiConsumer<Holder<Attribute>, AttributeModifier> action) {
		for (AffixAttributeEffect effect : this.getEffects(AffixEffectComponents.ATTRIBUTES)) {
			action.accept(effect.attribute(), effect.getModifier(affixLevel));
		}
	}

	public void runLocationChangedEffects(ServerLevel level, int affixLevel, Entity entity) {
		Reference2ObjectArrayMap<Affix, ObjectArraySet<AffixLocationBasedEffect>> map = entity.getData(ChampionsAttachments.ACTIVE_LOCATION_DEPENDENTS_EFFECTS);
		ObjectArraySet<AffixLocationBasedEffect> set = map.get(this);

		for (ConditionalEffect<AffixLocationBasedEffect> effect : this.getEffects(AffixEffectComponents.LOCATION_CHANGED)) {
			AffixLocationBasedEffect locationBasedEffect = effect.effect();
			boolean flag = set != null && set.contains(locationBasedEffect);
			if (effect.matches(LootContextFactory.affixedEntity(level, entity, affixLevel))) {
				if (!flag) {
					if (set == null) {
						set = new ObjectArraySet<>();
						map.put(this, set);
					}

					set.add(locationBasedEffect);
				}
				locationBasedEffect.onChangedBlock(level, affixLevel, entity, entity.position(), !flag);
			} else if (set != null && set.remove(locationBasedEffect)) {
				locationBasedEffect.onDeactivated(level, affixLevel, entity, entity.position());
			}
		}

		if (set != null && set.isEmpty()) {
			map.remove(this);
		}
	}

	public void stopLocationChangedEffects(ServerLevel level, int affixLevel, Entity entity) {
		Reference2ObjectMap<Affix, ObjectArraySet<AffixLocationBasedEffect>> map = entity.getData(ChampionsAttachments.ACTIVE_LOCATION_DEPENDENTS_EFFECTS);
		ObjectArraySet<AffixLocationBasedEffect> set = map.get(this);
		if (set != null) {
			for (AffixLocationBasedEffect effect : set) {
				effect.onDeactivated(level, affixLevel, entity, entity.position());
			}
		}
	}

	public void modifyHeal(ServerLevel level, int affixLevel, Entity victim, MutableFloat heal) {
		LootContext lootContext = LootContextFactory.affixedEntity(level, victim, affixLevel);
		RandomSource random = level.getRandom();
		applyConditionalEffects(this.getEffects(AffixEffectComponents.HEAL), lootContext, effect -> heal.setValue(effect.process(affixLevel, random, heal.floatValue())));

	}

	public void doPostAttack(ServerLevel level, int affixLevel, AffixTarget targetType, Entity victim, DamageSource source) {
		for (TargetedConditionalEffect<AffixEntityEffect> effect : this.getEffects(AffixEffectComponents.POST_ATTACK)) {
			if (targetType == effect.trigger()) {
				Entity target = switch (effect.target()) {
					case ATTACKER -> source.getEntity();
					case DAMAGING_ENTITY -> source.getDirectEntity();
					case VICTIM -> victim;
				};

				if (target != null) {
					LootContext lootContext = LootContextFactory.affixedDamage(level, target, source, affixLevel, source.getEntity(), source.getDirectEntity());

					if (effect.match(lootContext)) {
						effect.effect().apply(level, affixLevel, victim, target, target.position());
					}
				}
			}

		}
	}

	public void targetEffects(ServerLevel level, int affixLevel, Entity entity, Entity target) {
		LootContext context = LootContextFactory.affixedEntity(level, entity, affixLevel);
		applyConditionalEffects(this.getEffects(AffixEffectComponents.TARGET), context, effect -> effect.apply(level, affixLevel, entity, target, entity.position()));
	}

	public void tickEffects(ServerLevel level, int affixLevel, Entity entity) {
		LootContext context = LootContextFactory.affixedEntity(level, entity, affixLevel);
		applyConditionalEffects(this.getEffects(AffixEffectComponents.TICK), context, effect -> effect.apply(level, affixLevel, entity, entity, entity.position()));
	}

	public boolean isImmuneToDamage(ServerLevel level, int affixLevel, Entity entity, DamageSource source) {
		LootContext context = LootContextFactory.affixedDamage(level, entity, source, affixLevel, null, null);
		for (ConditionalEffect<DamageImmunity> effect : this.getEffects(AffixEffectComponents.DAMAGE_IMMUNITY)) {
			if (effect.matches(context)) {
				return true;
			}
		}
		return false;
	}

	public void modifyDamageProtection(ServerLevel level, int affixLevel, Entity victim, DamageSource source, MutableFloat protection) {
		LootContext context = LootContextFactory.affixedDamage(level, victim, source, affixLevel, source.getDirectEntity(), source.getEntity());
		RandomSource random = level.getRandom();
		applyConditionalEffects(this.getEffects(AffixEffectComponents.DAMAGE_PROTECTION), context, effect -> protection.setValue(effect.process(affixLevel, random, protection.floatValue())));
	}

	public <T> Optional<T> getSpecialEffect(Supplier<DataComponentType<T>> effectComponent) {
		return getSpecialEffect(effectComponent.get());
	}

	public <T> Optional<T> getSpecialEffect(DataComponentType<T> effectComponent) {
		return Optional.ofNullable(effects.get(effectComponent));
	}

	public <T> List<T> getEffects(Supplier<DataComponentType<List<T>>> effectComponent) {
		return getEffects(effectComponent.get());
	}

	public <T> List<T> getEffects(DataComponentType<List<T>> effectComponent) {
		return effects.getOrDefault(effectComponent, List.of());
	}

	public record AffixDefinition(
			Optional<HolderSet<EntityType<?>>> supportedEntityTypes,
			int maxLevel,
			int weight,
			Cost minCost,
			Cost maxCost
	) {
		public static final MapCodec<AffixDefinition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("supported_entity_types").forGetter(AffixDefinition::supportedEntityTypes),
				Codec.intRange(1, 5).optionalFieldOf("max_level", 5).forGetter(AffixDefinition::maxLevel),
				Codec.intRange(1, 1024).optionalFieldOf("weight", 5).forGetter(AffixDefinition::weight),
				Cost.CODEC.fieldOf("min_cost").forGetter(AffixDefinition::minCost),
				Cost.CODEC.fieldOf("max_cost").forGetter(AffixDefinition::maxCost)
		).apply(instance, AffixDefinition::new));
	}

	public record Cost(int base, int perLevelAboveFirst) {
		public static final Codec<Cost> CODEC = RecordCodecBuilder.create(
				p_345979_ -> p_345979_.group(
								Codec.INT.fieldOf("base").forGetter(Cost::base),
								Codec.INT.fieldOf("per_level_above_first").forGetter(Cost::perLevelAboveFirst)
						)
						.apply(p_345979_, Cost::new)
		);

		public int calculate(int level) {
			return this.base + this.perLevelAboveFirst * (level - 1);
		}
	}

	public static class Builder {
		private final AffixDefinition definition;
		private final DataComponentMap.Builder builder = DataComponentMap.builder();
		private final Map<DataComponentType<?>, List<?>> effects = new HashMap<>();
		private HolderSet<Affix> exclusiveSet = HolderSet.empty();
		private UnaryOperator<MutableComponent> nameFactory = UnaryOperator.identity();

		private Builder(AffixDefinition definition) {
			this.definition = definition;
		}

		public Builder exclusiveWith(HolderSet<Affix> set) {
			this.exclusiveSet = set;
			return this;
		}

		public Builder withCustomName(UnaryOperator<MutableComponent> nameFactory) {
			this.nameFactory = nameFactory;
			return this;
		}

		public <E> Builder withSpecialEffect(DataComponentType<E> effectComponent, E effect) {
			this.builder.set(effectComponent, effect);
			return this;
		}

		public Builder withEffect(Supplier<DataComponentType<List<AffixAttributeEffect>>> effectComponent, AffixAttributeEffect effect) {
			return this.withEffect(effectComponent.get(), effect);
		}

		public Builder withEffect(DataComponentType<List<AffixAttributeEffect>> effectComponent, AffixAttributeEffect effect) {
			this.getEffects(effectComponent).add(effect);
			return this;
		}

		public <E> Builder withEffect(Supplier<DataComponentType<List<TargetedConditionalEffect<E>>>> effectComponent, AffixTarget enchanted, AffixTarget affected, E effect) {
			return this.withEffect(effectComponent.get(), enchanted, affected, effect);
		}

		public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> effectComponent, AffixTarget enchanted, AffixTarget affected, E effect) {
			this.getEffects(effectComponent).add(TargetedConditionalEffect.create(enchanted, affected, effect));
			return this;
		}

		public <E> Builder withEffect(Supplier<DataComponentType<List<TargetedConditionalEffect<E>>>> effectComponent, AffixTarget enchanted, AffixTarget affected, E effect, LootItemCondition.Builder builder) {
			return this.withEffect(effectComponent.get(), enchanted, affected, effect, builder);
		}

		public <E> Builder withEffect(DataComponentType<List<TargetedConditionalEffect<E>>> effectComponent, AffixTarget enchanted, AffixTarget affected, E effect, LootItemCondition.Builder builder) {
			this.getEffects(effectComponent).add(TargetedConditionalEffect.create(enchanted, affected, effect, builder));
			return this;
		}

		public <E> Builder withEffect(Supplier<DataComponentType<List<ConditionalEffect<E>>>> effectComponent, E effect, LootItemCondition.Builder builder) {
			return this.withEffect(effectComponent.get(), effect, builder);
		}

		public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> effectComponent, E effect, LootItemCondition.Builder builder) {
			this.getEffects(effectComponent).add(ConditionalEffect.create(effect, builder));
			return this;
		}

		public <E> Builder withEffect(Supplier<DataComponentType<List<ConditionalEffect<E>>>> effectComponent, E effect) {
			return this.withEffect(effectComponent.get(), effect);
		}

		public <E> Builder withEffect(DataComponentType<List<ConditionalEffect<E>>> effectComponent, E effect) {
			this.getEffects(effectComponent).add(ConditionalEffect.create(effect));
			return this;
		}

		public Affix build(Identifier id) {
			return new Affix(
					this.nameFactory.apply(Component.translatable(Util.makeDescriptionId("affix", id))),
					this.definition,
					this.exclusiveSet,
					this.builder.build()
			);
		}

		private <E> List<E> getEffects(DataComponentType<List<E>> effectComponent) {
			//noinspection unchecked
			return (List<E>) this.effects.computeIfAbsent(effectComponent, type -> {
				List<E> list = new ArrayList<>();
				builder.set(effectComponent, list);
				return list;
			});
		}
	}
}
