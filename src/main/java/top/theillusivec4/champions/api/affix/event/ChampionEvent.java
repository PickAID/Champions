package top.theillusivec4.champions.api.affix.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.EntityEvent;
import top.theillusivec4.champions.api.IChampionHandler;

public abstract class ChampionEvent extends EntityEvent {
  private final ServerLevel level;
  private final IChampionHandler handler;

  public ChampionEvent(Entity entity, ServerLevel level, IChampionHandler handler) {
    super(entity);
    this.level = level;
    this.handler = handler;
  }

  public ServerLevel getLevel() {
    return this.level;
  }

  public IChampionHandler getHandler() {
    return handler;
  }

  public static abstract class UpdateAffixes extends ChampionEvent {

    public UpdateAffixes(Entity entity, ServerLevel level, IChampionHandler handler) {
      super(entity, level, handler);
    }

    public static class Pre extends UpdateAffixes {

      public Pre(Entity entity, ServerLevel level, IChampionHandler handler) {
        super(entity, level, handler);
      }
    }

    public static class Post extends UpdateAffixes {

      public Post(Entity entity, ServerLevel level, IChampionHandler handler) {
        super(entity, level, handler);
      }
    }

  }

}
