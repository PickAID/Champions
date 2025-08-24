package top.theillusivec4.champions.common.integration.kubejs.events;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import top.theillusivec4.champions.common.event.customEvent.SpawnChampionEvent;
import top.theillusivec4.champions.common.integration.kubejs.events.spawn.SpawnChampionEventJs;

public class NeoForgeJsEventHandler {
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onAttemptSpawnChampion(SpawnChampionEvent.Attempt event) {

    if (EventHandlers.attemptSpawn.hasListeners()) {
      var eventJs = new SpawnChampionEventJs.Attempt(event.getChampion());
      EventHandlers.attemptSpawn.post(eventJs);

      // 同步取消 forge event
      if (eventJs.isCanceled()) {
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onPreSpawnChampion(SpawnChampionEvent.Pre event) {

    if (EventHandlers.preSpawn.hasListeners()) {
      var eventJs = new SpawnChampionEventJs.Pre(event.getChampion());
      EventHandlers.preSpawn.post(eventJs);

      // 同步取消 forge event
      if (eventJs.isCancelled()) {
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onPostSpawnChampion(SpawnChampionEvent.Post event) {

    if (EventHandlers.postSpawn.hasListeners()) {
      var eventJs = new SpawnChampionEventJs.Post(event.getChampion());
      EventHandlers.postSpawn.post(eventJs);
    }
  }
}
