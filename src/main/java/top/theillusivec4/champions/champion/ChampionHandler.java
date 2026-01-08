package top.theillusivec4.champions.champion;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.LatestDamage;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;
import top.theillusivec4.champions.champion.rank.Rank;

import java.util.function.Consumer;

public interface ChampionHandler {
  void runInitializeEffects(ServerLevel serverLevel, int level, Entity victim, Vec3 origin);

  void stopInitializeEffects(ServerLevel serverLevel, int level, Entity victim, Vec3 origin);

  boolean isImmuneToDamage(ServerLevel serverLevel, DamageSource damageSource);

  float getDamageProtection(ServerLevel level, DamageSource source);

  float modifyKnockback(ServerLevel level, DamageSource source, float knockback);

  float modifyDamage(ServerLevel level, Entity victim, DamageSource damageSource, float amount);

  float modifyHeal(ServerLevel level, float amount);

  void doPostAttackEffects(ServerLevel level, AffixTarget target, Entity victim, DamageSource source);

  void tickEffects(ServerLevel level);

  void runIteration(Consumer<Holder<Affix>> consumer);

  void updateAffixes(Consumer<Affixes.Mutable> consumer);

  void copyFrom(IAttachmentHolder holder);

  void copyFrom(DataComponentHolder holder);

  void updateLatestDamage(Consumer<LatestDamage.Mutable> consumer);

  boolean isDisplay();

  void setDisplay(boolean display);

  Affixes getAllAffixes();

  int getLevel();

  void setLevel(int level);

  int getColor();

  void setColor(int color);

  Holder<Rank> getRank();

  void setRank(Holder<Rank> rank);

  Component getPrefixName();

  void setPrefixName(Component name);
}
