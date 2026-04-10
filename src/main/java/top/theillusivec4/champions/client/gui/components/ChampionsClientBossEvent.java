package top.theillusivec4.champions.client.gui.components;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import top.theillusivec4.champions.affix.Affix;
import top.theillusivec4.champions.affix.AffixContainer;
import top.theillusivec4.champions.champion.ChampionsBossEvent;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ChampionsClientBossEvent extends ChampionsBossEvent {
  public ChampionsClientBossEvent(UUID id, Component name) {
    super(id, name);
  }

  public ChampionsClientBossEvent(UUID id, Component name, float progress, int level, TextColor color, AffixContainer affixes) {
    super(id, name, progress, level, color, affixes);
  }

  public Component getAffixesComponent() {
    MutableComponent component = Component.empty().copy();

    Iterator<Map.Entry<Holder<Affix>, Integer>> iterator = this.getAffixes().entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<Holder<Affix>, Integer> entry = iterator.next();
      component.append(Affix.getFullName(entry.getKey(), entry.getValue()));
      if (iterator.hasNext()) {
        component.append(CommonComponents.SPACE);
      }
    }

    return component;
  }
}
