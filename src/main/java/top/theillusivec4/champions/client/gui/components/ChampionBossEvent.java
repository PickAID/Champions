package top.theillusivec4.champions.client.gui.components;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.champion.affix.Affix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ChampionBossEvent {
  private final List<Holder<Affix>> affixes = new ArrayList<>();
  private final List<Holder<Affix>> affixesView = Collections.unmodifiableList(this.affixes);
  private Component name;
  private float progress = 1.0f;
  private int level = 1;
  private int color = -1;
  private @Nullable Component affixesComponent;

  public ChampionBossEvent(Component name) {
    this.name = name;
  }

  public void addAffix(Holder<Affix> affix) {
    this.affixes.add(affix);
  }

  public void removeAffix(Holder<Affix> affix) {
    this.affixes.add(affix);
  }

  public Component getName() {
    return name;
  }

  public void setName(Component name) {
    this.name = name;
  }

  public Component getAffixesComponent() {
    if (this.affixesComponent == null) {
      MutableComponent mutableComponent = Component.literal("");
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

  public float getProgress() {
    return progress;
  }

  public void setProgress(float progress) {
    this.progress = progress;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = Math.max(level, 1);
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = ARGB.opaque(color);
  }
}
