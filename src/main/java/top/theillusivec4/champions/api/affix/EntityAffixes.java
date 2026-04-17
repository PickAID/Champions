package top.theillusivec4.champions.api.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class EntityAffixes {
  public static final EntityAffixes EMPTY = new EntityAffixes(Object2IntMaps.emptyMap());
  public static final StreamCodec<RegistryFriendlyByteBuf, EntityAffixes> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.map(Object2IntOpenHashMap::new, Affix.STREAM_CODEC, ByteBufCodecs.VAR_INT),
    container -> container.affixes,
    EntityAffixes::new
  );
  private static final Codec<Object2IntMap<Holder<Affix>>> AFFIXES_CODEC = Codec.unboundedMap(Affix.REFERENCE_CODEC, Codec.intRange(0, 255)).xmap(Object2IntOpenHashMap::new, Function.identity());
  public static final MapCodec<EntityAffixes> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    AFFIXES_CODEC.fieldOf("affixes").forGetter(container -> container.affixes)
  ).apply(instance, EntityAffixes::new));

  private final Object2IntMap<Holder<Affix>> affixes;

  private EntityAffixes(Object2IntMap<Holder<Affix>> affixes) {
    this.affixes = affixes;
  }

  public static EntityAffixes.Builder builder() {
    return new Builder();
  }

  public EntityAffixes.Mutable mutable() {
    return new Mutable(this);
  }

  public Set<Holder<Affix>> keySet() {
    return Collections.unmodifiableSet(this.affixes.keySet());
  }

  public Set<Map.Entry<Holder<Affix>, Integer>> entrySet() {
    return Collections.unmodifiableSet(this.affixes.object2IntEntrySet());
  }

  public boolean isEmpty() {
    return this.affixes.isEmpty();
  }

  @Override
  public int hashCode() {
    int i = this.affixes.hashCode();
    return 31 * i;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else {
      return obj instanceof EntityAffixes that && this.affixes.equals(that.affixes);
    }
  }

  @Override
  public String toString() {
    return "AffixContainer{" + this.affixes + "}";
  }

  public static class Mutable {
    private final Object2IntMap<Holder<Affix>> affixes = new Object2IntOpenHashMap<>();

    private Mutable(EntityAffixes container) {
      this.affixes.putAll(container.affixes);
    }

    public Mutable() {
    }

    public void upgrade(Holder<Affix> affix, int level) {
      if (level > 0) {
        this.affixes.merge(affix, Math.min(level, 255), Integer::max);
      }
    }

    public void removeIf(Predicate<Holder<Affix>> predicate) {
      this.affixes.keySet().removeIf(predicate);
    }

    public void remove(Holder<Affix> affix) {
      this.affixes.removeInt(affix);
    }

    public int getLevel(Holder<Affix> affix) {
      return this.affixes.getOrDefault(affix, 0);
    }

    public void set(Holder<Affix> affix, int level) {
      if (level <= 0) {
        this.affixes.removeInt(affix);
      } else {
        this.affixes.put(affix, Math.min(level, 255));
      }
    }

    public Set<Holder<Affix>> keySet() {
      return this.affixes.keySet();
    }

    public EntityAffixes toImmutable() {
      return new EntityAffixes(this.affixes);
    }
  }

  public static class Builder {
    private final Object2IntMap<Holder<Affix>> affixes = new Object2IntOpenHashMap<>();

    private Builder() {
    }

    public Builder add(Holder<Affix> affix, int level) {
      if (level <= 0 || level > 255) {
        throw new IllegalArgumentException("affix level must greater than 0 and less than 255.");
      }
      this.affixes.put(affix, level);
      return this;
    }

    public Builder add(AffixInstance instance) {
      return add(instance.affix(), instance.level());
    }

    public EntityAffixes build() {
      return new EntityAffixes(new Object2IntOpenHashMap<>(this.affixes));
    }
  }

}
