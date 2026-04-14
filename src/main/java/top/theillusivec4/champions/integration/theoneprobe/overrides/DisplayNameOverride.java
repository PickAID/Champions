package top.theillusivec4.champions.integration.theoneprobe.overrides;

import mcjty.theoneprobe.api.IEntityDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import top.theillusivec4.champions.integration.theoneprobe.elements.ElementColoredText;
import top.theillusivec4.champions.world.entity.champion.property.ChampionPropertyHelper;

public class DisplayNameOverride implements IEntityDisplayOverride {
  public static DisplayNameOverride create() {
    return new DisplayNameOverride();
  }

  @Override
  public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, Entity entity, IProbeHitEntityData data) {
    int tier = ChampionPropertyHelper.getTier(entity);
    if (tier > 0) {
      info.element(
        ElementColoredText.create(ChampionPropertyHelper.getDisplayName(entity), ChampionPropertyHelper.getColor(entity))
      );
      return true;
    }
    return false;
  }
}
