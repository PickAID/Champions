package top.theillusivec4.champions.common.integration.kubejs.events.spawn;

import dev.latvian.mods.kubejs.entity.LivingEntityEventJS;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.api.IChampion;

public abstract class SpawnChampionEventJs extends LivingEntityEventJS {
  private final IChampion champion;

  protected SpawnChampionEventJs(IChampion champion) {
    this.champion = champion;
  }

  public IChampion getChampion() {
    return champion;
  }

  @Override
  public LivingEntity getEntity() {
    return champion.getLivingEntity();
  }

  public static class Attempt extends SpawnChampionEventJs {
    private boolean cancelled;

    public Attempt(IChampion champion) {
      super(champion);
    }

    public boolean isCanceled() {
      return cancelled;
    }

    public void setCanceled(boolean cancelled) {
      this.cancelled = cancelled;
    }

  }

  public static class Pre extends SpawnChampionEventJs {
    private boolean cancelled;

    public Pre(IChampion champion) {
      super(champion);
    }

    public void setCanceled(boolean cancelled) {
      this.cancelled = cancelled;
    }

    public boolean isCancelled() {
      return cancelled;
    }
  }

  public static class Post extends SpawnChampionEventJs {
    public Post(IChampion champion) {
      super(champion);
    }
  }
}
