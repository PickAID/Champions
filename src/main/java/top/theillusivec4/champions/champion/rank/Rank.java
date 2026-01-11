package top.theillusivec4.champions.champion.rank;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import top.theillusivec4.champions.server.champion.config.ChampionDefaultConfigs;
import top.theillusivec4.champions.registry.Registries;

public record Rank(Component description, int level, int color) {
  public static final Codec<Rank> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("description").forGetter(Rank::description),
    Codec.intRange(ChampionDefaultConfigs.MIN_LEVEL, ChampionDefaultConfigs.MAX_LEVEL).fieldOf("level").forGetter(Rank::level),
    Codec.INT.fieldOf("color").forGetter(Rank::color)
  ).apply(instance, Rank::new));
  public static final MapCodec<Rank> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("description").forGetter(Rank::description),
    Codec.intRange(ChampionDefaultConfigs.MIN_LEVEL, ChampionDefaultConfigs.MAX_LEVEL).fieldOf("level").forGetter(Rank::level),
    Codec.INT.fieldOf("color").forGetter(Rank::color)
  ).apply(instance, Rank::new));
  public static final Codec<Holder<Rank>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.RANK, DIRECT_CODEC);
  public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Rank>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.RANK);

  public static Rank.Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private int level = 1;
    private int color = -1;

    public Rank build(Identifier id) {
      return new Rank(
        Component.translatable(Util.makeDescriptionId("rank", id)),
        this.level,
        this.color
      );
    }

    public Builder setLevel(int level) {
      this.level = level;
      return this;
    }

    public Builder setColor(String color) {
      this.color = TextColor.parseColor(color)
        .result()
        .orElse(TextColor.fromRgb(-1)).getValue();
      return this;
    }

    public Builder setColor(int color) {
      this.color = color;
      return this;
    }
  }
}
