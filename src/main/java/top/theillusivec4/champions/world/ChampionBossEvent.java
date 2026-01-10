package top.theillusivec4.champions.world;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.ChampionDefaultProperties;

import java.util.List;
import java.util.UUID;

public abstract class ChampionBossEvent {
  protected final UUID id;
  private Component name;
  private float progress;
  private int level;
  private int color;
  private List<Holder<Affix>> affixes = List.of();

  public ChampionBossEvent(UUID id, Component name) {
    this.id = id;
    this.name = name;
  }

  public ChampionBossEvent(UUID id, Component name, float progress, int level, int color, List<Holder<Affix>> affixes) {
    this.id = id;
    this.name = name;
    this.progress = Math.clamp(progress, 0.0f, 1.0f);
    this.level = Math.clamp(level, ChampionDefaultProperties.MIN_LEVEL, ChampionDefaultProperties.MAX_LEVEL);
    this.color = ARGB.opaque(color);
    this.affixes = List.copyOf(affixes);
  }

  public UUID getId() {
    return id;
  }

  public List<Holder<Affix>> getAffixes() {
    return affixes;
  }

  public void setAffixes(List<Holder<Affix>> affixes) {
    this.affixes = List.copyOf(affixes);
  }

  public Component getName() {
    return name;
  }

  public void setName(Component name) {
    this.name = name;
  }

  public float getProgress() {
    return progress;
  }

  public void setProgress(float progress) {
    this.progress = Math.clamp(progress, 0.0f, 1.0f);
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = Math.clamp(level, ChampionDefaultProperties.MIN_LEVEL, ChampionDefaultProperties.MAX_LEVEL);
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = ARGB.opaque(color);
  }
}
