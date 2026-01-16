package top.theillusivec4.champions.champion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;

import java.util.Optional;

/**
 * 一个冠军数据快照对象。
 * 将冠军数据组织进一个配置对象应该对数据转移有所帮助，事实上也确实如此。
 *
 * @param rank
 * @param prefixName
 * @param affixes
 * @param level
 * @param color
 * @param boss
 */
public record ChampionData(
  Optional<Holder<Rank>> rank,
  Optional<Component> prefixName,
  Optional<Affixes> affixes,
  Optional<Integer> level,
  Optional<Integer> color,
  Optional<Boolean> boss
) {
  public static final Codec<ChampionData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Rank.REFERENCE_CODEC.optionalFieldOf("rank").forGetter(ChampionData::rank),
    ComponentSerialization.CODEC.optionalFieldOf("prefix_name").forGetter(ChampionData::prefixName),
    Affixes.CODEC.optionalFieldOf("affixes").forGetter(ChampionData::affixes),
    Codec.INT.optionalFieldOf("level").forGetter(ChampionData::level),
    Codec.INT.optionalFieldOf("color").forGetter(ChampionData::color), Codec.BOOL.optionalFieldOf("boss").forGetter(ChampionData::boss)
  ).apply(instance, ChampionData::new));

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private @Nullable Holder<Rank> rank;
    private @Nullable Component prefixName;
    private @Nullable Affixes.Mutable affixes;
    private @Nullable Integer level;
    private @Nullable Integer color;
    private @Nullable Boolean boss;

    public Builder addAffix(Holder<Affix> affix) {
      if (this.affixes == null) {
        this.affixes = new Affixes.Mutable();
      }
      this.affixes.add(affix);
      return this;
    }

    public ChampionData build() {
      return new ChampionData(
        Optional.ofNullable(this.rank),
        Optional.ofNullable(this.prefixName),
        this.affixes != null ? Optional.of(this.affixes.toImmutable()) : Optional.empty(),
        Optional.ofNullable(this.level),
        Optional.ofNullable(this.color),
        Optional.ofNullable(this.boss)
      );
    }

    public Builder setRank(@Nullable Holder<Rank> rank) {
      this.rank = rank;
      return this;
    }

    public Builder setPrefixName(@Nullable Component prefixName) {
      this.prefixName = prefixName;
      return this;
    }

    public Builder setLevel(@Nullable Integer level) {
      this.level = level;
      return this;
    }

    public Builder setColor(@Nullable Integer color) {
      this.color = color;
      return this;
    }

    public Builder setBoss(@Nullable Boolean boss) {
      this.boss = boss;
      return this;
    }
  }
}
