package top.theillusivec4.champions.world.entity.damagetracker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageType;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public final class DamageTracker {
  public static final DamageTracker EMPTY = new DamageTracker(new Object2IntLinkedOpenHashMap<>());
  private static final Codec<Object2IntLinkedOpenHashMap<Holder<DamageType>>> DAMAGES = Codec.unboundedMap(DamageType.CODEC, Codec.intRange(1, Integer.MAX_VALUE)).xmap(Object2IntLinkedOpenHashMap::new, Function.identity());
  public static final MapCodec<DamageTracker> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    DAMAGES.fieldOf("damages").forGetter(tracker -> tracker.damages)
  ).apply(instance, DamageTracker::new));
  private final Object2IntLinkedOpenHashMap<Holder<DamageType>> damages;

  private DamageTracker(Object2IntLinkedOpenHashMap<Holder<DamageType>> damages) {
    this.damages = damages;
  }

  public DamageTracker.Mutable mutable() {
    return new Mutable(this);
  }

  public int size() {
    return damages.size();
  }

  public int getCount(Holder<DamageType> type) {
    return damages.getOrDefault(type, 0);
  }

  public @Nullable Holder<DamageType> getFirst() {
    return damages.firstKey();
  }

  public @Nullable Holder<DamageType> getLast() {
    return damages.lastKey();
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
    private final Object2IntLinkedOpenHashMap<Holder<DamageType>> damages = new Object2IntLinkedOpenHashMap<>();

    private Mutable(DamageTracker tracker) {
      this.damages.putAll(tracker.damages);
    }

    public int getCount(Holder<DamageType> type) {
      return damages.getOrDefault(type, 0);
    }

    public int size() {
      return damages.size();
    }

    public void removeFirst() {
      damages.removeFirstInt();
    }

    public void removeLast() {
      damages.removeLastInt();
    }

    public @Nullable Holder<DamageType> getLast() {
      return damages.lastKey();
    }

    public @Nullable Holder<DamageType> getFirst() {
      return damages.firstKey();
    }

    public void add(Holder<DamageType> type) {
      damages.merge(type, 1, Integer::sum);
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
      return new DamageTracker(damages);
    }
  }

}
