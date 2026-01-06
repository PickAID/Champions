package top.theillusivec4.champions.affix;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.champions.affix.effect.AffixTarget;
import top.theillusivec4.champions.affix.effect.ConditionalEffect;
import top.theillusivec4.champions.affix.effect.DamageImmunity;
import top.theillusivec4.champions.affix.event.ChampionEventHooks;
import top.theillusivec4.champions.attachments.Attachments;
import top.theillusivec4.champions.loot.parameters.LootContextParamSets;
import top.theillusivec4.champions.util.ChampionUtil;

import java.util.function.Consumer;

public class EntityChampionHandler implements ChampionHandler {
  private final Entity entity;

  public EntityChampionHandler(Entity entity) {
    this.entity = entity;
  }

  @Override
  public void runInitializeEffects(ServerLevel serverLevel, int level, Entity victim, Vec3 origin) {
    this.runIteration(affix -> affix.value().runInitialize(serverLevel, level, victim, origin));
  }

  @Override
  public void stopInitializeEffects(ServerLevel serverLevel, int level, Entity victim, Vec3 origin) {
    this.runIteration(affix -> affix.value().stopInitialized(serverLevel, level, victim, origin));
  }

  @Override
  public boolean isImmuneToDamage(ServerLevel serverLevel, DamageSource damageSource) {
    LootContext context = LootContextParamSets.damageImmunity(serverLevel, this.entity, this.getLevel(), damageSource, this.getLatestDamage(), damageSource.getDirectEntity(), damageSource.getEntity());
    for (Holder<Affix> affix : this.getAllAffixes().getAffixes()) {
      for (ConditionalEffect<DamageImmunity> effect : affix.value().getEffects(AffixEffectComponents.DAMAGE_IMMUNITY)) {
        if (effect.matches(context)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public float getDamageProtection(ServerLevel serverLevel, DamageSource source) {
    MutableFloat mutableFloat = new MutableFloat(0.0f);
    this.runIteration(affix -> affix.value().modifyDamageProtection(serverLevel, this.getLevel(), this.entity, source, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyKnockback(ServerLevel serverLevel, DamageSource source, float knockback) {
    MutableFloat mutableFloat = new MutableFloat(knockback);
    this.runIteration(affix -> affix.value().modifyKnockback(serverLevel, this.getLevel(), this.entity, source, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyDamage(ServerLevel level, Entity victim, DamageSource damageSource, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    this.runIteration(affix -> affix.value().modifyDamage(level, this.getLevel(), this.entity, damageSource, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyHeal(ServerLevel level, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    this.runIteration(affix -> affix.value().modifyHeal(level, this.getLevel(), this.entity, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public void doPostAttackEffects(ServerLevel serverLevel, AffixTarget target, Entity victim, DamageSource source) {
    this.runIteration(affix -> affix.value().doPostAttack(serverLevel, this.getLevel(), target, victim, source, this.getLatestDamage()));
  }

  @Override
  public void tickEffects(ServerLevel level) {
    this.runIteration(affix -> affix.value().tick(level, this.getLevel(), this.entity, this.getLatestDamage()));
  }

  @Override
  public void runIteration(Consumer<Holder<Affix>> consumer) {
    for (Holder<Affix> affix : this.getAllAffixes().getAffixes()) {
      consumer.accept(affix);
    }
  }

  @Override
  public void updateAffixes(Consumer<EntityAffixes.Mutable> consumer) {
    if (this.entity.level() instanceof ServerLevel serverLevel) {
      EntityAffixes oldAffixes = entity.getData(Attachments.ENTITY_AFFIXES);
      EntityAffixes.Mutable mutable = oldAffixes.toMutable();
      consumer.accept(mutable);
      EntityAffixes newAffixes = mutable.toImmutable();
      ChampionEventHooks.onUpdateAffixesPre(this.entity, serverLevel, this);
      this.stopInitializeEffects(serverLevel, this.getLevel(), this.entity, this.entity.position());
      this.entity.setData(Attachments.ENTITY_AFFIXES, newAffixes);
      ChampionEventHooks.onUpdateAffixesPost(this.entity, serverLevel, this);
      this.runInitializeEffects(serverLevel, this.getLevel(), this.entity, this.entity.position());
    }
  }

  @Override
  public void copyFrom(Entity entity) {
    if (this.entity != entity && !this.entity.level().isClientSide()) {
      ChampionUtil.getHandler(entity).ifPresent(handler -> {
        this.updateAffixes(mutable -> {
          this.setLevel(handler.getLevel());
          for (Holder<Affix> affix : handler.getAllAffixes().getAffixes()) {
            mutable.add(affix);
          }
        });
      });
    }
  }

  @Override
  public void updateLatestDamage(Consumer<LatestDamage.Mutable> consumer) {
    if (!this.entity.level().isClientSide()) {
      LatestDamage latestDamage = this.entity.getData(Attachments.LATEST_DAMAGE);
      LatestDamage.Mutable mutable = latestDamage.toMutable();
      consumer.accept(mutable);
      this.entity.setData(Attachments.LATEST_DAMAGE, mutable.toImmutable());
    }
  }

  public EntityAffixes getAllAffixes() {
    return this.entity.getData(Attachments.ENTITY_AFFIXES);
  }

  @Override
  public int getLevel() {
    return this.entity.getData(Attachments.LEVEL);
  }

  @Override
  public void setLevel(int level) {
    this.entity.setData(Attachments.LEVEL, Math.max(level, 1));
  }

  private LatestDamage getLatestDamage() {
    return this.entity.getData(Attachments.LATEST_DAMAGE);
  }

}
