package top.theillusivec4.champions.champion.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageType;

@SuppressWarnings("unused")
public record Damage(Holder<DamageType> damageType, int damageCount, int latestTime, float damageAmount) {
  public static final MapCodec<Damage> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    DamageType.CODEC.fieldOf("damage_type").forGetter(Damage::damageType),
    Codec.INT.fieldOf("damage_count").forGetter(Damage::damageCount),
    Codec.INT.fieldOf("latest_time").forGetter(Damage::latestTime),
    Codec.FLOAT.fieldOf("damage_amount").forGetter(Damage::damageAmount)
  ).apply(instance, Damage::new));

  public Damage.Mutable toMutable() {
    return new Mutable(this);
  }

  public static class Mutable {
    private Holder<DamageType> damageType;
    private int damageCount;
    private int latestTime;
    private float originalDamageAmount;

    public Mutable(Damage damage) {
      this.damageType = damage.damageType();
      this.damageCount = damage.damageCount;
      this.latestTime = damage.latestTime;
    }

    public Damage toImmutable() {
      return new Damage(this.damageType, this.damageCount, this.latestTime, this.originalDamageAmount);
    }

    public float getOriginalDamageAmount() {
      return originalDamageAmount;
    }

    public void setOriginalDamageAmount(float originalDamageAmount) {
      this.originalDamageAmount = originalDamageAmount;
    }

    public Holder<DamageType> getDamageType() {
      return damageType;
    }

    public Mutable setDamageType(Holder<DamageType> damageType) {
      this.damageType = damageType;
      return this;
    }

    public int getLatestTime() {
      return latestTime;
    }

    public void setLatestTime(int latestTime) {
      this.latestTime = latestTime;
    }

    public int getDamageCount() {
      return damageCount;
    }

    public void setDamageCount(int damageCount) {
      this.damageCount = damageCount;
    }
  }
}
