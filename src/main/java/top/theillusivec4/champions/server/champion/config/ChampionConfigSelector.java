package top.theillusivec4.champions.server.champion.config;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.champion.ChampionConfig;
import top.theillusivec4.champions.world.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Optional;

/**
 * <h1>冠军配置选择器</h1>
 * <p>
 * 用于处理实体生成时的冠军配置数据选择与应用。<br>
 * 游戏中将实体类型id解析为配置选择器id进行选择。<br>
 * 例如：minecraft:zombie，实际配置选择器路径：minecraft:champions/champion_config_selector/zombie.json
 * </p>
 * <pre>
 *   {
 *     "entries":[
 *      {
 *        "requirements"{},
 *        "config":{},
 *        "weight":5
 *      }
 *     ],
 *     "random_sequence":"minecraft:zombie"
 *   }
 * </pre>
 * <li><code>entries</code>配置项列表</li>
 * <li><code>random_sequence</code>随机序列[可选]</li>
 * <li><code>requirements</code>条件[可选]</li>
 * <li><code>config</code>配置</li>
 * <li><code>weight</code>权重</li>
 *
 * @param entries
 * @param randomSequence
 */
public record ChampionConfigSelector(List<ChampionConfigEntry> entries, Optional<Identifier> randomSequence) {
  public static final Codec<ChampionConfigSelector> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    ChampionConfigEntry.CODEC.listOf().fieldOf("entries").forGetter(ChampionConfigSelector::entries),
    Identifier.CODEC.optionalFieldOf("random_sequence").forGetter(ChampionConfigSelector::randomSequence)
  ).apply(instance, ChampionConfigSelector::new));
  public static final Codec<Optional<WithConditions<ChampionConfigSelector>>> WITH_CONDITIONS_CODEC = ConditionalOps.createConditionalCodecWithConditions(CODEC);

  public static Builder builder() {
    return new Builder();
  }

  public Optional<ChampionConfig> select(ServerLevel serverLevel, Entity entity, @Nullable EntitySpawnReason entitySpawnReason) {
    LootContext lootContext = LootContextParamSets.spawn(serverLevel, entity, entitySpawnReason, this.randomSequence);
    RandomSource randomSource = lootContext.getRandom();
    List<ChampionConfigEntry> entries = this.entries().stream().filter(entry -> entry.matches(lootContext)).toList();
    final int totalWeight = entries.stream().mapToInt(ChampionConfigEntry::weight).sum();
    final int selectWeight = randomSource.nextInt(totalWeight);
    int currentWeight = 0;
    for (ChampionConfigEntry entry : entries) {
      currentWeight += entry.weight();
      if (selectWeight < currentWeight) {
        return Optional.of(entry.config());
      }
    }

    return Optional.empty();
  }

  public static class Builder {
    private ImmutableList.Builder<ChampionConfigEntry> builder = new ImmutableList.Builder<>();

    public Builder add(ChampionConfig.Builder config) {
      return this.add(null, config, 5);
    }

    public Builder add(ChampionConfig.Builder config, int weight) {
      return this.add(null, config, weight);
    }

    public Builder add(@Nullable LootItemCondition requirements, ChampionConfig.Builder config, int weight) {
      this.builder.add(new ChampionConfigEntry(Optional.ofNullable(requirements), config.build(), Math.clamp(weight, 1, Integer.MAX_VALUE)));
      return this;
    }

    public ChampionConfigSelector build(@Nullable Identifier id) {
      return new ChampionConfigSelector(this.builder.build(), Optional.ofNullable(id));
    }
  }
}
