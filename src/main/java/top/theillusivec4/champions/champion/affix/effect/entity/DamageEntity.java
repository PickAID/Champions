package top.theillusivec4.champions.champion.affix.effect.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.champion.value.based.lootcontext.LootContextBasedValue;

import java.util.Set;

public record DamageEntity(LootContextBasedValue minDamage, LootContextBasedValue maxDamage, Holder<DamageType> damageType) implements AffixEntityEffect {
  public static final MapCodec<DamageEntity> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    LootContextBasedValue.CODEC.fieldOf("min_damage").forGetter(DamageEntity::minDamage),
    LootContextBasedValue.CODEC.fieldOf("max_damage").forGetter(DamageEntity::maxDamage),
    DamageType.CODEC.fieldOf("damage_type").forGetter(DamageEntity::damageType)
  ).apply(instance, DamageEntity::new));

  @Override
  public void apply(LootContext context, int level, Entity entity, Vec3 origin) {
    float damage = Mth.randomBetween(context.getRandom(), this.minDamage.calculate(context, level), this.maxDamage.calculate(context, level));
    Entity attacker = context.getParameter(LootContextParams.DIRECT_ATTACKING_ENTITY);
    entity.hurtServer(context.getLevel(), new DamageSource(damageType, attacker), damage);
  }

  @Override
  public MapCodec<? extends AffixEntityEffect> codec() {
    return MAP_CODEC;
  }

  @Override
  public Set<ContextKey<?>> getReferencedContextParams() {
    return Set.of(LootContextParams.DIRECT_ATTACKING_ENTITY);
  }

  @Override
  public void validate(ValidationContext context) {
    AffixEntityEffect.super.validate(context);
    Validatable.validate(context, "min_damage", this.minDamage);
    Validatable.validate(context, "max_damage", this.maxDamage);
  }
}
