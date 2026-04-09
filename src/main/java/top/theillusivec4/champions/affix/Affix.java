package top.theillusivec4.champions.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.TargetedConditionalEffect;
import net.minecraft.world.item.enchantment.effects.DamageImmunity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.champions.affix.effects.*;
import top.theillusivec4.champions.loot.ChampionsLootContextParamSets;
import top.theillusivec4.champions.loot.ChampionsLootContextParams;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.ChampionsUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record Affix(
  Component description,
  DataComponentMap effects
) {
  public static final Codec<Affix> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("description").forGetter(Affix::description),
    AffixEffectComponents.CODEC.fieldOf("effects").forGetter(Affix::effects)
  ).apply(instance, Affix::new));
  public static final Codec<Holder<Affix>> REFERENCE_CODEC = RegistryFileCodec.create(ChampionsRegistries.Keys.AFFIX, DIRECT_CODEC, false);
  public static final Codec<HolderSet<Affix>> LIST_CODEC = RegistryCodecs.homogeneousList(ChampionsRegistries.Keys.AFFIX, DIRECT_CODEC);

  public static <T> void applyConditionalEffects(List<ConditionalEffect<T>> effects, LootContext context, Consumer<T> consumer) {
    effects.forEach(effect -> effect.apply(context, consumer));
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

  public boolean isImmuneToDamage(ServerLevel level, int affixLevel, Entity entity, DamageSource source) {
    LootContext context = ChampionsUtil.createDamageImmunityContext(level, entity, affixLevel, source, null, null, null);
    for (ConditionalEffect<DamageImmunity> effect : this.getEffects(AffixEffectComponents.DAMAGE_IMMUNITY)) {
      if (effect.matches(context)) {
        return true;
      }
    }
    return false;
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
}
