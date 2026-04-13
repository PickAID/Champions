package top.theillusivec4.champions.extralootparam;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageType;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class DamageTracker {
  public static final DamageTracker EMPTY = new DamageTracker(Object2IntMaps.emptyMap(), Optional.empty());
  private static final Codec<Object2IntMap<Holder<DamageType>>> DAMAGES = Codec.unboundedMap(DamageType.CODEC, Codec.intRange(1, Integer.MAX_VALUE)).xmap(Object2IntOpenHashMap::new, Function.identity());
  public static final MapCodec<DamageTracker> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(DAMAGES.fieldOf("damages").forGetter(tracker -> tracker.damages), DamageType.CODEC.optionalFieldOf("last").forGetter(tracker -> tracker.last)).apply(instance, DamageTracker::new));
  private final Object2IntMap<Holder<DamageType>> damages;
  private final Optional<Holder<DamageType>> last;

  private DamageTracker(Object2IntMap<Holder<DamageType>> damages, Optional<Holder<DamageType>> last) {
    this.damages = damages;
    this.last = last;
  }

  public DamageTracker.Mutable mutable() {
    return new Mutable(this);
  }

  public int getCount(Holder<DamageType> type) {
    return damages.getOrDefault(type, 0);
  }

  public Optional<Holder<DamageType>> getLast() {
    return last;
  }

  @Override
  public int hashCode() {
    return Objects.hash(damages);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (DamageTracker) obj;
    return Objects.equals(this.damages, that.damages);
  }

  @Override
  public String toString() {
    return "DamageTracker[" + "damages=" + damages + ']';
  }

  public static class Mutable {
    private final Object2IntMap<Holder<DamageType>> damages = new Object2IntOpenHashMap<>();
    private Holder<DamageType> last = null;

    private Mutable(DamageTracker tracker) {
      this.damages.putAll(tracker.damages);
    }

    public void add(Holder<DamageType> type) {
      damages.merge(type, 1, Integer::sum);
    }

    public void setLast(Holder<DamageType> last) {
      this.last = last;
    }

    public void remove(Holder<DamageType> type) {
      int value = damages.getInt(type);
      if (value <= 1) {
        damages.removeInt(type);
      } else {
        damages.merge(type, -1, Integer::sum);
      }
    }

    public void set(Holder<DamageType> type, int value) {
      damages.put(type, value);
    }

    public DamageTracker toImmutable() {
      return new DamageTracker(damages, Optional.ofNullable(last));
    }
  }

}
