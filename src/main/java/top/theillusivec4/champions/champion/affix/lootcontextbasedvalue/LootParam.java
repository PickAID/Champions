package top.theillusivec4.champions.champion.affix.lootcontextbasedvalue;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Set;

/**
 * 从战利品上下文参数映射值
 */
@Deprecated
public record LootParam(FloatLootParamSource<?> source) implements LootContextBasedValue {
  public static final MapCodec<LootParam> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    FloatLootParamSource.CODEC.fieldOf("source").forGetter(LootParam::source)
  ).apply(instance, LootParam::new));

  @Override
  public float calculate(LootContext context, int level) {
    return source.provide(context);
  }

  @Override
  public MapCodec<? extends LootContextBasedValue> codec() {
    return MAP_CODEC;
  }

  @Override
  public Set<ContextKey<?>> getReferencedContextParams() {
    return source.getReferencedContextParams();
  }
}
