package top.theillusivec4.champions.integration.theoneprobe.providers;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import top.theillusivec4.champions.integration.theoneprobe.ChampionsTheOneProbePlugin;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.AffixHelper;

import java.util.Map;

public class EntityAffixesInfoEntityProvider implements IProbeInfoEntityProvider {

  public static EntityAffixesInfoEntityProvider create() {
    return new EntityAffixesInfoEntityProvider();
  }

  @Override
  public String getID() {
    return ChampionsTheOneProbePlugin.PROVIDER_ENTITY_AFFIXES.toString();
  }

  @Override
  public void addProbeEntityInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, Entity entity, IProbeHitEntityData data) {
    for (Map.Entry<Holder<Affix>, Integer> entry : AffixHelper.get(entity).entrySet()) {
      info.horizontal().mcText(Affix.getFullName(entry.getKey(), entry.getValue()));
    }
  }
}
