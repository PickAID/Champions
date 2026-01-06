package top.theillusivec4.champions.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.champions.affix.effect.*;
import top.theillusivec4.champions.affix.effect.entity.AffixEntityEffect;
import top.theillusivec4.champions.loot.parameters.LootContextParamSets;
import top.theillusivec4.champions.registries.Registries;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public record Affix(Component description, DataComponentMap effects) {
  public static final Codec<Affix> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("description").forGetter(Affix::description),
    AffixEffectComponents.CODEC.fieldOf("effects").forGetter(Affix::effects)
  ).apply(instance, Affix::new));
  public static final Codec<Holder<Affix>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.AFFIX, DIRECT_CODEC);
  public static final Codec<HolderSet<Affix>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.AFFIX, DIRECT_CODEC);
  public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Affix>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.AFFIX);

  public static <T> void applyConditionalEffects(List<ConditionalEffect<T>> effects, LootContext context, Consumer<T> consumer) {
    for (ConditionalEffect<T> effect : effects) {
      effect.apply(context, consumer);
    }
  }

  public static Affix.Builder builder() {
    return new Builder();
  }

  public void modifyKnockback(ServerLevel serverLevel, int level, Entity victim, DamageSource source, LatestDamage latestDamage, MutableFloat knockback) {
    LootContext context = LootContextParamSets.knockback(serverLevel, victim, level, source, latestDamage, source.getDirectEntity(), source.getEntity());
    applyConditionalEffects(this.getEffects(AffixEffectComponents.KNOCKBACK), context, effect -> knockback.setValue(effect.process(context, level, knockback.floatValue())));
  }

  public void modifyDamage(ServerLevel serverLevel, int level, Entity victim, DamageSource source, LatestDamage latestDamage, MutableFloat damage) {
    LootContext context = LootContextParamSets.damage(serverLevel, victim, level, source, latestDamage, source.getDirectEntity(), source.getEntity());
    applyConditionalEffects(this.getEffects(AffixEffectComponents.DAMAGE), context, effect -> damage.setValue(effect.process(context, level, damage.floatValue())));

//    LootContext lootContext = LootContextParamSets.damage(serverLevel, victim, level, source, latestDamage, source.getEntity(), source.getDirectEntity());
//
//    for (TargetedConditionalEffect<AffixValueEffect> effect : this.getEffects(AffixEffectComponentTypes.DAMAGE)) {
//      if (target == effect.enchanted()) {
//        if (effect.match(lootContext)) {
//          damage.setValue(effect.effect().process(lootContext, level, damage.floatValue()));
//        }
//      }
//    }
  }

  public void runInitialize(ServerLevel serverLevel, int level, Entity victim, Vec3 origin) {
    LootContext lootContext = LootContextParamSets.location(serverLevel, victim, level, origin);
    for (AffixLocationBasedEffect effect : this.getEffects(AffixEffectComponents.INITIALIZE)) {
      effect.onChangedBlock(lootContext, level, victim, origin, true);
    }
  }

  public void stopInitialized(ServerLevel serverLevel, int level, Entity victim, Vec3 origin) {
    LootContext lootContext = LootContextParamSets.location(serverLevel, victim, level, origin);
    for (AffixLocationBasedEffect effect : this.getEffects(AffixEffectComponents.INITIALIZE)) {
      effect.onDeactivated(lootContext, level, victim, origin);
    }
  }

  public void modifyHeal(ServerLevel serverLevel, int level, Entity victim, LatestDamage latestDamage, MutableFloat heal) {
    LootContext lootContext = LootContextParamSets.heal(serverLevel, victim, level, latestDamage);
    applyConditionalEffects(this.getEffects(AffixEffectComponents.HEAL), lootContext, effect -> heal.setValue(effect.process(lootContext, level, heal.floatValue())));
  }

  public void doPostAttack(ServerLevel serverLevel, int level, AffixTarget target, Entity victim, DamageSource source, LatestDamage latestDamage) {
    for (TargetedConditionalEffect<AffixEntityEffect> effect : this.getEffects(AffixEffectComponents.POST_ATTACK)) {
      if (target == effect.enchanted()) {
        Entity targetEntity = switch (effect.affected()) {
          case ATTACKER -> source.getEntity();
          case DAMAGING_ENTITY -> source.getDirectEntity();
          case VICTIM -> victim;
        };

        if (targetEntity != null) {
          LootContext lootContext = LootContextParamSets.postAttack(serverLevel, targetEntity, level, source, latestDamage, source.getEntity(), source.getDirectEntity());

          if (effect.match(lootContext)) {
            effect.effect().apply(lootContext, level, targetEntity, targetEntity.position());
          }
        }
      }

    }
  }

  public void tick(ServerLevel serverLevel, int level, Entity entity, LatestDamage latestDamage) {
    LootContext context = LootContextParamSets.tick(serverLevel, entity, level, latestDamage);
    applyConditionalEffects(this.getEffects(AffixEffectComponents.TICK), context, effect -> effect.apply(context, level, entity, entity.position()));
  }

  public void modifyDamageProtection(ServerLevel serverLevel, int level, Entity victim, DamageSource source, LatestDamage latestDamage, MutableFloat protection) {
    LootContext context = LootContextParamSets.damageProtection(serverLevel, victim, level, source, latestDamage, source.getDirectEntity(), source.getEntity());
    applyConditionalEffects(this.getEffects(AffixEffectComponents.DAMAGE_PROTECTION), context, effect -> protection.setValue(effect.process(context, level, protection.floatValue())));
  }

  public <T> Optional<T> getSpecialEffect(Supplier<DataComponentType<T>> componentTypeSupplier) {
    return getSpecialEffect(componentTypeSupplier.get());
  }

  public <T> Optional<T> getSpecialEffect(DataComponentType<T> dataComponentType) {
    return Optional.ofNullable(effects.get(dataComponentType));
  }

  public <T> List<T> getEffects(Supplier<DataComponentType<List<T>>> dataComponentTypeSupplier) {
    return getEffects(dataComponentTypeSupplier.get());
  }

  public <T> List<T> getEffects(DataComponentType<List<T>> dataComponentType) {
    return effects.getOrDefault(dataComponentType, List.of());
  }

  public static class Builder {
    private final DataComponentMap.Builder builder = DataComponentMap.builder();
    private final Map<DataComponentType<?>, List<?>> effects = new HashMap<>();
    private UnaryOperator<MutableComponent> nameFactory = UnaryOperator.identity();

    private Builder() {
    }

    public Builder withCustomName(UnaryOperator<MutableComponent> nameFactory) {
      this.nameFactory = nameFactory;
      return this;
    }

    public <E> Builder withSpecialEffect(DataComponentType<E> dataComponentType, E effect) {
      this.builder.set(dataComponentType, effect);
      return this;
    }

    public <E> Builder withEffects(Supplier<DataComponentType<List<E>>> dataComponentTypeSupplier, E effect) {
      return this.withEffects(dataComponentTypeSupplier.get(), effect);
    }

    public <E> Builder withEffects(DataComponentType<List<E>> dataComponentType, E effect) {
      this.getEffects(dataComponentType).add(effect);
      return this;
    }

    public <E> Builder withTargetedConditionalEffects(Supplier<DataComponentType<List<TargetedConditionalEffect<E>>>> dataComponentTypeSupplier, AffixTarget enchanted, AffixTarget affected, E effect) {
      return this.withTargetedConditionalEffects(dataComponentTypeSupplier.get(), enchanted, affected, effect);
    }

    public <E> Builder withTargetedConditionalEffects(DataComponentType<List<TargetedConditionalEffect<E>>> dataComponentType, AffixTarget enchanted, AffixTarget affected, E effect) {
      this.getEffects(dataComponentType).add(TargetedConditionalEffect.create(enchanted, affected, effect));
      return this;
    }

    public <E> Builder withEnchantedTargetedConditionalEffects(Supplier<DataComponentType<List<TargetedConditionalEffect<E>>>> dataComponentTypeSupplier, AffixTarget enchanted, E effect, LootItemCondition.Builder builder) {
      return this.withTargetedConditionalEffects(dataComponentTypeSupplier.get(), enchanted, AffixTarget.VICTIM, effect, builder);
    }

    public <E> Builder withTargetedConditionalEffects(Supplier<DataComponentType<List<TargetedConditionalEffect<E>>>> dataComponentTypeSupplier, AffixTarget enchanted, AffixTarget affected, E effect, LootItemCondition.Builder builder) {
      return this.withTargetedConditionalEffects(dataComponentTypeSupplier.get(), enchanted, affected, effect, builder);
    }

    public <E> Builder withTargetedConditionalEffects(DataComponentType<List<TargetedConditionalEffect<E>>> dataComponentType, AffixTarget enchanted, AffixTarget affected, E effect, LootItemCondition.Builder builder) {
      this.getEffects(dataComponentType).add(TargetedConditionalEffect.create(enchanted, affected, effect, builder));
      return this;
    }

    public <E> Builder withConditionalEffects(Supplier<DataComponentType<List<ConditionalEffect<E>>>> dataComponentTypeSupplier, E effect, LootItemCondition.Builder builder) {
      return this.withConditionalEffects(dataComponentTypeSupplier.get(), effect, builder);
    }

    public <E> Builder withConditionalEffects(DataComponentType<List<ConditionalEffect<E>>> dataComponentType, E effect, LootItemCondition.Builder builder) {
      this.getEffects(dataComponentType).add(ConditionalEffect.create(effect, builder));
      return this;
    }

    public <E> Builder withConditionalEffects(Supplier<DataComponentType<List<ConditionalEffect<E>>>> dataComponentTypeSupplier, E effect) {
      return this.withConditionalEffects(dataComponentTypeSupplier.get(), effect);
    }

    public <E> Builder withConditionalEffects(DataComponentType<List<ConditionalEffect<E>>> dataComponentType, E effect) {
      this.getEffects(dataComponentType).add(ConditionalEffect.create(effect));
      return this;
    }

    public Affix build(Identifier identifier) {
      return new Affix(
        this.nameFactory.apply(Component.translatable(Util.makeDescriptionId("affix", identifier))),
        this.builder.build()
      );
    }

    private <E> List<E> getEffects(DataComponentType<List<E>> dataComponentType) {
      //noinspection unchecked
      return (List<E>) this.effects.computeIfAbsent(dataComponentType, dataComponentType1 -> {
        List<E> list = new ArrayList<>();
        builder.set(dataComponentType, list);
        return list;
      });
    }
  }
}
