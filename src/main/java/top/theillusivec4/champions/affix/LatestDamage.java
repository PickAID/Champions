package top.theillusivec4.champions.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record LatestDamage(Optional<Holder<DamageType>> damageType, int damageCount, int latestTime, float originalDamageAmount) {
  public static final LatestDamage EMPTY = new LatestDamage(Optional.empty(), 0, 0, 0);
  public static final MapCodec<LatestDamage> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    DamageType.CODEC.optionalFieldOf("damage_type").forGetter(LatestDamage::damageType),
    Codec.intRange(0, 99).fieldOf("damage_count").forGetter(LatestDamage::damageCount),
    Codec.INT.fieldOf("latest_time").forGetter(LatestDamage::latestTime),
    Codec.FLOAT.fieldOf("original_damage_amount").forGetter(LatestDamage::originalDamageAmount)
  ).apply(instance, LatestDamage::new));

  public LatestDamage.Mutable toMutable() {
    return new Mutable(this);
  }

  public static class Mutable {
    private @Nullable Holder<DamageType> damageType;
    private int damageCount;
    private int latestTime;
    private float originalDamageAmount;

    public Mutable(LatestDamage latestDamage) {
      this.damageType = latestDamage.damageType().orElse(null);
      this.damageCount = latestDamage.damageCount;
      this.latestTime = latestDamage.latestTime;
    }

    public Mutable() {
    }

    public LatestDamage toImmutable() {
      return new LatestDamage(Optional.ofNullable(this.damageType), this.damageCount, this.latestTime, this.originalDamageAmount);
    }

    public float getOriginalDamageAmount() {
      return originalDamageAmount;
    }

    public Mutable setOriginalDamageAmount(float originalDamageAmount) {
      this.originalDamageAmount = originalDamageAmount;
      return this;
    }

    public @Nullable Holder<DamageType> getDamageType() {
      return damageType;
    }

    public Mutable setDamageType(@Nullable Holder<DamageType> damageType) {
      this.damageType = damageType;
      return this;
    }

    public int getLatestTime() {
      return latestTime;
    }

    public Mutable setLatestTime(int latestTime) {
      this.latestTime = latestTime;
      return this;
    }

    public int getDamageCount() {
      return damageCount;
    }

    public Mutable setDamageCount(int damageCount) {
      this.damageCount = damageCount;
      return this;
    }
  }
}
