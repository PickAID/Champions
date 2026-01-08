package top.theillusivec4.champions.champion.item;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.attachments.Attachments;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.ChampionHandler;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.LatestDamage;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.components.DataComponents;

import java.util.Optional;
import java.util.function.Consumer;

public class ItemChampionHandler implements ChampionHandler {
  private final ItemStack itemStack;
  private final Level level;

  public ItemChampionHandler(ItemStack itemStack, Level level) {
    this.itemStack = itemStack;
    this.level = level;
  }

  @Override
  public void runInitializeEffects(ServerLevel serverLevel, int level, Entity victim, Vec3 origin) {

  }

  @Override
  public void stopInitializeEffects(ServerLevel serverLevel, int level, Entity victim, Vec3 origin) {

  }

  @Override
  public boolean isImmuneToDamage(ServerLevel serverLevel, DamageSource damageSource) {
    return false;
  }

  @Override
  public float getDamageProtection(ServerLevel level, DamageSource source) {
    return 0;
  }

  @Override
  public float modifyKnockback(ServerLevel level, DamageSource source, float knockback) {
    return 0;
  }

  @Override
  public float modifyDamage(ServerLevel level, Entity victim, DamageSource damageSource, float amount) {
    return 0;
  }

  @Override
  public float modifyHeal(ServerLevel level, float amount) {
    return 0;
  }

  @Override
  public void doPostAttackEffects(ServerLevel level, AffixTarget target, Entity victim, DamageSource source) {

  }

  @Override
  public void tickEffects(ServerLevel level) {

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
  public void copyFrom(Entity entity) {
    if (entity.hasData(Attachments.AFFIXES)) {
      this.itemStack.set(DataComponents.AFFIXES, entity.getData(Attachments.AFFIXES).copy());
    }

    if (entity.hasData(Attachments.RANK)) {
      Optional<Holder<Rank>> optional = entity.getData(Attachments.RANK);
      optional.ifPresent(rank -> this.itemStack.set(DataComponents.RANK, rank));
    }

    if (entity.hasData(Attachments.PREFIX_NAME)) {
      this.itemStack.set(DataComponents.PREFIX_NAME, entity.getData(Attachments.PREFIX_NAME).copy());
    }

    if (entity.hasData(Attachments.LEVEL)) {
      this.itemStack.set(DataComponents.LEVEL, entity.getData(Attachments.LEVEL));
    }

    if (entity.hasData(Attachments.COLOR)) {
      this.itemStack.set(DataComponents.COLOR, entity.getData(Attachments.COLOR));
    }
  }

  @Override
  public void copyFrom(ItemStack itemStack) {
    if (itemStack.has(DataComponents.AFFIXES)) {
      this.itemStack.set(DataComponents.AFFIXES, itemStack.getOrDefault(DataComponents.AFFIXES, Affixes.EMPTY).copy());
    }

    if (itemStack.has(DataComponents.RANK)) {
      Holder<Rank> rank = itemStack.getOrDefault(DataComponents.RANK, this.level.registryAccess().getOrThrow(Ranks.COMMON));
      this.itemStack.set(DataComponents.RANK, rank);
    }

    if (itemStack.has(DataComponents.PREFIX_NAME)) {
      this.itemStack.set(DataComponents.PREFIX_NAME, itemStack.getOrDefault(DataComponents.PREFIX_NAME, Component.empty()).copy());
    }

    if (itemStack.has(DataComponents.LEVEL)) {
      this.itemStack.set(DataComponents.LEVEL, itemStack.getOrDefault(DataComponents.LEVEL, 1));
    }

    if (itemStack.has(DataComponents.COLOR)) {
      this.itemStack.set(DataComponents.COLOR, itemStack.getOrDefault(DataComponents.COLOR, -1));
    }
  }

  @Override
  public void updateLatestDamage(Consumer<LatestDamage.Mutable> consumer) {

  }

  @Override
  public boolean isDisplay() {
    return this.itemStack.getOrDefault(DataComponents.DISPLAY, false);
  }

  @Override
  public void setDisplay(boolean display) {
    if (!display) {
      this.itemStack.remove(DataComponents.DISPLAY);
    } else {
      this.itemStack.set(DataComponents.DISPLAY, true);
    }
  }

  @Override
  public Affixes getAllAffixes() {
    return this.itemStack.getOrDefault(DataComponents.AFFIXES, Affixes.EMPTY);
  }

  @Override
  public int getLevel() {
    if (this.itemStack.has(DataComponents.LEVEL)) {
      return this.itemStack.getOrDefault(DataComponents.LEVEL, 1);
    }

    return this.getRank().value().level();
  }

  @Override
  public void setLevel(int level) {
    this.itemStack.set(DataComponents.LEVEL, Math.clamp(level, 1, 255));
  }

  @Override
  public int getColor() {
    if (this.itemStack.has(DataComponents.COLOR)) {
      return this.itemStack.getOrDefault(DataComponents.COLOR, -1);
    }

    return this.getRank().value().color();
  }

  @Override
  public void setColor(int color) {
    this.itemStack.set(DataComponents.COLOR, ARGB.opaque(color));
  }

  @Override
  public Holder<Rank> getRank() {
    return this.itemStack.getOrDefault(DataComponents.RANK, this.level.registryAccess().getOrThrow(Ranks.COMMON));
  }

  @Override
  public void setRank(Holder<Rank> rank) {
    this.itemStack.set(DataComponents.RANK, rank);
  }

  @Override
  public Component getPrefixName() {
    if (this.itemStack.has(DataComponents.PREFIX_NAME)) {
      return this.itemStack.getOrDefault(DataComponents.PREFIX_NAME, Component.empty());
    }

    return this.getRank().value().description();
  }

  @Override
  public void setPrefixName(Component name) {
    this.itemStack.set(DataComponents.PREFIX_NAME, name);
  }
}
