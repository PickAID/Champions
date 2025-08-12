package top.theillusivec4.champions.common.integration.kubejs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import top.theillusivec4.champions.common.integration.kubejs.events.spawn.SpawnChampionEventJs;

public class EventHandlers {
  public static final EventGroup ChampionsJs = EventGroup.of("ChampionsJs");
  public static final EventHandler attemptSpawn = ChampionsJs.server("attemptSpawn", () -> SpawnChampionEventJs.Attempt.class);
  public static final EventHandler addSephiahName = ChampionsJs.server("addSephiahName", () -> SpawnChampionEventJs.AddSephiahName.class);
  public static final EventHandler preSpawn = ChampionsJs.server("preSpawn", () -> SpawnChampionEventJs.Pre.class);
  public static final EventHandler postSpawn = ChampionsJs.server("postSpawn", () -> SpawnChampionEventJs.Post.class);
}
