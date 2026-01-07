package top.theillusivec4.champions.champion.affix.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.EntityEvent;
import top.theillusivec4.champions.champion.ChampionHandler;

public abstract class ChampionEvent extends EntityEvent {
  private final ServerLevel level;
  private final ChampionHandler handler;

  public ChampionEvent(Entity entity, ServerLevel level, ChampionHandler handler) {
    super(entity);
    this.level = level;
    this.handler = handler;
  }

  public ServerLevel getLevel() {
    return this.level;
  }

  public ChampionHandler getHandler() {
    return handler;
  }

  public static abstract class UpdateAffixes extends ChampionEvent {

    public UpdateAffixes(Entity entity, ServerLevel level, ChampionHandler handler) {
      super(entity, level, handler);
    }

    public static class Pre extends UpdateAffixes {

      public Pre(Entity entity, ServerLevel level, ChampionHandler handler) {
        super(entity, level, handler);
      }
    }

    public static class Post extends UpdateAffixes {

      public Post(Entity entity, ServerLevel level, ChampionHandler handler) {
        super(entity, level, handler);
      }
    }

  }

}
