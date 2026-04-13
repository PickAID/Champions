package top.theillusivec4.champions.world.entity.champion.property;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import top.theillusivec4.champions.world.entity.affix.EntityAffixes;

import java.util.UUID;

public abstract class ChampionsBossEvent {
  private final UUID id;
  private Component name;
  private float progress = 1.0f;
  private int level;
  private TextColor color;
  private EntityAffixes affixes = EntityAffixes.EMPTY;

  public ChampionsBossEvent(UUID id, Component name, float progress, int level, TextColor color, EntityAffixes affixes) {
    this.id = id;
    this.name = name;
    this.progress = progress;
    this.level = level;
    this.color = color;
    this.affixes = affixes;
  }

  public UUID getId() {
    return id;
  }

  public ChampionsBossEvent(UUID id, Component name) {
    this.id = id;
    this.name = name;
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

  public int getTier() {
    return level;
  }

  public void setTier(int level) {
    this.level = level;
  }

  public TextColor getColor() {
    return color;
  }

  public void setColor(TextColor color) {
    this.color = color;
  }

  public EntityAffixes getAffixes() {
    return affixes;
  }

  public void setAffixes(EntityAffixes affixes) {
    this.affixes = affixes;
  }
}
