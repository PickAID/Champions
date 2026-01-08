package top.theillusivec4.champions.champion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import top.theillusivec4.champions.champion.affix.Affix;

import java.util.ArrayList;
import java.util.List;

public class Affixes {
  public static final Affixes EMPTY = new Affixes();
  public static final StreamCodec<RegistryFriendlyByteBuf, Affixes> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.collection(ArrayList::new, Affix.STREAM_CODEC), Affixes::getAffixes,
    Affixes::new
  );
  private static final Codec<List<Holder<Affix>>> AFFIXES_CODEC = Affix.REFERENCE_CODEC.listOf();
  public static final Codec<Affixes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    AFFIXES_CODEC.fieldOf("affixes").forGetter(Affixes::getAffixes)
  ).apply(instance, Affixes::new));

  public static final MapCodec<Affixes> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    AFFIXES_CODEC.fieldOf("affixes").forGetter(Affixes::getAffixes)
  ).apply(instance, Affixes::new));
  private final List<Holder<Affix>> affixes;

  public static Affixes.Mutable mutable() {
    return new Mutable();
  }

  public Affixes() {
    this.affixes = List.of();
  }

  public Affixes(List<Holder<Affix>> affixes) {
    this.affixes = List.copyOf(affixes);
  }

  public boolean contains(Holder<Affix> affix) {
    return affixes.contains(affix);
  }

  public Affixes.Mutable toMutable() {
    return new Mutable(this);
  }

  public Affixes copy() {
    return new Affixes(this.affixes);
  }

  @Override
  public int hashCode() {
    return affixes.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else {
      return obj instanceof Affixes champion && this.affixes.equals(champion.affixes);
    }
  }

  @Override
  public String toString() {
    return "EntityChampion{affixes=" + this.affixes + "}";
  }

  public boolean isEmpty() {
    return this.affixes.isEmpty();
  }

  public List<Holder<Affix>> getAffixes() {
    return affixes;
  }

  public static class Mutable {
    private final List<Holder<Affix>> affixes;

    public Mutable(Affixes affixes) {
      this.affixes = new ArrayList<>(affixes.affixes);
    }

    public Mutable() {
      this.affixes = new ArrayList<>();
    }

    public void add(Holder<Affix> affix) {
      affixes.add(affix);
    }

    public boolean contains(Holder<Affix> affix) {
      return affixes.contains(affix);
    }

    public void remove(Holder<Affix> affix) {
      affixes.remove(affix);
    }

    public Affixes toImmutable() {
      return this.affixes.isEmpty() ? Affixes.EMPTY : new Affixes(this.affixes);
    }
  }
}
