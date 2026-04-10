package top.theillusivec4.champions.affix;

import com.google.common.collect.Lists;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.champions.affix.effects.AffixTarget;
import top.theillusivec4.champions.attachments.ChampionsAttachments;
import top.theillusivec4.champions.champion.ChampionHelper;
import top.theillusivec4.champions.component.ChampionsDataComponents;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.server.boss.ChampionsServerBossEvent;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class AffixHelper {
  private AffixHelper() {
  }

  public static AffixContainer getStored(ItemStack item) {
    return item.getOrDefault(ChampionsDataComponents.STORED_AFFIXES, AffixContainer.EMPTY);
  }

  public static List<AffixInstance> selectAffix(RandomSource random, Entity entity, int level, Stream<Holder<Affix>> possible) {
    // 选择可用的词缀 词缀亲和力 需要注册项映射
    List<AffixInstance> list = Lists.newArrayList();
    Item item = stack.getItem();
    int i = stack.getEnchantmentValue();
    if (i <= 0) {
      return list;
    } else {
      level += 1 + random.nextInt(i / 4 + 1) + random.nextInt(i / 4 + 1);
      float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
      level = Mth.clamp(Math.round((float) level + (float) level * f), 1, Integer.MAX_VALUE);
      List<AffixInstance> list1 = getAvailableAffixResults(level, entity, possible);
      if (!list1.isEmpty()) {
        WeightedRandom.getRandomItem(random, list1).ifPresent(list::add);

        while (random.nextInt(50) <= level) {
          if (!list.isEmpty()) {
            filterCompatibleAffixes(list1, Util.lastOf(list));
          }

          if (list1.isEmpty()) {
            break;
          }

          WeightedRandom.getRandomItem(random, list1).ifPresent(list::add);
          level /= 2;
        }
      }

      return list;
    }
  }

  public static List<AffixInstance> getAvailableAffixResults(int level, Entity entity, Stream<Holder<Affix>> possible) {
    List<AffixInstance> list = Lists.newArrayList();

    possible.filter(affix -> affix.value().isSupported(entity)).forEach(affix -> {
      Affix value = affix.value();

      for (int i = value.getMaxLevel(); i >= value.getMinLevel(); i--) {
        if (level >= value.getMinCost(i) && level <= value.getMaxCost(i)) {
          list.add(new AffixInstance(affix, i));
          break;
        }
      }
    });
    return list;
  }

  public static void filterCompatibleAffixes(List<AffixInstance> affixes, AffixInstance target) {
    affixes.removeIf(instance -> !Affix.areCompatible(instance.affix(), target.affix()));
  }

  public static boolean isEnchantmentCompatible(Collection<Holder<Enchantment>> currentEnchantments, Holder<Enchantment> newEnchantment) {
    for (Holder<Enchantment> holder : currentEnchantments) {
      if (!Enchantment.areCompatible(holder, newEnchantment)) {
        return false;
      }
    }

    return true;
  }

  public static void doFinalizeSpawn(ServerLevel level, Mob mob, double x, double y, double z, DifficultyInstance difficultyInstance, MobSpawnType reason) {
    RandomSource random = level.getRandom();
    if (random.nextFloat() < difficultyInstance.getSpecialMultiplier()) {
      // 这里做初始化
      List<AffixInstance> list = AffixHelper.selectAffix(mob, championLevel, level.registryAccess().lookupOrThrow(ChampionsRegistries.Keys.AFFIX).listElements());
      updateEntity(mob, mutable -> list.forEach(instance -> mutable.upgrade(instance.affix(), instance.level())));
    }
  }

  public static void updateItem(ItemStack item, Consumer<AffixContainer.Mutable> consumer) {
    AffixContainer.Mutable mutable = getStored(item).mutable();
    consumer.accept(mutable);
    item.set(ChampionsDataComponents.STORED_AFFIXES, mutable.toImmutable());
  }

  public static AffixContainer get(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.AFFIXES).orElse(AffixContainer.EMPTY);
  }

  public static void updateEntity(Entity entity, Consumer<AffixContainer.Mutable> consumer) {
    AffixContainer.Mutable mutable = get(entity).mutable();
    consumer.accept(mutable);
    entity.setData(ChampionsAttachments.AFFIXES, mutable.toImmutable());

    ChampionsServerBossEvent event = ChampionHelper.getBossbar(entity);
    if (event != ChampionsServerBossEvent.EMPTY) {
      event.setAffixes(get(entity));
    }
  }

  public static void runIteration(Entity entity, AffixVisitor visitor) {
    get(entity).entrySet().forEach(entry -> visitor.accept(entry.getKey(), entry.getValue()));
  }

  public static void runLocationChangedEffects(ServerLevel level, Entity entity, Vec3 origin, boolean becameActive) {
    runIteration(entity, (affix, affixLevel) -> affix.value().runLocationChangedEffects(level, affixLevel, entity, origin, becameActive));
  }

  public static void stopLocationChangedEffects(ServerLevel level, Entity entity, Vec3 origin) {
    runIteration(entity, (affix, affixLevel) -> affix.value().stopLocationChangedEffects(level, affixLevel, entity, origin));
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
    runIteration(victim, (affix, affixLevel) -> affix.value().modifyKnockback(level, affixLevel, victim, source, mutableFloat));
    return mutableFloat.floatValue();
  }

  public static float modifyDamage(ServerLevel level, Entity victim, DamageSource source, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    runIteration(victim, (affix, affixLevel) -> affix.value().modifyDamage(level, affixLevel, victim, source, mutableFloat));
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
      runIteration(attacker, (affix, affixLevel) -> affix.value().doPostAttack(level, affixLevel, AffixTarget.DAMAGING_ENTITY, victim, source));
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
