package top.theillusivec4.champions.champion.rank;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.Optional;

/**
 *
 * @param description
 */
public record Rank(
  Component description,
  MinMaxBounds.Ints level,
  int weight,
  boolean boss,
  Optional<ResourceKey<LootTable>> lootTable
) {
  public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Rank>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ChampionsRegistries.RANK);
  public static final Codec<Rank> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("description").forGetter(Rank::description),
    MinMaxBounds.Ints.CODEC.fieldOf("level").forGetter(Rank::level),
    Codec.intRange(1, 1024).optionalFieldOf("weight", 5).forGetter(Rank::weight),
    Codec.BOOL.optionalFieldOf("boss", false).forGetter(Rank::boss),
    ResourceKey.codec(net.minecraft.core.registries.Registries.LOOT_TABLE).optionalFieldOf("loot_table").forGetter(Rank::lootTable)
  ).apply(instance, Rank::new));
  public static final Codec<Holder<Rank>> REFERENCE_CODEC = RegistryFileCodec.create(ChampionsRegistries.RANK, DIRECT_CODEC);
  public static final MapCodec<Rank> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("description").forGetter(Rank::description),
    MinMaxBounds.Ints.CODEC.fieldOf("level").forGetter(Rank::level),
    Codec.intRange(1, 1024).optionalFieldOf("weight", 5).forGetter(Rank::weight),
    Codec.BOOL.optionalFieldOf("boss", false).forGetter(Rank::boss),
    ResourceKey.codec(net.minecraft.core.registries.Registries.LOOT_TABLE).optionalFieldOf("loot_table").forGetter(Rank::lootTable)
  ).apply(instance, Rank::new));

  public static Rank.Builder builder() {
    return new Builder();
  }

  public boolean matches(int championLevel) {
    return this.level.matches(championLevel);
  }

  @SuppressWarnings("unused")
  public static class Builder {
    private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
    private int weight = 5;
    private boolean boss;
    private @Nullable ResourceKey<LootTable> lootTable;

    public Rank build(Identifier id) {
      return new Rank(
        Component.translatable(Util.makeDescriptionId("rank", id)),
        this.level,
        this.weight,
        this.boss,
        Optional.ofNullable(this.lootTable)
      );
    }

    public Builder setLootTable(ResourceKey<LootTable> lootTable) {
      this.lootTable = lootTable;
      return this;
    }

    public Builder setBoss(boolean boss) {
      this.boss = boss;
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
