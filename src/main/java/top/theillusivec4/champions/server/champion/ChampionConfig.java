package top.theillusivec4.champions.server.champion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.Difficulty;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;
import top.theillusivec4.champions.champion.DifficultyBasedValue;

import java.util.Optional;

public record ChampionConfig(DifficultyBasedValue minLevel, DifficultyBasedValue maxLevel) {
  public static final Codec<ChampionConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    DifficultyBasedValue.CODEC.fieldOf("min_level").forGetter(ChampionConfig::minLevel),
    DifficultyBasedValue.CODEC.fieldOf("max_level").forGetter(ChampionConfig::maxLevel)
  ).apply(instance, ChampionConfig::new));
  public static final Codec<Optional<WithConditions<ChampionConfig>>> WITH_CONDITIONS_CODEC = ConditionalOps.createConditionalCodecWithConditions(CODEC);

  public static Builder builder() {
    return new Builder();
  }

  public int calculateLevel(Difficulty difficulty, int championLevel) {
    return (int) Math.clamp(championLevel, this.minLevel.calculate(difficulty), this.maxLevel.calculate(difficulty));
  }

  public static class Builder {
    private DifficultyBasedValue minLevel = DifficultyBasedValue.linear(DifficultyBasedValue.constant(0), DifficultyBasedValue.constant(1));
    private DifficultyBasedValue maxLevel = DifficultyBasedValue.linear(DifficultyBasedValue.constant(1), DifficultyBasedValue.constant(1));

    public ChampionConfig build() {
      return new ChampionConfig(this.minLevel, this.maxLevel);
    }

    public Builder setMinLevel(DifficultyBasedValue minLevel) {
      this.minLevel = minLevel;
      return this;
    }

    public Builder setMaxLevel(DifficultyBasedValue maxLevel) {
      this.maxLevel = maxLevel;
      return this;
    }
  }

  public record ChampionConfigProvider(DifficultBasedLevel difficultBasedLevel) {

    public record DifficultBasedLevel(int base, int perDifficulty) {

    }

  }
}
