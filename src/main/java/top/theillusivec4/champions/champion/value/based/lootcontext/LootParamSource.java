package top.theillusivec4.champions.champion.value.based.lootcontext;

import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

import java.util.Set;

@Deprecated
public interface LootParamSource<T> extends LootContextUser {
  ContextKey<?> key();

  T provide(LootContext context);

  @Override
  default Set<ContextKey<?>> getReferencedContextParams() {
    return Set.of(key());
  }
}
