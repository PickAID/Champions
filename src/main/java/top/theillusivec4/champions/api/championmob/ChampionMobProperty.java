package top.theillusivec4.champions.api.championmob;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import top.theillusivec4.champions.util.StreamCodecs;

public record ChampionMobProperty(int tier, TextColor color, Component prefix, boolean boss) {
  public static final ChampionMobProperty EMPTY = new ChampionMobProperty(0, TextColor.fromLegacyFormat(ChatFormatting.WHITE), Component.empty(), false);
  public static final MapCodec<ChampionMobProperty> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.intRange(0, 255).fieldOf("tier").forGetter(ChampionMobProperty::tier),
    TextColor.CODEC.fieldOf("color").forGetter(ChampionMobProperty::color),
    ComponentSerialization.CODEC.fieldOf("prefix").forGetter(ChampionMobProperty::prefix),
    Codec.BOOL.fieldOf("boss").forGetter(ChampionMobProperty::boss)
  ).apply(instance, ChampionMobProperty::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, ChampionMobProperty> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.INT, ChampionMobProperty::tier,
    StreamCodecs.TEXT_COLOR, ChampionMobProperty::color,
    ComponentSerialization.STREAM_CODEC, ChampionMobProperty::prefix,
    ByteBufCodecs.BOOL, ChampionMobProperty::boss,
    ChampionMobProperty::new
  );

  public static ChampionMobProperty.Builder builder() {
    return new Builder();
  }

  public ChampionMobProperty.Mutable mutable() {
    return new Mutable(this);
  }

  public static class Mutable {
    private int tier;
    private TextColor color;
    private Component prefix;
    private boolean boss;

    public Mutable(ChampionMobProperty championMobProperty) {
      this.tier = championMobProperty.tier;
      this.color = championMobProperty.color;
      this.prefix = championMobProperty.prefix;
    }

    public Mutable setTier(int tier) {
      if (tier > 0) {
        this.tier = tier;
      }
      return this;
    }

    public Mutable setColor(TextColor color) {
      this.color = color;
      return this;
    }

    public Mutable setPrefix(Component prefix) {
      this.prefix = prefix;
      return this;
    }

    public Mutable setBoss(boolean boss) {
      this.boss = boss;
      return this;
    }

    public ChampionMobProperty toImmutable() {
      return new ChampionMobProperty(this.tier, this.color, this.prefix, this.boss);
    }
  }

  public static class Builder {
    private int tier = 1;
    private TextColor color = TextColor.fromLegacyFormat(ChatFormatting.WHITE);
    private Component prefix = CommonComponents.EMPTY;
    private boolean boss = false;

    private Builder() {
    }

    public Builder tier(int tier) {
      if (tier <= 0) {
        throw new IllegalArgumentException("The tier must greater than 0");
      }
      this.tier = tier;
      return this;
    }

    public Builder color(TextColor color) {
      this.color = color;
      return this;
    }

    public Builder prefix(Component prefix) {
      this.prefix = prefix;
      return this;
    }

    public Builder boss(boolean boss) {
      this.boss = boss;
      return this;
    }

    public ChampionMobProperty build() {
      return new ChampionMobProperty(this.tier, this.color, this.prefix, this.boss);
    }
  }
}
