package top.theillusivec4.champions.client.gui.components;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.world.ChampionBossEvent;

import java.util.List;
import java.util.UUID;

public class ClientChampionBossEvent extends ChampionBossEvent {
  public ClientChampionBossEvent(UUID id, Component name) {
    super(id, name);
  }

  public ClientChampionBossEvent(UUID id, Component name, float progress, int level, int color, List<Holder<Affix>> affixes) {
    super(id, name, progress, level, color, affixes);
  }

  public Component getAffixesComponent() {
    MutableComponent mutableComponent = Component.empty().copy();
    for (int i = 0; i < this.getAffixes().size(); i++) {
      Holder<Affix> affix = this.getAffixes().get(i);
      mutableComponent.append(affix.value().description());
      if (i + 1 < this.getAffixes().size()) {
        mutableComponent.append(CommonComponents.space());
      }
    }
    return mutableComponent;
  }
}
