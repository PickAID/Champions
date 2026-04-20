package top.theillusivec4.champions.api.affix;

import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.champions.api.affix.effect.AffixTarget;
import top.theillusivec4.champions.core.attachments.ChampionsAttachments;
import top.theillusivec4.champions.core.components.ChampionsDataComponents;
import top.theillusivec4.champions.core.registries.ChampionsDataMaps;
import top.theillusivec4.champions.server.ChampionsServerConfig;
import top.theillusivec4.champions.util.Util;
import top.theillusivec4.champions.api.championmob.ChampionMobPropertyHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class AffixHelper {
  private static final Component TITLE = Component.translatable(
    Util.makeDescriptionId("item", Util.id("stored_affixes"))
  ).withStyle(ChatFormatting.GRAY);

  private AffixHelper() {
  }

  public static EntityAffixes get(ItemStack item) {
    return item.getOrDefault(ChampionsDataComponents.STORED_AFFIXES, EntityAffixes.EMPTY);
  }

  public static void set(ItemStack item, EntityAffixes container) {
    if (!get(item).equals(container)) {
      item.set(ChampionsDataComponents.STORED_AFFIXES, container);
    }
  }

  public static void update(ItemStack item, Consumer<EntityAffixes.Mutable> consumer) {
    EntityAffixes.Mutable mutable = get(item).mutable();
    consumer.accept(mutable);
    set(item, mutable.toImmutable());
  }


  public static EntityAffixes get(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.ENTITY_AFFIXES).orElse(EntityAffixes.EMPTY);
  }

  public static void set(Entity entity, EntityAffixes container) {
    if (!get(entity).equals(container) && entity.level() instanceof ServerLevel level) {
      stopLocationChangedEffects(level, entity);
      if (entity instanceof LivingEntity livingEntity) {
        forEachModifier(entity, ((attribute, modifier) -> {
          AttributeMap map = livingEntity.getAttributes();
          AttributeInstance instance = map.getInstance(attribute);
          if (instance != null) {
            instance.removeModifier(modifier);
          }
        }));
      }
      entity.setData(ChampionsAttachments.ENTITY_AFFIXES, container);
      if (entity instanceof LivingEntity livingEntity) {
        forEachModifier(entity, ((attribute, modifier) -> {
          AttributeMap map = livingEntity.getAttributes();
          AttributeInstance instance = map.getInstance(attribute);
          if (instance != null) {
            instance.addTransientModifier(modifier);
          }
        }));
      }
      runLocationChangedEffects(level, entity);
      ChampionMobPropertyHelper.getBossbar(entity).setAffixes(get(entity));
    }
  }

  public static void update(Entity entity, Consumer<EntityAffixes.Mutable> consumer) {
    EntityAffixes.Mutable mutable = get(entity).mutable();
    consumer.accept(mutable);
    set(entity, mutable.toImmutable());
  }

  public static void addToTooltip(ItemStack item, Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
    EntityAffixes container = get(item);
    if (!container.isEmpty()) {
      tooltipAdder.accept(TITLE);
      container.entrySet().forEach(entry -> tooltipAdder.accept(CommonComponents.space().append(Affix.getFullName(entry.getKey(), entry.getValue()))));
    }
  }

  public static int getAffixableValue(Entity entity) {
    return getAffixableValue(entity.getType());
  }

  public static int getAffixableValue(EntityType<?> entityType) {
    return Optional.ofNullable(entityType.builtInRegistryHolder().getData(ChampionsDataMaps.AFFIXABLE)).map(Affixable::value).orElse(ChampionsServerConfig.DEFAULT_AFFIXABLE.get());
  }

  public static int mapLevelToCost(RandomSource random, int level, int affixableValue) {
    level += 1 + random.nextInt((int) (affixableValue * ChampionsServerConfig.AFFIXABLE_FACTOR.get() + 1)) + random.nextInt((int) (affixableValue * ChampionsServerConfig.AFFIXABLE_FACTOR.get() + 1));
    float f = (float) ((random.nextFloat() + random.nextFloat() - 1.0F) * ChampionsServerConfig.RANDOM_FACTOR.get());
    level = Mth.clamp(Math.round((float) level + (float) level * f), ChampionsServerConfig.MIN_AFFIX_COST.get(), ChampionsServerConfig.MAX_AFFIX_COST.get());
    return level;
  }

  public static List<AffixInstance> selectAffixByLevel(RandomSource random, EntityType<?> entity, int level, Stream<? extends Holder<Affix>> possible) {
    return selectAffixByLevel(random, entity, level, possible, List.of());
  }

  public static List<AffixInstance> selectAffixByLevel(RandomSource random, EntityType<?> entity, int level, Stream<? extends Holder<Affix>> possible, List<AffixInstance> prefix) {
    int value = getAffixableValue(entity);
    if (value > 0) {
      int cost = mapLevelToCost(random, level, value);
      return selectAffixByCost(random, entity, cost, possible, prefix);
    } else {
      return List.of();
    }
  }

  public static List<AffixInstance> selectAffixByCost(RandomSource random, EntityType<?> entity, int cost, Stream<? extends Holder<Affix>> possible) {
    return selectAffixByCost(random, entity, cost, possible, List.of());
  }

  public static List<AffixInstance> selectAffixByCost(RandomSource random, EntityType<?> entity, int cost, Stream<? extends Holder<Affix>> possible, List<AffixInstance> prefix) {
    ImmutableList.Builder<AffixInstance> builder = ImmutableList.builder();
    builder.addAll(filterCompatibleAffixes(prefix));
    List<AffixInstance> list = new ArrayList<>(getAvailableAffixResults(cost, entity, possible));
    if (!list.isEmpty()) {

      while (random.nextInt(10) <= cost) {
        Optional<AffixInstance> optional = WeightedRandom.getRandomItem(random, list);
        if (optional.isPresent()) {
          AffixInstance instance = optional.get();
          removeCompatibleAffixes(list, instance);
          builder.add(instance);
        } else {
          break;
        }

        if (list.isEmpty()) {
          break;
        }

        cost /= 2;
      }
    }
    return builder.build();
  }

  public static List<AffixInstance> getAvailableAffixResults(int cost, EntityType<?> entity, Stream<? extends Holder<Affix>> possible) {
    ImmutableList.Builder<AffixInstance> builder = ImmutableList.builder();

    possible.filter(affix -> affix.value().isSupported(entity)).forEach(affix -> {
      Affix value = affix.value();

      for (int i = value.getMaxLevel(); i >= value.getMinLevel(); i--) {
        if (cost >= value.getMinCost(i) && cost <= value.getMaxCost(i)) {
          builder.add(new AffixInstance(affix, i));
          break;
        }
      }
    });
    return builder.build();
  }

  private static void removeCompatibleAffixes(List<AffixInstance> affixes, AffixInstance target) {
    affixes.removeIf(instance -> !Affix.areCompatible(instance.affix(), target.affix()));
  }

  private static List<AffixInstance> filterCompatibleAffixes(List<AffixInstance> affixes) {
    ImmutableList.Builder<AffixInstance> builder = ImmutableList.builder();
    AffixInstance last = null;
    for (AffixInstance affix : affixes) {
      if (last == null || Affix.areCompatible(affix, last)) {
        builder.add(affix);
        last = affix;
      }
    }
    return builder.build();
  }

  public static void runIteration(Entity entity, AffixVisitor visitor) {
    get(entity).entrySet().forEach(entry -> visitor.accept(entry.getKey(), entry.getValue()));
  }

  public static void runLocationChangedEffects(ServerLevel level, Entity entity) {
    runIteration(entity, (affix, affixLevel) -> affix.value().runLocationChangedEffects(level, affixLevel, entity));
  }

  public static void stopLocationChangedEffects(ServerLevel level, Entity entity) {
    runIteration(entity, (affix, affixLevel) -> affix.value().stopLocationChangedEffects(level, affixLevel, entity));
  }

  public static void forEachModifier(Entity entity, BiConsumer<Holder<Attribute>, AttributeModifier> action) {
    runIteration(entity, (affix, affixLevel) -> affix.value().forEachModifier(affixLevel, action));
  }

  public static boolean isImmuneToDamage(ServerLevel serverLevel, Entity victim, DamageSource source) {
    MutableBoolean mutable = new MutableBoolean();
    runIteration(victim, (affix, affixLevel) -> mutable.setValue(
      mutable.isTrue() || affix.value().isImmuneToDamage(serverLevel, affixLevel, victim, source)
    ));
    return mutable.isTrue();
  }

  public static float getDamageProtection(ServerLevel level, Entity victim, DamageSource source) {
    MutableFloat mutableFloat = new MutableFloat(0.0f);
    runIteration(victim, (affix, affixLevel) -> affix.value().modifyDamageProtection(level, affixLevel, victim, source, mutableFloat));
    return mutableFloat.floatValue();
  }

  public static float modifyKnockback(ServerLevel level, Entity victim, DamageSource source, float knockback) {
    MutableFloat mutableFloat = new MutableFloat(knockback);
    Entity attacker = source.getEntity();
    if (attacker != null) {
      runIteration(attacker, (affix, affixLevel) -> affix.value().modifyKnockback(level, affixLevel, victim, source, mutableFloat));
    }
    return mutableFloat.floatValue();
  }

  public static float modifyDamage(ServerLevel level, Entity victim, DamageSource source, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    Entity attacker = source.getEntity();
    if (attacker != null) {
      runIteration(attacker, (affix, affixLevel) -> affix.value().modifyDamage(level, affixLevel, victim, source, mutableFloat));
    }
    return mutableFloat.floatValue();
  }

  public static float modifyHeal(ServerLevel level, Entity victim, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    runIteration(victim, (affix, affixLevel) -> affix.value().modifyHeal(level, affixLevel, victim, mutableFloat));
    return mutableFloat.floatValue();
  }

  public static void doPostAttackEffects(ServerLevel level, Entity victim, DamageSource source) {
    runIteration(victim, (affix, affixLevel) -> affix.value().doPostAttack(level, affixLevel, AffixTarget.VICTIM, victim, source));
    Entity direct = source.getDirectEntity();
    if (direct != null) {
      runIteration(direct, (affix, affixLevel) -> affix.value().doPostAttack(level, affixLevel, AffixTarget.DAMAGING_ENTITY, victim, source));
    }
    Entity attacker = source.getEntity();
    if (attacker != null) {
      runIteration(attacker, (affix, affixLevel) -> affix.value().doPostAttack(level, affixLevel, AffixTarget.ATTACKER, victim, source));
    }
  }

  public static void tickEffects(ServerLevel level, Entity entity) {
    runIteration(entity, (affix, affixLevel) -> affix.value().tickEffects(level, affixLevel, entity));
  }

  public static void targetEffects(ServerLevel level, Entity entity) {
    if (entity instanceof Mob mob && mob.getTarget() != null) {
      runIteration(entity, (affix, affixLevel) -> affix.value().targetEffects(level, affixLevel, mob, mob.getTarget()));
    }
  }

  @FunctionalInterface
  public interface AffixVisitor {
    void accept(Holder<Affix> affix, int level);
  }
}
