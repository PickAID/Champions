package top.theillusivec4.champions.integration.theoneprobe.providers;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import top.theillusivec4.champions.integration.theoneprobe.ChampionsTheOneProbePlugin;
import top.theillusivec4.champions.integration.theoneprobe.elements.ElementStar;
import top.theillusivec4.champions.world.entity.champion.property.ChampionPropertyHelper;

public class ChampionPropertyEntityProvider implements IProbeInfoEntityProvider {
  public static ChampionPropertyEntityProvider create() {
    return new ChampionPropertyEntityProvider();
  }

  @Override
  public String getID() {
    return ChampionsTheOneProbePlugin.PROVIDER_CHAMPION_PROPERTY.toString();
  }

  @Override
  public void addProbeEntityInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, Entity entity, IProbeHitEntityData data) {
    TextColor color = ChampionPropertyHelper.getColor(entity);
    int tier = ChampionPropertyHelper.getTier(entity);
    if (tier > 0) {
      info.element(ElementStar.create(tier, color));
    }
  }

}
