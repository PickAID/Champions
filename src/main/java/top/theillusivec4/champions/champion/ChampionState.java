package top.theillusivec4.champions.champion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import top.theillusivec4.champions.util.ChampionsStreamCodecs;

public record ChampionState(int tier, TextColor color, Component prefix, boolean boss) {
  public static final ChampionState EMPTY = new ChampionState(0,  TextColor.fromLegacyFormat(ChatFormatting.WHITE), Component.empty(), false);
  public static final MapCodec<ChampionState> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.intRange(0, 255).fieldOf("tier").forGetter(ChampionState::tier),
    TextColor.CODEC.fieldOf("color").forGetter(ChampionState::color),
    ComponentSerialization.CODEC.fieldOf("prefix").forGetter(ChampionState::prefix),
    Codec.BOOL.fieldOf("boss").forGetter(ChampionState::boss)
  ).apply(instance, ChampionState::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, ChampionState> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.INT, ChampionState::tier,
    ChampionsStreamCodecs.TEXT_COLOR, ChampionState::color,
    ComponentSerialization.STREAM_CODEC, ChampionState::prefix,
    ByteBufCodecs.BOOL, ChampionState::boss,
    ChampionState::new
  );

  public ChampionState.Mutable mutable() {
    return new Mutable(this);
  }

  public static class Mutable {
    private int tier;
    private TextColor color;
    private Component prefix;
    private boolean boss;

    public Mutable(ChampionState championState) {
      this.tier = championState.tier;
      this.color = championState.color;
      this.prefix = championState.prefix;
    }

    public Mutable setTier(int tier) {
      this.tier = tier;
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

    public ChampionState toImmutable() {
      return new ChampionState(this.tier, this.growthFactor, this.color, this.prefix, this.boss);
    }
  }
}
