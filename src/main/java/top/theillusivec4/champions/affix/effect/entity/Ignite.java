package top.theillusivec4.champions.affix.effect.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.affix.lootcontextbasedvalue.LootContextBasedValue;

public record Ignite(LootContextBasedValue duration) implements AffixEntityEffect {
  public static final MapCodec<Ignite> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    LootContextBasedValue.CODEC.fieldOf("duration").forGetter(Ignite::duration)
  ).apply(instance, Ignite::new));

  @Override
  public void apply(LootContext context, int level, Entity entity, Vec3 origin) {
    entity.igniteForSeconds(this.duration.calculate(context, level));
  }

  @Override
  public MapCodec<? extends AffixEntityEffect> codec() {
    return MAP_CODEC;
  }

  @Override
  public void validate(ValidationContext context) {
    AffixEntityEffect.super.validate(context);
    Validatable.validate(context, "duration", this.duration);
  }
}
