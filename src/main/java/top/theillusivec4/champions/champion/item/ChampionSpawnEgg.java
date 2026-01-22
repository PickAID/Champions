package top.theillusivec4.champions.champion.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.registry.Registries;

import java.util.Optional;

@SuppressWarnings("unused")
public record ChampionSpawnEgg(Holder<Item> item, Optional<Holder<Rank>> rank, Optional<Component> prefix, Optional<Integer> level, int color, HolderSet<Affix> affixes) {

  public static final Codec<ChampionSpawnEgg> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Item.CODEC.fieldOf("item").forGetter(ChampionSpawnEgg::item),
    Rank.REFERENCE_CODEC.optionalFieldOf("rank").forGetter(ChampionSpawnEgg::rank),
    ComponentSerialization.CODEC.optionalFieldOf("prefix").forGetter(ChampionSpawnEgg::prefix),
    Codec.intRange(1, 5).optionalFieldOf("level").forGetter(ChampionSpawnEgg::level),
    Codec.INT.optionalFieldOf("color", -1).forGetter(ChampionSpawnEgg::color),
    Affix.LIST_CODEC.fieldOf("affixes").forGetter(ChampionSpawnEgg::affixes)
  ).apply(instance, ChampionSpawnEgg::new));

  public static Builder builder(Item item) {
    return new Builder(BuiltInRegistries.ITEM.wrapAsHolder(item));
  }

  public ItemStack getSpawnEgg(Level level) {
    ItemStack itemStack = new ItemStack(this.item);
    ChampionUtil.getHandler(itemStack).ifPresent(handler -> {
      this.prefix.ifPresent(handler::setPrefixName);
      this.level.ifPresent(handler::setLevel);
      handler.setColor(this.color);
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
    private int color = -1;
    private @Nullable HolderSet<Affix> affixes;

    public Builder(Holder<Item> item) {
      this.item = item;
    }

    public ChampionSpawnEgg build(BootstrapContext<ChampionSpawnEgg> context) {
      HolderGetter<Rank> ranks = context.lookup(Registries.RANK);
      return new ChampionSpawnEgg(
        this.item,
        Optional.ofNullable(this.rank),
        Optional.ofNullable(this.prefix),
        Optional.ofNullable(this.level),
        this.color,
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
