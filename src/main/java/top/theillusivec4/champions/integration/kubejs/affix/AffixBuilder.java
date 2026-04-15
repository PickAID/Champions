package top.theillusivec4.champions.integration.kubejs.affix;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.util.ChampionsUtil;
import top.theillusivec4.champions.world.entity.affix.Affix;
import top.theillusivec4.champions.world.entity.affix.AffixEffectComponents;
import top.theillusivec4.champions.world.entity.affix.TargetedConditionalEffect;
import top.theillusivec4.champions.world.entity.affix.effects.*;

import java.util.*;
import java.util.function.UnaryOperator;

public class AffixBuilder extends BuilderBase<Affix> {
  private final UnaryOperator<Affix.AffixDefinition.Builder> definitionFactory = UnaryOperator.identity();
  private final DataComponentMap.Builder builder = DataComponentMap.builder();
  private final Map<DataComponentType<?>, List<?>> effects = new HashMap<>();
  private HolderSet<Affix> exclusiveSet = HolderSet.empty();
  private UnaryOperator<MutableComponent> nameFactory = UnaryOperator.identity();

  public AffixBuilder(ResourceLocation id) {
    super(id);
  }

  public AffixBuilder exclusiveWith(HolderSet<Affix> exclusiveSet) {
    this.exclusiveSet = exclusiveSet;
    return this;
  }

  public AffixBuilder withCustomName(UnaryOperator<MutableComponent> nameFactory) {
    this.nameFactory = nameFactory;
    return this;
  }

  public AffixBuilder attribute(AffixAttributeEffect effect) {
    getEffects(AffixEffectComponents.ATTRIBUTES.get()).add(effect);
    return this;
  }

  public AffixBuilder locationChanged(AffixLocationBasedEffect effect) {
    getEffects(AffixEffectComponents.LOCATION_CHANGED.get()).add(effect);
    return this;
  }

  public AffixBuilder damageProtection(AffixValueEffect effect, @Nullable LootItemCondition.Builder condition) {
    getEffects(AffixEffectComponents.DAMAGE_PROTECTION.get()).add(ConditionalEffect.create(effect, condition));
    return this;
  }

  public AffixBuilder knockback(AffixValueEffect effect, @Nullable LootItemCondition.Builder condition) {
    Objects.requireNonNull(condition);
    getEffects(AffixEffectComponents.KNOCKBACK.get()).add(ConditionalEffect.create(effect, condition));
    return this;
  }

  public AffixBuilder damage(AffixValueEffect effect, @Nullable LootItemCondition.Builder condition) {
    Objects.requireNonNull(condition);
    getEffects(AffixEffectComponents.DAMAGE.get()).add(ConditionalEffect.create(effect, condition));
    return this;
  }

  public AffixBuilder heal(AffixValueEffect effect, @Nullable LootItemCondition.Builder condition) {
    Objects.requireNonNull(condition);
    getEffects(AffixEffectComponents.HEAL.get()).add(ConditionalEffect.create(effect, condition));
    return this;
  }

  public AffixBuilder postAttack(AffixEntityEffect effect, AffixTarget enchanted, AffixTarget affected, @Nullable LootItemCondition.Builder condition) {
    Objects.requireNonNull(condition);
    getEffects(AffixEffectComponents.POST_ATTACK.get()).add(TargetedConditionalEffect.create(enchanted, affected, effect, condition));
    return this;
  }

  public AffixBuilder tick(AffixEntityEffect effect, @Nullable LootItemCondition.Builder condition) {
    Objects.requireNonNull(condition);
    getEffects(AffixEffectComponents.TICK.get()).add(ConditionalEffect.create(effect, condition));
    return this;
  }

  public AffixBuilder target(AffixEntityEffect effect, @Nullable LootItemCondition.Builder condition) {
    Objects.requireNonNull(condition);
    getEffects(AffixEffectComponents.TARGET.get()).add(ConditionalEffect.create(effect, condition));
    return this;
  }

  @HideFromJS
  private <E> List<E> getEffects(DataComponentType<List<E>> effectComponent) {
    //noinspection unchecked
    return (List<E>) this.effects.computeIfAbsent(effectComponent, type -> {
      List<E> list = new ArrayList<>();
      builder.set(effectComponent, list);
      return list;
    });
  }

  @Override
  public Affix createObject() {
    return new Affix(
      nameFactory.apply(Component.translatable(ChampionsUtil.makeDescriptionId("affix", this.id))),
      definitionFactory.apply(Affix.AffixDefinition.builder()).build(),
      exclusiveSet,
      builder.build()
    );
  }
}
