package top.theillusivec4.champions.common.integration.kubejs.events.spawn;

import dev.latvian.mods.kubejs.entity.KubeLivingEntityEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.api.IChampion;

public abstract class SpawnChampionEventJs implements KubeLivingEntityEvent {
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

  public static class AddSephiahName extends Attempt {
    private final ResourceLocation mobId;
    private String sephirahName;

    public AddSephiahName(IChampion champion, String sephirahName) {
      super(champion);
      this.sephirahName = sephirahName;
      this.mobId = BuiltInRegistries.ENTITY_TYPE.getKey(champion.getLivingEntity().getType());
    }

    public String getName() {
      return sephirahName;
    }

    public void setName(String sephirahName) {
      this.sephirahName = sephirahName;
    }

    public ResourceLocation getMobId() {
      return mobId;
    }

  }

  public static class Post extends SpawnChampionEventJs {
    public Post(IChampion champion) {
      super(champion);
    }
  }
}
