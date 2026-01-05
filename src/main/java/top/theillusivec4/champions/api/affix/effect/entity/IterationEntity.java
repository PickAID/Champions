package top.theillusivec4.champions.api.affix.effect.entity;

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

public record IterationEntity(double xScale, double yScale, double zScale, Optional<EntityPredicate> predicate, AffixEntityEffect effect) implements AffixEntityEffect {
  public static final MapCodec<IterationEntity> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.doubleRange(1.0, 1024.0).fieldOf("x_scale").forGetter(IterationEntity::xScale),
    Codec.doubleRange(1.0, 1024.0).fieldOf("x_scale").forGetter(IterationEntity::xScale),
    Codec.doubleRange(1.0, 1024.0).fieldOf("x_scale").forGetter(IterationEntity::xScale),
    EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(IterationEntity::predicate),
    AffixEntityEffect.CODEC.fieldOf("effect").forGetter(IterationEntity::effect)
  ).apply(instance, IterationEntity::new));

  @Override
  public void apply(LootContext context, int level, Entity entity, Vec3 position) {
    ServerLevel serverLevel = context.getLevel();
    AABB boundingBox = entity.getBoundingBox();
    for (Entity entity1 : serverLevel.getEntities(entity, boundingBox.inflate(this.xScale, this.yScale, this.zScale))) {
      Vec3 position1 = entity1.position();
      if (this.predicate.map(predicate1 -> predicate1.matches(serverLevel, position1, entity1)).orElse(true)) {
        this.effect.apply(context, level, entity1, position1);
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
