package top.theillusivec4.champions.common.event.customEvent;

import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.champions.api.IChampion;

public class ChampionsEventHooks {
  public static boolean onAttemptChampionSpawn(IChampion champion) {
    SpawnChampionEvent.Attempt event = new SpawnChampionEvent.Attempt(champion);
    return !NeoForge.EVENT_BUS.post(event).isCanceled();
  }

  public static boolean onAddSephiahName(IChampion champion, String sephiahName) {
    SpawnChampionEvent.AddSephiahName event = new SpawnChampionEvent.AddSephiahName(champion, sephiahName);
    boolean notCancelled = !NeoForge.EVENT_BUS.post(event).isCanceled();

    if (notCancelled && event.getName() != null && !event.getName().isEmpty()) {
      event.addSephirah();
    }

    return notCancelled;
  }

  public static boolean onPreChampionSpawn(IChampion champion) {
    SpawnChampionEvent.Pre event = new SpawnChampionEvent.Pre(champion);

    return !NeoForge.EVENT_BUS.post(event).isCanceled();
  }

  public static void onPostChampionSpawn(IChampion champion) {
    SpawnChampionEvent.Post event = new SpawnChampionEvent.Post(champion);
    NeoForge.EVENT_BUS.post(event);
  }
}
