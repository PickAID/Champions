package top.theillusivec4.champions.champion.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.attachment.Attachments;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.ChampionDefaultConfig;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.component.DataComponents;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 物品的实现，许多方法为空实现有些怪异，我或许应该将某一个数据组件或附件与方法绑定
 */
public class ChampionHandlerItemImpl implements ChampionHandlerItem {
  private final ItemStack itemStack;

  public ChampionHandlerItemImpl(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

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
  public void copyFrom(IAttachmentHolder holder) {
    if (holder.hasData(Attachments.AFFIXES)) {
      this.itemStack.set(DataComponents.AFFIXES, holder.getData(Attachments.AFFIXES).copy());
    }

    if (holder.hasData(Attachments.RANK)) {
      Optional<Holder<Rank>> optional = holder.getData(Attachments.RANK);
      optional.ifPresent(rank -> this.itemStack.set(DataComponents.RANK, rank));
    }

    if (holder.hasData(Attachments.PREFIX_NAME)) {
      holder.getData(Attachments.PREFIX_NAME).ifPresent(this::setPrefixName);
    }

    if (holder.hasData(Attachments.LEVEL)) {
      this.itemStack.set(DataComponents.LEVEL, holder.getData(Attachments.LEVEL));
    }

    if (holder.hasData(Attachments.COLOR)) {
      this.itemStack.set(DataComponents.COLOR, holder.getData(Attachments.COLOR));
    }

    if (holder.hasData(Attachments.BOSS)) {
      this.itemStack.set(DataComponents.BOSS, holder.getData(Attachments.BOSS));
    }
  }

  @Override
  public void copyFrom(DataComponentHolder holder) {
    if (holder.has(DataComponents.AFFIXES)) {
      this.itemStack.set(DataComponents.AFFIXES, holder.getOrDefault(DataComponents.AFFIXES, Affixes.EMPTY).copy());
    }

    if (holder.has(DataComponents.RANK)) {
      Holder<Rank> rank = holder.get(DataComponents.RANK);
      this.itemStack.set(DataComponents.RANK, rank);
    }

    if (holder.has(DataComponents.PREFIX_NAME)) {
      this.itemStack.set(DataComponents.PREFIX_NAME, holder.getOrDefault(DataComponents.PREFIX_NAME, Component.empty()).copy());
    }

    if (holder.has(DataComponents.LEVEL)) {
      this.itemStack.set(DataComponents.LEVEL, holder.getOrDefault(DataComponents.LEVEL, 1));
    }

    if (holder.has(DataComponents.COLOR)) {
      this.itemStack.set(DataComponents.COLOR, holder.getOrDefault(DataComponents.COLOR, -1));
    }

    if (holder.has(DataComponents.BOSS)) {
      this.itemStack.set(DataComponents.BOSS, holder.getOrDefault(DataComponents.BOSS, false));
    }

  }

  @Override
  public Affixes getAffixes() {
    return this.itemStack.getOrDefault(DataComponents.AFFIXES, Affixes.EMPTY);
  }

  @Override
  public int getLevel() {
    if (this.itemStack.has(DataComponents.LEVEL)) {
      return this.itemStack.getOrDefault(DataComponents.LEVEL, ChampionDefaultConfig.EMPTY_LEVEL);
    }

    return this.getRank().map(rank -> rank.value().level()).orElse(ChampionDefaultConfig.EMPTY_LEVEL);
  }

  @Override
  public void setLevel(int level) {
    if (level <= ChampionDefaultConfig.EMPTY_LEVEL) {
      this.itemStack.remove(DataComponents.LEVEL);
    } else {
      this.itemStack.set(DataComponents.LEVEL, Math.clamp(level, ChampionDefaultConfig.MIN_LEVEL, ChampionDefaultConfig.MAX_LEVEL));
    }
  }

  @Override
  public boolean isBoss() {
    return this.itemStack.getOrDefault(DataComponents.BOSS, false);
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
  public int getColor() {
    if (this.itemStack.has(DataComponents.COLOR)) {
      return this.itemStack.getOrDefault(DataComponents.COLOR, -1);
    }

    return this.getRank().map(rank -> rank.value().color()).orElse(ChampionDefaultConfig.DEFAULT_COLOR);
  }

  @Override
  public void setColor(int color) {
    if (color == ChampionDefaultConfig.DEFAULT_COLOR) {
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
    if (!rank.is(Ranks.EMPTY)) {
      this.itemStack.set(DataComponents.RANK, rank);
    }
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
