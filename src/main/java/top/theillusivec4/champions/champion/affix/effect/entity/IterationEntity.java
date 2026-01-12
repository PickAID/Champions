package top.theillusivec4.champions.champion.affix.effect.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * 迭代实体效果组件
 * 用于迭代过滤维度内全部实体并执行效果
 *
 * @param horizontalScale
 * @param verticalScale
 * @param predicate
 * @param effect
 */
public record IterationEntity(double horizontalScale, double verticalScale, Optional<EntityPredicate> predicate, AffixEntityEffect effect) implements AffixEntityEffect {
  public static final MapCodec<IterationEntity> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.doubleRange(0.0, 1024.0).fieldOf("horizontal_scale").forGetter(IterationEntity::horizontalScale),
    Codec.doubleRange(0.0, 1024.0).fieldOf("vertical_scale").forGetter(IterationEntity::verticalScale),
    EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(IterationEntity::predicate),
    AffixEntityEffect.CODEC.fieldOf("effect").forGetter(IterationEntity::effect)
  ).apply(instance, IterationEntity::new));

  @Override
  public void apply(LootContext context, int level, Entity entity, Vec3 origin) {
    ServerLevel serverLevel = context.getLevel();
    AABB aabb= entity.getBoundingBox().inflate(this.horizontalScale, this.verticalScale, this.horizontalScale);
//    if (this.inflate){
//      aabb = entity.getBoundingBox().inflate(this.horizontalScale, this.verticalScale, this.horizontalScale);
//    }else {
//      aabb = new AABB(
//        entity.getX() - this.horizontalScale,
//        entity.getY() - this.verticalScale,
//        entity.getZ() - this.horizontalScale,
//        entity.getX() + this.horizontalScale,
//        entity.getY() + this.verticalScale,
//        entity.getZ() + this.horizontalScale
//      );
//    }
    for (Entity target : serverLevel.getEntities(entity, aabb)) {
      if (this.predicate.map(entityPredicate -> entityPredicate.matches(serverLevel, origin, target)).orElse(true)) {
        this.effect.apply(context, level, target, target.position());
      }
    }
  }

  @Override
  public MapCodec<? extends AffixEntityEffect> codec() {
    return MAP_CODEC;
  }

  @Override
  public void validate(ValidationContext context) {
    AffixEntityEffect.super.validate(context);
    Validatable.validate(context, "effect", this.effect);
  }
}
