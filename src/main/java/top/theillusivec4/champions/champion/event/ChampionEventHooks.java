package top.theillusivec4.champions.champion.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.champions.champion.ChampionHandler;

public final class ChampionEventHooks {
  public static void onUpdateAffixesPre(Entity entity, ServerLevel serverLevel, ChampionHandler handler) {
    ChampionEvent.UpdateAffixes.Pre event = new ChampionEvent.UpdateAffixes.Pre(entity, serverLevel, handler);
    NeoForge.EVENT_BUS.post(event);
  }

  public static void onUpdateAffixesPost(Entity entity, ServerLevel serverLevel, ChampionHandler handler) {
    ChampionEvent.UpdateAffixes.Post event = new ChampionEvent.UpdateAffixes.Post(entity, serverLevel, handler);
    NeoForge.EVENT_BUS.post(event);
  }

  private ChampionEventHooks() {
  }
}
