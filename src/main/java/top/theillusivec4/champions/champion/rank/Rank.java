package top.theillusivec4.champions.champion.rank;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.registry.Registries;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @param description
 */
public record Rank(
  Component description,
  MinMaxBounds.Ints level,
  int weight,
  List<TextColor> colors,
  TextColor defaultColor,
  HolderSet<Affix> affixes,
  boolean boss,
  Optional<ResourceKey<LootTable>> lootTable
) {
  public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Rank>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.RANK);
  private static final List<TextColor> COLORS = List.of(
    Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.WHITE)),
    Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)),
    Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.AQUA)),
    Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE)),
    Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.GOLD))
  );
  public static final Codec<Rank> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("description").forGetter(Rank::description),
    MinMaxBounds.Ints.CODEC.fieldOf("level").forGetter(Rank::level),
    Codec.intRange(1, 1024).optionalFieldOf("weight", 5).forGetter(Rank::weight),
    TextColor.CODEC.listOf().optionalFieldOf("colors", COLORS).forGetter(Rank::colors),
    TextColor.CODEC.optionalFieldOf("default_color", Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.WHITE))).forGetter(Rank::defaultColor),
    RegistryCodecs.homogeneousList(Registries.AFFIX).optionalFieldOf("affixes", HolderSet.empty()).forGetter(Rank::affixes),
    Codec.BOOL.optionalFieldOf("boss", false).forGetter(Rank::boss),
    ResourceKey.codec(net.minecraft.core.registries.Registries.LOOT_TABLE).optionalFieldOf("loot_table").forGetter(Rank::lootTable)
  ).apply(instance, Rank::new));
  public static final Codec<Holder<Rank>> REFERENCE_CODEC = RegistryFileCodec.create(Registries.RANK, DIRECT_CODEC);
  public static final MapCodec<Rank> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("description").forGetter(Rank::description),
    MinMaxBounds.Ints.CODEC.fieldOf("level").forGetter(Rank::level),
    Codec.intRange(1, 1024).optionalFieldOf("weight", 5).forGetter(Rank::weight),
    TextColor.CODEC.listOf().optionalFieldOf("colors", COLORS).forGetter(Rank::colors),
    TextColor.CODEC.optionalFieldOf("default_color", Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.WHITE))).forGetter(Rank::defaultColor),
    RegistryCodecs.homogeneousList(Registries.AFFIX).optionalFieldOf("affixes", HolderSet.empty()).forGetter(Rank::affixes),
    Codec.BOOL.optionalFieldOf("boss", false).forGetter(Rank::boss),
    ResourceKey.codec(net.minecraft.core.registries.Registries.LOOT_TABLE).optionalFieldOf("loot_table").forGetter(Rank::lootTable)
  ).apply(instance, Rank::new));

  public static Rank.Builder builder() {
    return new Builder();
  }

  public TextColor getColor(int level) {
    return level <= this.colors.size() ? this.colors.get(level - 1) : this.defaultColor;
  }

  public boolean matches(int championLevel) {
    return this.level.matches(championLevel);
  }

  @SuppressWarnings("unused")
  public static class Builder {
    private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
    private int weight = 5;
    private List<TextColor> colors = COLORS;
    private TextColor defaultColor = Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.WHITE));
    private HolderSet<Affix> affixes = HolderSet.empty();
    private boolean boss;
    private @Nullable ResourceKey<LootTable> lootTable;

    public Rank build(Identifier id) {
      return new Rank(
        Component.translatable(Util.makeDescriptionId("rank", id)),
        this.level,
        this.weight,
        this.colors,
        this.defaultColor,
        this.affixes,
        this.boss,
        Optional.ofNullable(this.lootTable)
      );
    }

    public Builder setColors(List<TextColor> colors) {
      this.colors = colors;
      return this;
    }

    public Builder setDefaultColor(TextColor defaultColor) {
      this.defaultColor = defaultColor;
      return this;
    }

    public Builder setLootTable(@Nullable ResourceKey<LootTable> lootTable) {
      this.lootTable = lootTable;
      return this;
    }

    public Builder setBoss(boolean boss) {
      this.boss = boss;
      return this;
    }

    public Builder setAffixes(HolderSet<Affix> affixes) {
      this.affixes = affixes;
      return this;
    }

    public Builder setLevel(MinMaxBounds.Ints level) {
      this.level = level;
      return this;
    }

    public Builder setWeight(int weight) {
      this.weight = Math.clamp(weight, 1, 1024);
      return this;
    }
  }
}
