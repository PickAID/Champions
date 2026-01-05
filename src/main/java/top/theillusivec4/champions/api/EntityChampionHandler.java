package top.theillusivec4.champions.api;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.LatestDamage;
import top.theillusivec4.champions.api.affix.effect.AffixTarget;
import top.theillusivec4.champions.api.affix.event.ChampionEventHooks;
import top.theillusivec4.champions.common.attachments.AttachmentTypes;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EntityChampionHandler implements IChampionHandler {
  private final Entity entity;

  public EntityChampionHandler(Entity entity) {
    this.entity = entity;
  }

  @Override
  public void addAttributeModifier() {
    if (this.entity.level() instanceof ServerLevel serverLevel && this.entity instanceof LivingEntity livingEntity) {
      this.forEachModifier(serverLevel, (attribute, modifier) -> {
        AttributeInstance attributeInstance = livingEntity.getAttributes().getInstance(attribute);
        if (attributeInstance != null) {
          attributeInstance.addTransientModifier(modifier);
        }
      });
    }
  }

  @Override
  public void removeAttributeModifier() {
    if (this.entity.level() instanceof ServerLevel serverLevel && this.entity instanceof LivingEntity livingEntity) {
      this.forEachModifier(serverLevel, (attribute, modifier) -> {
        AttributeInstance attributeInstance = livingEntity.getAttributes().getInstance(attribute);
        if (attributeInstance != null) {
          attributeInstance.removeModifier(modifier);
        }
      });
    }
  }

  @Override
  public float getDamageProtection(ServerLevel serverLevel, DamageSource source) {
    MutableFloat mutableFloat = new MutableFloat(0.0f);
    this.runIteration(affix -> affix.value().modifyDamageProtection(serverLevel, this.getChampionLevel(), this.entity, source, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyKnockback(ServerLevel serverLevel, DamageSource source, float knockback) {
    MutableFloat mutableFloat = new MutableFloat(knockback);
    this.runIteration(affix -> affix.value().modifyKnockback(serverLevel, this.getChampionLevel(), this.entity, source, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public void doPostAttackEffects(ServerLevel serverLevel, AffixTarget target, Entity victim, DamageSource source) {
    this.runIteration(affix -> affix.value().doPostAttack(serverLevel, this.getChampionLevel(), target, victim, source, this.getLatestDamage()));
  }

  @Override
  public void doSpawnEffects(ServerLevel serverLevel) {
    this.runIteration(affix -> affix.value().doSpawn(serverLevel, this.entity, this.getChampionLevel()));
  }

  @Override
  public void tickEffects(ServerLevel level) {
    this.runIteration(affix -> affix.value().tick(level, this.getChampionLevel(), this.entity, this.getLatestDamage()));
  }

  @Override
  public void runIteration(Consumer<Holder<Affix>> consumer) {
    for (Holder<Affix> affix : this.getEntityAffixes().getAffixes()) {
      consumer.accept(affix);
    }
  }

  @Override
  public void updateAffixes(Consumer<EntityAffixes.Mutable> consumer) {
    if (this.entity.level() instanceof ServerLevel serverLevel) {
      EntityAffixes oldAffixes = entity.getData(AttachmentTypes.ENTITY_AFFIXES);
      EntityAffixes.Mutable mutable = oldAffixes.toMutable();
      consumer.accept(mutable);
      EntityAffixes newAffixes = mutable.toImmutable();
      ChampionEventHooks.onUpdateAffixesPre(this.entity, serverLevel, this);
      this.entity.setData(AttachmentTypes.ENTITY_AFFIXES, newAffixes);
      ChampionEventHooks.onUpdateAffixesPost(this.entity, serverLevel, this);
    }
  }

  @Override
  public void updateLatestDamage(Consumer<LatestDamage.Mutable> consumer) {
    if (!this.entity.level().isClientSide()) {
      LatestDamage latestDamage = this.entity.getData(AttachmentTypes.LATEST_DAMAGE);
      LatestDamage.Mutable mutable = latestDamage.toMutable();
      consumer.accept(mutable);
      this.entity.setData(AttachmentTypes.LATEST_DAMAGE, mutable.toImmutable());
    }
  }

  @Override
  public int getChampionLevel() {
    return this.entity.getData(AttachmentTypes.LEVEL);
  }

  @Override
  public void setChampionLevel(int level) {
    this.entity.setData(AttachmentTypes.LEVEL, level);
  }

  private LatestDamage getLatestDamage() {
    return this.entity.getData(AttachmentTypes.LATEST_DAMAGE);
  }

  private EntityAffixes getEntityAffixes() {
    return this.entity.getData(AttachmentTypes.ENTITY_AFFIXES);
  }

  private void forEachModifier(ServerLevel serverLevel, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
    if (this.entity instanceof LivingEntity livingEntity) {
      this.runIteration(affix -> affix.value().forEachModifier(serverLevel, this.getChampionLevel(), livingEntity, consumer));
    }
  }

}
