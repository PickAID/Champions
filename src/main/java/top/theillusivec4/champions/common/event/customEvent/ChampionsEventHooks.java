package top.theillusivec4.champions.common.event.customEvent;

import net.minecraftforge.common.MinecraftForge;
import top.theillusivec4.champions.api.IChampion;

public class ChampionsEventHooks {
	public static boolean onAttemptChampionSpawn(IChampion champion) {
		SpawnChampionEvent.Attempt event = new SpawnChampionEvent.Attempt(champion);
		return !MinecraftForge.EVENT_BUS.post(event);
	}

	public static boolean onPreChampionSpawn(IChampion champion) {
		SpawnChampionEvent.Pre event = new SpawnChampionEvent.Pre(champion);

		return !MinecraftForge.EVENT_BUS.post(event);
	}

	public static void onPostChampionSpawn(IChampion champion) {
		SpawnChampionEvent.Post event = new SpawnChampionEvent.Post(champion);
		MinecraftForge.EVENT_BUS.post(event);
	}
}
