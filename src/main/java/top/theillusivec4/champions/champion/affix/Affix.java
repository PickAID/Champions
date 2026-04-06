package top.theillusivec4.champions.champion.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.champion.affix.effect.*;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.world.loot.parameters.ChampionsLootContextParamSets;
import top.theillusivec4.champions.world.loot.parameters.ChampionsLootContextParams;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public record Affix(Component description, Affix.AffixDefinition definition, HolderSet<Affix> exclusiveSet, DataComponentMap effects) {
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

  public static AffixDefinition definition(@Nullable HolderSet<EntityType<?>> supportedEntityTypes, int maxLevel, int weight) {
    return new AffixDefinition(
      Optional.ofNullable(supportedEntityTypes),
      maxLevel,
      weight
    );
  }

  public static Affix.Builder affix(AffixDefinition definition) {
    return new Builder(definition);
  }

  public boolean isSupportedEntityType(EntityType<?> entityType) {
    return this.definition.supportedEntityTypes.isEmpty() || this.definition.supportedEntityTypes.get().contains(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entityType));
  }

  public void modifyKnockback(ServerLevel level, int affixLevel, Entity victim, DamageSource source, MutableFloat knockback) {
    LootContext context = ChampionsLootContextParamSets.knockback(level, victim, affixLevel, source, null, source.getDirectEntity(), source.getEntity());
    RandomSource random = level.getRandom();
    applyConditionalEffects(this.getEffects(AffixEffectComponents.KNOCKBACK), context, effect -> knockback.setValue(effect.process(affixLevel, random, knockback.floatValue())));
  }

  public void modifyDamage(ServerLevel level, int affixLevel, Entity victim, DamageSource source, MutableFloat damage) {
    LootContext context = ChampionsLootContextParamSets.damage(level, victim, affixLevel, source, null, source.getDirectEntity(), source.getEntity());
    RandomSource random = level.getRandom();
    applyConditionalEffects(this.getEffects(AffixEffectComponents.DAMAGE), context, effect -> damage.setValue(effect.process(affixLevel, random, damage.floatValue())));
  }

  public void forEachModifier(int affixLevel, BiConsumer<Holder<Attribute>, AttributeModifier> action) {
    for (AffixAttributeEffect effect : this.getEffects(AffixEffectComponents.ATTRIBUTES)) {
      action.accept(effect.attribute(), effect.getModifier(affixLevel));
    }
  }

  public void runLocationChangedEffects(ServerLevel level, int affixLevel, Entity entity, Vec3 origin, boolean becameActive) {
    for (AffixLocationBasedEffect effect : this.getEffects(AffixEffectComponents.INITIALIZE)) {
      effect.onChangedBlock(level, affixLevel, entity, origin, becameActive);
    }
  }

  public void stopLocationChangedEffects(ServerLevel level, int affixLevel, Entity entity, Vec3 origin) {
    for (AffixLocationBasedEffect effect : this.getEffects(AffixEffectComponents.INITIALIZE)) {
      effect.onDeactivated(level, affixLevel, entity, origin);
    }
  }

  public void modifyHeal(ServerLevel level, int affixLevel, Entity victim, MutableFloat heal) {
    LootContext lootContext = ChampionsLootContextParamSets.heal(level, victim, affixLevel, null);
    RandomSource random = level.getRandom();
    applyConditionalEffects(this.getEffects(AffixEffectComponents.HEAL), lootContext, effect -> heal.setValue(effect.process(affixLevel, random, heal.floatValue())));

  }

  public void doPostAttack(ServerLevel level, int affixLevel, AffixTarget targetType, Entity victim, DamageSource source) {
    for (TargetedConditionalEffect<AffixEntityEffect> effect : this.getEffects(AffixEffectComponents.POST_ATTACK)) {
      if (targetType == effect.enchanted()) {
        Entity target = switch (effect.affected()) {
          case ATTACKER -> source.getEntity();
          case DAMAGING_ENTITY -> source.getDirectEntity();
          case VICTIM -> victim;
        };

        if (target != null) {
          LootContext lootContext = ChampionsLootContextParamSets.postAttack(level, target, affixLevel, source, null, source.getEntity(), source.getDirectEntity());

          if (effect.match(lootContext)) {
            effect.effect().apply(level, affixLevel, victim, target, target.position());
          }
        }
      }

    }
  }

  public void targetEffects(ServerLevel level, int affixLevel, Entity entity, Entity target) {
    LootParams params = new LootParams.Builder(level)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, target.position())
      .withParameter(ChampionsLootContextParams.CHAMPION_LEVEL, affixLevel)
      .create(ChampionsLootContextParamSets.LOCATION);
    LootContext context = new LootContext.Builder(params).create(Optional.empty());
    applyConditionalEffects(this.getEffects(AffixEffectComponents.TARGET), context, effect -> effect.apply(level, affixLevel, entity, target, entity.position()));
  }

  public void tickEffects(ServerLevel level, int affixLevel, Entity entity) {
    LootContext context = ChampionsLootContextParamSets.tick(level, entity, affixLevel, null);
    applyConditionalEffects(this.getEffects(AffixEffectComponents.TICK), context, effect -> effect.apply(level, affixLevel, entity, entity, entity.position()));
  }

  public void modifyDamageProtection(ServerLevel level, int affixLevel, Entity victim, DamageSource source, MutableFloat protection) {
    LootContext context = ChampionsLootContextParamSets.damageProtection(level, victim, affixLevel, source, null, source.getDirectEntity(), source.getEntity());
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
    int weight
  ) {
    public static final MapCodec<AffixDefinition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.ENTITY_TYPE).optionalFieldOf("supported_entity_types").forGetter(AffixDefinition::supportedEntityTypes),
      Codec.intRange(1, 5).optionalFieldOf("max_level", 5).forGetter(AffixDefinition::maxLevel),
      Codec.intRange(1, 1024).optionalFieldOf("weight", 5).forGetter(AffixDefinition::weight)
    ).apply(instance, AffixDefinition::new));
  }

  @SuppressWarnings("unused")
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

    public <E> Builder withEffects(Supplier<DataComponentType<List<E>>> effectComponent, E effect) {
      return this.withEffects(effectComponent.get(), effect);
    }

    public <E> Builder withEffects(DataComponentType<List<E>> effectComponent, E effect) {
      this.getEffects(effectComponent).add(effect);
      return this;
    }

    public <E> Builder withTargetedConditionalEffects(Supplier<DataComponentType<List<TargetedConditionalEffect<E>>>> effectComponent, AffixTarget enchanted, AffixTarget affected, E effect) {
      return this.withTargetedConditionalEffects(effectComponent.get(), enchanted, affected, effect);
    }

    public <E> Builder withTargetedConditionalEffects(DataComponentType<List<TargetedConditionalEffect<E>>> effectComponent, AffixTarget enchanted, AffixTarget affected, E effect) {
      this.getEffects(effectComponent).add(TargetedConditionalEffect.create(enchanted, affected, effect));
      return this;
    }

    public <E> Builder withTargetedConditionalEffects(Supplier<DataComponentType<List<TargetedConditionalEffect<E>>>> effectComponent, AffixTarget enchanted, AffixTarget affected, E effect, LootItemCondition.Builder builder) {
      return this.withTargetedConditionalEffects(effectComponent.get(), enchanted, affected, effect, builder);
    }

    public <E> Builder withTargetedConditionalEffects(DataComponentType<List<TargetedConditionalEffect<E>>> effectComponent, AffixTarget enchanted, AffixTarget affected, E effect, LootItemCondition.Builder builder) {
      this.getEffects(effectComponent).add(TargetedConditionalEffect.create(enchanted, affected, effect, builder));
      return this;
    }

    public <E> Builder withConditionalEffects(Supplier<DataComponentType<List<ConditionalEffect<E>>>> effectComponent, E effect, LootItemCondition.Builder builder) {
      return this.withConditionalEffects(effectComponent.get(), effect, builder);
    }

    public <E> Builder withConditionalEffects(DataComponentType<List<ConditionalEffect<E>>> effectComponent, E effect, LootItemCondition.Builder builder) {
      this.getEffects(effectComponent).add(ConditionalEffect.create(effect, builder));
      return this;
    }

    public <E> Builder withConditionalEffects(Supplier<DataComponentType<List<ConditionalEffect<E>>>> effectComponent, E effect) {
      return this.withConditionalEffects(effectComponent.get(), effect);
    }

    public <E> Builder withConditionalEffects(DataComponentType<List<ConditionalEffect<E>>> effectComponent, E effect) {
      this.getEffects(effectComponent).add(ConditionalEffect.create(effect));
      return this;
    }

    public Affix build(Identifier identifier) {
      return new Affix(
        this.nameFactory.apply(Component.translatable(Util.makeDescriptionId("affix", identifier))),
        this.definition,
        this.exclusiveSet,
        this.builder.build()
      );
    }

    private <E> List<E> getEffects(DataComponentType<List<E>> effectComponent) {
      //noinspection unchecked
      return (List<E>) this.effects.computeIfAbsent(effectComponent, _ -> {
        List<E> list = new ArrayList<>();
        builder.set(effectComponent, list);
        return list;
      });
    }
  }
}
