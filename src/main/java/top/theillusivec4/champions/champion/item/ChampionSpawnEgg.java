package top.theillusivec4.champions.champion.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.capability.Capabilities;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.registry.Registries;

import java.util.Optional;
import java.util.function.Function;

public record ChampionSpawnEgg(Holder<Item> item, Holder<Rank> rank, Optional<Component> prefix, Optional<Integer> level, Optional<Integer> color, HolderSet<Affix> affixes) {
  private static final Codec<Integer> STRING_COLOR_CODEC = Codec.STRING.comapFlatMap(string -> TextColor.parseColor(string).map(TextColor::getValue), integer -> TextColor.fromRgb(integer).serialize());
  private static final Codec<Integer> INT_COLOR_CODEC = Codec.INT.xmap(ARGB::opaque, Function.identity());
  private static final Codec<Integer> COLOR_CODEC = Codec.withAlternative(STRING_COLOR_CODEC, INT_COLOR_CODEC);
  private static final Codec<Holder<Item>> SPAWN_EGG_CODEC = Item.CODEC.validate(item -> Capabilities.ChampionHandlers.isImplemented(item.value()) ? DataResult.success(item) : DataResult.error(() -> "This item %s cannot be used as a Champion Spawn Egg".formatted(item.getRegisteredName())));

  public static final Codec<ChampionSpawnEgg> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Item.CODEC.fieldOf("item").forGetter(ChampionSpawnEgg::item),
    Rank.REFERENCE_CODEC.fieldOf("rank").forGetter(ChampionSpawnEgg::rank),
    ComponentSerialization.CODEC.optionalFieldOf("prefix").forGetter(ChampionSpawnEgg::prefix),
    Codec.intRange(1, 5).optionalFieldOf("level").forGetter(ChampionSpawnEgg::level),
    COLOR_CODEC.optionalFieldOf("color").forGetter(ChampionSpawnEgg::color),
    Affix.LIST_CODEC.fieldOf("affixes").forGetter(ChampionSpawnEgg::affixes)
  ).apply(instance, ChampionSpawnEgg::new));

  public static Builder builder(Item item) {
    return new Builder(item.builtInRegistryHolder());
  }

  public ItemStack getSpawnEgg(Level level) {
    ItemStack itemStack = new ItemStack(this.item);
    ChampionUtil.getHandler(itemStack).ifPresent(handler -> {
      handler.setRank(this.rank);
      this.prefix.ifPresent(handler::setPrefixName);
      this.level.ifPresent(handler::setLevel);
      this.level.ifPresent(handler::setColor);
      handler.updateAffixes(mutable -> {
        for (Holder<Affix> affix : this.affixes) {
          mutable.add(affix);
        }
      });
    });

    return itemStack;
  }

  public static class Builder {
    private Holder<Item> item;
    private @Nullable Holder<Rank> rank;
    private @Nullable ResourceKey<Rank> rankKey;
    private @Nullable Component prefix;
    private @Nullable Integer level;
    private @Nullable Integer color;
    private @Nullable HolderSet<Affix> affixes;

    public Builder(Holder<Item> item) {
      this.item = item;
    }

    public ChampionSpawnEgg build(BootstrapContext<ChampionSpawnEgg> context) {
      HolderGetter<Rank> ranks = context.lookup(Registries.RANK);
      return new ChampionSpawnEgg(
        this.item,
        this.rank != null ? this.rank : ranks.getOrThrow(Ranks.EMPTY),
        Optional.ofNullable(this.prefix),
        Optional.ofNullable(this.level),
        Optional.ofNullable(this.color),
        this.affixes != null ? this.affixes : HolderSet.empty()
      );
    }

    public Builder setItem(Holder<Item> item) {
      this.item = item;
      return this;
    }

    public Builder setRank(Holder<Rank> rank) {
      this.rank = rank;
      return this;
    }

    public Builder setPrefix(Component prefix) {
      this.prefix = prefix;
      return this;
    }

    public Builder setLevel(int level) {
      this.level = level;
      return this;
    }

    public Builder setColor(int color) {
      this.color = color;
      return this;
    }

    public Builder setAffixes(HolderSet<Affix> affixes) {
      this.affixes = affixes;
      return this;
    }
  }
}
