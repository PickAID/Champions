
package top.theillusivec4.champions.common.event.customEvent;

import net.minecraftforge.eventbus.api.Event;
import top.theillusivec4.champions.api.IChampion;

public abstract class SpawnChampionEvent extends Event {
  IChampion champion;

  public IChampion getChampion() {
    return champion;
  }

  public static class Attempt extends SpawnChampionEvent {

    public Attempt(IChampion champion) {
      this.champion = champion;
    }

  }

  public static class Pre extends SpawnChampionEvent {
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
