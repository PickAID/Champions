package top.theillusivec4.champions.client.gui.components;

import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.champion.affix.Affix;

import java.util.*;

public class ChampionClientBossEvent extends LerpingBossEvent {
  private final List<Holder<Affix>> affixes = new ArrayList<>();
  private final List<Holder<Affix>> affixesView = Collections.unmodifiableList(this.affixes);
  private int level;
  private int color;
  private @Nullable Component affixesComponent = null;

  public ChampionClientBossEvent(UUID id, Component name, float progress, BossBarColor color, BossBarOverlay overlay, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {
    super(id, name, progress, color, overlay, darkenScreen, playMusic, createWorldFog);
    this.color = Objects.requireNonNull(color.getFormatting().getColor());
  }

  @Override
  public void setColor(BossBarColor color) {
    super.setColor(color);
    this.color = Objects.requireNonNull(color.getFormatting().getColor());
  }

  public void addAffix(Holder<Affix> affix) {
    this.affixes.add(affix);
  }

  public void removeAffix(Holder<Affix> affix) {
    this.affixes.remove(affix);
  }

  public void refreshAffixesComponent() {
    this.affixesComponent = null;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = Math.max(level, 1);
  }

  public List<Holder<Affix>> getAffixes() {
    return affixesView;
  }

  public Component getAffixesComponent() {
    if (this.affixesComponent == null) {
      MutableComponent mutableComponent = Component.empty().copy();
      for (int i = 0; i < this.affixes.size(); i++) {
        Holder<Affix> affix = this.affixes.get(i);
        mutableComponent.append(affix.value().description());
        if (i + 1 < this.affixes.size()) {
          mutableComponent.append(CommonComponents.space());
        }
      }

      this.affixesComponent = mutableComponent;
    }
    return this.affixesComponent;
  }

  public int color() {
    return this.color;
  }
}
