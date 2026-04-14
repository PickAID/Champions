package top.theillusivec4.champions.world.entity.champion.property;

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
import top.theillusivec4.champions.util.ChampionsStreamCodecs;

public record ChampionProperty(int tier, TextColor color, Component prefix, boolean boss) {
  public static final ChampionProperty EMPTY = new ChampionProperty(0, TextColor.fromLegacyFormat(ChatFormatting.WHITE), Component.empty(), false);
  public static final MapCodec<ChampionProperty> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.intRange(0, 255).fieldOf("tier").forGetter(ChampionProperty::tier),
    TextColor.CODEC.fieldOf("color").forGetter(ChampionProperty::color),
    ComponentSerialization.CODEC.fieldOf("prefix").forGetter(ChampionProperty::prefix),
    Codec.BOOL.fieldOf("boss").forGetter(ChampionProperty::boss)
  ).apply(instance, ChampionProperty::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, ChampionProperty> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.INT, ChampionProperty::tier,
    ChampionsStreamCodecs.TEXT_COLOR, ChampionProperty::color,
    ComponentSerialization.STREAM_CODEC, ChampionProperty::prefix,
    ByteBufCodecs.BOOL, ChampionProperty::boss,
    ChampionProperty::new
  );

  public static Builder builder() {
    return new Builder();
  }

  public Mutable mutable() {
    return new Mutable(this);
  }

  public static class Mutable {
    private int tier;
    private TextColor color;
    private Component prefix;
    private boolean boss;

    public Mutable(ChampionProperty championProperty) {
      this.tier = championProperty.tier;
      this.color = championProperty.color;
      this.prefix = championProperty.prefix;
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

    public ChampionProperty toImmutable() {
      return new ChampionProperty(this.tier, this.color, this.prefix, this.boss);
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

    public ChampionProperty build() {
      return new ChampionProperty(this.tier, this.color, this.prefix, this.boss);
    }
  }
}
