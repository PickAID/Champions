package top.theillusivec4.champions.world.entity.damagetracker;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.core.attachments.ChampionsAttachments;

import java.util.Objects;
import java.util.function.Consumer;

public final class DamageTrackerHelper {
  private DamageTrackerHelper() {
  }

  public static DamageTracker get(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.DAMAGE_TRACKER).orElse(DamageTracker.EMPTY);
  }

  public static void set(Entity entity, DamageTracker tracker) {
    if (!Objects.equals(get(entity), tracker)) {
      entity.setData(ChampionsAttachments.DAMAGE_TRACKER, tracker);
    }
  }

  public static void update(Entity entity, Consumer<DamageTracker.Mutable> consumer) {
    DamageTracker.Mutable mutable = get(entity).mutable();
    consumer.accept(mutable);
    set(entity, mutable.toImmutable());
  }

  public static void record(Entity entity, Holder<DamageType> type) {
    update(entity, mutable -> {
      mutable.add(type);
      if (mutable.size() > 3) {
        mutable.removeFirst();
      }
    });
  }
}
