package top.theillusivec4.champions.world.loot.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.Locale;

public record EntitySpawnReasonCondition(List<EntitySpawnReason> reasons) implements LootItemCondition {
  private static final EntitySpawnReason[] REASONS = EntitySpawnReason.values();
  public static final MapCodec<EntitySpawnReasonCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.STRING.xmap(EntitySpawnReasonCondition::byName, EntitySpawnReasonCondition::getName).listOf().fieldOf("reasons").forGetter(EntitySpawnReasonCondition::reasons)
  ).apply(instance, EntitySpawnReasonCondition::new));

  private static EntitySpawnReason byName(String name) {
    for (EntitySpawnReason reason : REASONS) {
      if (reason.name().toLowerCase(Locale.ROOT).equals(name)) {
        return reason;
      }
    }
    return EntitySpawnReason.NATURAL;
  }

  private static String getName(EntitySpawnReason reason) {
    return reason.name().toLowerCase(Locale.ROOT);
  }

  @Override
  public MapCodec<? extends LootItemCondition> codec() {
    return MAP_CODEC;
  }

  @Override
  public boolean test(LootContext lootContext) {
    Entity entity = lootContext.getOptionalParameter(LootContextParams.THIS_ENTITY);
    return entity instanceof Mob mob && mob.getSpawnType() != null && this.reasons.contains(mob.getSpawnType());
  }
}
