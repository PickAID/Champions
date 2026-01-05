package top.theillusivec4.champions.api.affix.effect;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.phys.Vec3;

public interface AffixLocationBasedEffect extends LootContextUser {
  void onChangedBlock(LootContext context, int level, AffixPosition position, Entity entity, Vec3 contextPosition);
}
