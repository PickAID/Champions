package top.theillusivec4.champions.champion.rank;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.Util;
import top.theillusivec4.champions.champion.value.based.lootcontext.LevelBasedValue;
import top.theillusivec4.champions.registry.Registries;

import java.util.List;

/**
 * 冠军生物的等级或“稀有度”
 *
 * @param description
 * @param level
 * @param color
 */
public record Rank(Component description, MinMaxBounds.Ints level, Rank.Color color, LevelBasedValue minAffix, LevelBasedValue maxAffix, int weight) {
  public static final MapCodec<Rank> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("description").forGetter(Rank::description),
    MinMaxBounds.Ints.CODEC.optionalFieldOf("level", MinMaxBounds.Ints.between(1, 5)).forGetter(Rank::level),
    Color.CODEC.optionalFieldOf("color", Color.INSTANCE).forGetter(Rank::color),
    LevelBasedValue.CODEC.optionalFieldOf("min_affix", LevelBasedValue.linear(LevelBasedValue.constant(1), LevelBasedValue.constant(1))).forGetter(Rank::minAffix),
    LevelBasedValue.CODEC.optionalFieldOf("min_affix", LevelBasedValue.constant(5)).forGetter(Rank::maxAffix),
    Codec.intRange(1, 1024).optionalFieldOf("weight", 5).forGetter(Rank::weight)
  ).apply(instance, Rank::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Rank>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.RANK);
  private static final MinMaxBounds.Ints LEVEL = MinMaxBounds.Ints.between(1, 5);
  private static final LevelBasedValue MIN_AFFIX = LevelBasedValue.linear(LevelBasedValue.constant(1), LevelBasedValue.constant(1));
  private static final LevelBasedValue MAX_AFFIX = LevelBasedValue.constant(5);
  public static final Codec<Rank> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("description").forGetter(Rank::description),
    MinMaxBounds.Ints.CODEC.optionalFieldOf("level", LEVEL).forGetter(Rank::level),
    Color.CODEC.optionalFieldOf("color", Color.INSTANCE).forGetter(Rank::color),
    LevelBasedValue.CODEC.optionalFieldOf("min_affix", MIN_AFFIX).forGetter(Rank::minAffix),
    LevelBasedValue.CODEC.optionalFieldOf("min_affix", MAX_AFFIX).forGetter(Rank::maxAffix),
    Codec.intRange(1, 1024).optionalFieldOf("weight", 5).forGetter(Rank::weight)
  ).apply(instance, Rank::new));
  public static final Codec<Holder<Rank>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.RANK, DIRECT_CODEC);

  public static Rank.Builder builder() {
    return new Builder();
  }

  public TextColor getColor(int level) {
    return level <= this.color.colors.size() ? this.color.colors.get(level - 1) : this.color.fallback;
  }

  public record Color(List<TextColor> colors, TextColor fallback) {
    @SuppressWarnings("DataFlowIssue")
    public static final Color INSTANCE = new Color(
      List.of(
        TextColor.fromLegacyFormat(ChatFormatting.WHITE),
        TextColor.fromLegacyFormat(ChatFormatting.YELLOW),
        TextColor.fromLegacyFormat(ChatFormatting.AQUA),
        TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE)
      ),
      TextColor.fromLegacyFormat(ChatFormatting.WHITE)
    );
    public static final Codec<Color> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      TextColor.CODEC.listOf().fieldOf("colors").forGetter(Color::colors),
      TextColor.CODEC.fieldOf("fallback").forGetter(Color::fallback)
    ).apply(instance, Color::new));
  }

  public static class Builder {
    private MinMaxBounds.Ints level = LEVEL;
    private Rank.Color color = Color.INSTANCE;
    private LevelBasedValue minAffix = MIN_AFFIX;
    private LevelBasedValue maxAffix = MAX_AFFIX;
    private int weight = 5;


    public Rank build(Identifier id) {
      return new Rank(
        Component.translatable(Util.makeDescriptionId("rank", id)),
        this.level,
        this.color,
        this.minAffix,
        this.maxAffix,
        this.weight
      );
    }

    public Builder setMinAffix(LevelBasedValue minAffix) {
      this.minAffix = minAffix;
      return this;
    }

    public Builder setMaxAffix(LevelBasedValue maxAffix) {
      this.maxAffix = maxAffix;
      return this;
    }

    public Builder setLevel(MinMaxBounds.Ints level) {
      this.level = level;
      return this;
    }

    public Builder setColor(Rank.Color color) {
      this.color = color;
      return this;
    }

    public Builder setWeight(int weight) {
      this.weight = Math.clamp(weight, 1, 1024);
      return this;
    }
  }
}
