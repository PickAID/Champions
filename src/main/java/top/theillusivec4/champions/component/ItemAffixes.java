package top.theillusivec4.champions.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import top.theillusivec4.champions.champion.affix.Affix;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Deprecated
public class ItemAffixes {
  public static final ItemAffixes EMPTY = new ItemAffixes(List.of());
  public static final Codec<ItemAffixes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Affix.REFERENCE_CODEC.listOf().fieldOf("affixes").forGetter(ItemAffixes::affixes)
  ).apply(instance, ItemAffixes::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, ItemAffixes> STREAM_CODEC = StreamCodec.composite(
    Affix.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)), ItemAffixes::affixes,
    ItemAffixes::new
  );
  final List<Holder<Affix>> affixes;

  public ItemAffixes(List<Holder<Affix>> affixes) {
    this.affixes = affixes;
  }

  public boolean contains(Holder<Affix> affix) {
    return this.affixes.contains(affix);
  }

  public Mutable toMutable() {
    return new Mutable(this);
  }

  public List<Holder<Affix>> affixes() {
    return affixes;
  }

  @Override
  public int hashCode() {
    return Objects.hash(affixes);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (ItemAffixes) obj;
    return Objects.equals(this.affixes, that.affixes);
  }

  @Override
  public String toString() {
    return "ItemAffixes[" +
      "affixes=" + affixes + ']';
  }


  public static class Mutable extends ItemAffixes {

    public Mutable(ItemAffixes itemAffixes) {
      super(new ArrayList<>(itemAffixes.affixes));
    }

    public Mutable() {
      super(new ArrayList<>());
    }

    public Mutable add(Holder<Affix> affix) {
      if (!this.affixes.contains(affix)) {
        this.affixes.add(affix);
      }
      return this;
    }

    public Mutable remove(Holder<Affix> affix) {
      this.affixes.remove(affix);
      return this;
    }

    public ItemAffixes toImmutable() {
      return new ItemAffixes(List.copyOf(this.affixes));
    }
  }
}
