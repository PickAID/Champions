package top.theillusivec4.champions.server.champion.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.world.loot.parameters.LootContextParamSets;

import java.util.Optional;

/**
 * 将冠军数据组织进一个配置对象应该对数据转移有所帮助
 *
 * @param rank
 * @param prefixName
 * @param affixes
 * @param level
 * @param color
 * @param boss
 */
public record ChampionConfig(
  Optional<Holder<Rank>> rank,
  Optional<Component> prefixName,
  Optional<Affixes> affixes,
  Optional<Integer> level,
  Optional<Integer> color,
  Optional<Boolean> boss
) {
  public static final Codec<ChampionConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Rank.REFERENCE_CODEC.optionalFieldOf("rank").forGetter(ChampionConfig::rank),
    ComponentSerialization.CODEC.optionalFieldOf("prefix_name").forGetter(ChampionConfig::prefixName),
    Affixes.CODEC.optionalFieldOf("affixes").forGetter(ChampionConfig::affixes),
    Codec.intRange(ChampionDefaultConfigs.MIN_LEVEL, ChampionDefaultConfigs.MAX_LEVEL).optionalFieldOf("level").forGetter(ChampionConfig::level),
    Codec.INT.optionalFieldOf("color").forGetter(ChampionConfig::color),
    Codec.BOOL.optionalFieldOf("boss").forGetter(ChampionConfig::boss)
  ).apply(instance, ChampionConfig::new));

  private static Codec<LootItemCondition> validateConditionCodec(ContextKeySet contextKeySet) {
    return LootItemCondition.DIRECT_CODEC.validate(Validatable.validatorForContext(contextKeySet));
  }


  public record Entry(Optional<LootItemCondition> requirements, ChampionConfig config, int weight) {
    public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      validateConditionCodec(LootContextParamSets.SPAWN).optionalFieldOf("requirements").forGetter(Entry::requirements),
      ChampionConfig.CODEC.fieldOf("config").forGetter(Entry::config),
      Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight").forGetter(Entry::weight)
    ).apply(instance, Entry::new));


    public boolean matches(LootContext lootContext) {
      return this.requirements.isEmpty() || this.requirements.get().test(lootContext);
    }
  }
}
