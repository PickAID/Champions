package top.theillusivec4.champions.common.event.customEvent;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.util.ChampionBuilder;

public abstract class SpawnChampionEvent extends Event {
  IChampion champion;

  public IChampion getChampion() {
    return champion;
  }

  public static class Attempt extends SpawnChampionEvent implements ICancellableEvent {

    public Attempt(IChampion champion) {
      this.champion = champion;
    }

  }

  public static class AddSephiahName extends Attempt implements ICancellableEvent {
    String sephiahName;

    public AddSephiahName(IChampion champion, String sephiahName) {
      super(champion);
      this.sephiahName = sephiahName;
    }

    public String getName() {
      return sephiahName;
    }

    public void setName(String sephiahName) {
      this.sephiahName = sephiahName;
    }

    public boolean addSephirah() {
      if (sephiahName == null || sephiahName.isEmpty()) return false;
      return ChampionBuilder.addSephirah(champion.getLivingEntity(), sephiahName);
    }
  }

  public static class Pre extends SpawnChampionEvent implements ICancellableEvent {
    public Pre(IChampion champion) {
      this.champion = champion;
    }

  }

  public static class Post extends SpawnChampionEvent {

    public Post(IChampion champion) {
      this.champion = champion;
    }

  }

}
