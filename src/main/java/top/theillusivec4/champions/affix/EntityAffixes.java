package top.theillusivec4.champions.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public class EntityAffixes {
  public static final EntityAffixes EMPTY = new EntityAffixes();
  public static final StreamCodec<RegistryFriendlyByteBuf, EntityAffixes> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.collection(ArrayList::new, Affix.STREAM_CODEC), EntityAffixes::getAffixes,
    EntityAffixes::new
  );
  private static final Codec<List<Holder<Affix>>> AFFIXES_CODEC = Affix.REFERENCE_CODEC.listOf();
  public static final MapCodec<EntityAffixes> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    AFFIXES_CODEC.fieldOf("affixes").forGetter(EntityAffixes::getAffixes)
  ).apply(instance, EntityAffixes::new));
  private final List<Holder<Affix>> affixes;

  public static EntityAffixes.Mutable mutable() {
    return new Mutable();
  }

  public EntityAffixes() {
    this.affixes = List.of();
  }

  private EntityAffixes(List<Holder<Affix>> affixes) {
    this.affixes = List.copyOf(affixes);
  }

  public boolean contains(Holder<Affix> affix) {
    return affixes.contains(affix);
  }

  public EntityAffixes.Mutable toMutable() {
    return new Mutable(this);
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
      return obj instanceof EntityAffixes champion && this.affixes.equals(champion.affixes);
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

  public static class Mutable extends EntityAffixes {
    private final List<Holder<Affix>> affixes;

    public Mutable(EntityAffixes entityAffixes) {
      this.affixes = new ArrayList<>(entityAffixes.affixes);
    }

    public Mutable() {
      this.affixes = new ArrayList<>();
    }

    public void add(Holder<Affix> affix) {
      affixes.add(affix);
    }

    public void remove(Holder<Affix> affix) {
      affixes.remove(affix);
    }

    public EntityAffixes toImmutable() {
      return this.affixes.isEmpty() ? EntityAffixes.EMPTY : new EntityAffixes(this.affixes);
    }
  }
}
