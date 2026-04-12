package top.theillusivec4.champions.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import top.theillusivec4.champions.affix.effects.AffixTarget;
import top.theillusivec4.champions.affix.effects.ConditionalEffect;

import java.util.Optional;

public record TargetedConditionalEffect<T>(AffixTarget enchanted, AffixTarget affected, T effect, Optional<LootItemCondition> requirements) {
  /**
   * 创建带目标和谓词的效果组件编解码器
   * <p>
   * 具有触发目标与作用目标<br>
   * 触发目标：只有词缀源实体是该类型的目标时才会触发效果<br>
   * 作用目标：触发效果时尝试从上下文解析该类型目标的实体，如果存在则触发效果
   * </p>
   * <pre>
   *   {
   *     "enchanted":"",
   *     "affected":"",
   *     "effect":{},
   *     "requirements":{}
   *   }
   * </pre>
   * <li><code>enchanted</code>效果触发目标</li>
   * <li><code>affected</code>效果作用目标</li>
   * <li><code>effect</code>效果</li>
   * <li><code>requirements</code>条件</li>
   *
   * @param effectCodec 效果组件编解码器
   * @param <T>         效果组件类型
   * @return 带目标和谓词的效果组件编解码器
   */
  public static <T> Codec<TargetedConditionalEffect<T>> codec(Codec<T> effectCodec, LootContextParamSet set) {
    return RecordCodecBuilder.create(instance -> instance.group(
      AffixTarget.CODEC.fieldOf("enchanted").forGetter(TargetedConditionalEffect::enchanted),
      AffixTarget.CODEC.fieldOf("affected").forGetter(TargetedConditionalEffect::affected),
      effectCodec.fieldOf("effect").forGetter(TargetedConditionalEffect::effect),
      ConditionalEffect.conditionCodec(set).optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements)
    ).apply(instance, TargetedConditionalEffect::new));
  }

  public static <T> TargetedConditionalEffect<T> create(AffixTarget enchanted, T effect) {
    return new TargetedConditionalEffect<>(enchanted, AffixTarget.VICTIM, effect, Optional.empty());
  }

  public static <T> TargetedConditionalEffect<T> create(AffixTarget enchanted, AffixTarget affected, T effect) {
    return new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.empty());
  }

  public static <T> TargetedConditionalEffect<T> create(AffixTarget enchanted, AffixTarget affected, T effect, LootItemCondition.Builder builder) {
    return new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.of(builder.build()));
  }

  public boolean match(LootContext context) {
    return this.requirements.isEmpty() || this.requirements.get().test(context);
  }
}
