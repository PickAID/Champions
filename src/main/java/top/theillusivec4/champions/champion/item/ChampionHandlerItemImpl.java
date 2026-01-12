package top.theillusivec4.champions.champion.item;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.component.DataComponents;
import top.theillusivec4.champions.server.champion.config.ChampionDefaultConfigs;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 物品的实现，许多方法为空实现有些怪异，我或许应该将某一个数据组件或附件与方法绑定
 */
public record ChampionHandlerItemImpl(ItemStack itemStack) implements ChampionHandlerItem {

  @Override
  public void runInitializeEffects(ServerLevel serverLevel, Entity entity, Vec3 origin) {

  }

  @Override
  public void stopInitializeEffects(ServerLevel serverLevel, Entity entity, Vec3 origin) {

  }

  @Override
  public boolean isImmuneToDamage(ServerLevel serverLevel, DamageSource damageSource) {
    return false;
  }

  @Override
  public float getDamageProtection(ServerLevel serverLevel, DamageSource damageSource) {
    return 0;
  }

  @Override
  public float modifyKnockback(ServerLevel serverLevel, DamageSource damageSource, float knockback) {
    return 0;
  }

  @Override
  public float modifyDamage(ServerLevel serverLevel, Entity target, DamageSource damageSource, float amount) {
    return 0;
  }

  @Override
  public float modifyHeal(ServerLevel serverLevel, float amount) {
    return 0;
  }

  @Override
  public void doPostAttackEffects(ServerLevel serverLevel, AffixTarget targetType, Entity victim, DamageSource damageSource) {

  }

  @Override
  public void tickEffects(ServerLevel serverLevel) {

  }

  @Override
  public void runIteration(Consumer<Holder<Affix>> consumer) {

  }

  @Override
  public void updateAffixes(Consumer<Affixes.Mutable> consumer) {
    Affixes affixes = this.itemStack.getOrDefault(DataComponents.AFFIXES, Affixes.EMPTY);
    Affixes.Mutable mutable = affixes.toMutable();
    consumer.accept(mutable);
    Affixes affixes1 = mutable.toImmutable();
    if (!affixes1.equals(affixes)) {
      this.itemStack.set(DataComponents.AFFIXES, affixes1);
    }
  }

  @Override
  public Optional<Affixes> getAffixes() {
    return Optional.ofNullable(this.itemStack.get(DataComponents.AFFIXES));
  }

  @Override
  public Affixes getAffixesOrDefault() {
    return this.itemStack.getOrDefault(DataComponents.AFFIXES, Affixes.EMPTY);
  }

  @Override
  public Optional<Integer> getLevel() {
    if (this.itemStack.has(DataComponents.LEVEL)) {
      return Optional.ofNullable(this.itemStack.get(DataComponents.LEVEL));
    }

    return this.getRank().map(rank -> rank.value().level());
  }

  @Override
  public void setLevel(int level) {
    if (level <= ChampionDefaultConfigs.DEFAULT_LEVEL) {
      this.itemStack.remove(DataComponents.LEVEL);
    } else {
      this.itemStack.set(DataComponents.LEVEL, Math.clamp(level, ChampionDefaultConfigs.MIN_LEVEL, ChampionDefaultConfigs.MAX_LEVEL));
    }
  }

  @Override
  public int getLevelOrDefault() {
    return this.getLevel().orElse(ChampionDefaultConfigs.DEFAULT_LEVEL);
  }

  @Override
  public Optional<Boolean> isBoss() {
    return Optional.ofNullable(this.itemStack.get(DataComponents.BOSS));
  }

  @Override
  public boolean isBossOrDefault() {
    return this.isBoss().orElse(false);
  }

  @Override
  public void setBoss(boolean boss) {
    if (boss) {
      this.itemStack.set(DataComponents.BOSS, true);
    } else {
      this.itemStack.remove(DataComponents.BOSS);
    }
  }

  @Override
  public Optional<Integer> getColor() {
    if (this.itemStack.has(DataComponents.COLOR)) {
      return Optional.ofNullable(this.itemStack.get(DataComponents.COLOR));
    }

    return this.getRank().map(rank -> rank.value().color());
  }

  @Override
  public void setColor(int color) {
    if (color == ChampionDefaultConfigs.DEFAULT_COLOR) {
      this.itemStack.remove(DataComponents.COLOR);
    } else {
      this.itemStack.set(DataComponents.COLOR, ARGB.opaque(color));
    }
  }

  @Override
  public Optional<Holder<Rank>> getRank() {
    return Optional.ofNullable(this.itemStack.get(DataComponents.RANK));
  }

  @Override
  public void setRank(Holder<Rank> rank) {
    this.itemStack.set(DataComponents.RANK, rank);
  }

  @Override
  public Optional<Component> getPrefixName() {
    if (this.itemStack.has(DataComponents.PREFIX_NAME)) {
      return Optional.ofNullable(this.itemStack.get(DataComponents.PREFIX_NAME));
    }

    return this.getRank().map(rank -> rank.value().description());
  }

  @Override
  public void setPrefixName(@Nullable Component name) {
    if (name == null) {
      this.itemStack.remove(DataComponents.PREFIX_NAME);
    } else {
      this.itemStack.set(DataComponents.PREFIX_NAME, name);
    }
  }

}
