package top.theillusivec4.champions.common.affix;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.AffixData;
import top.theillusivec4.champions.common.affix.core.CombatAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.config.ConfigEnums;

import java.util.List;

public class AdaptableAffix extends CombatAffix {
  @Override
  public float onHurt(IChampion champion, DamageSource source, float amount, float newAmount) {
    String type = source.getMsgId();
    DamageData damageData = AffixData.getData(champion, this.toString(), DamageData.class);

    if (damageData.name.equalsIgnoreCase(type)) {
      newAmount -= (float) (amount * ChampionsConfig.adaptableDamageReductionIncrement * damageData.count);
      damageData.count++;
    } else {
      damageData.name = type;
      damageData.count = 0;
    }
    damageData.saveData();
    return Math.max(amount * (float) (1.0f - ChampionsConfig.adaptableMaxDamageReduction),
      newAmount);
  }

  @Override
  public AffixSetting createDefaultSetting() {
    return AffixSetting.builder()
      .withDefault()
      .setCategory(AffixCategory.DEFENSE)
      .setHasSub(false)
      .setMobList(List.of(
        ResourceLocation.withDefaultNamespace("pig"),
        ResourceLocation.withDefaultNamespace("creeper")
      ))
      .setMobPermission(ConfigEnums.Permission.BLACKLIST)
      .build();
  }

  public static class DamageData extends AffixData {
    String name;
    int count;

    @Override
    public void readFromNBT(CompoundTag tag) {
      name = tag.getString("name").orElse("unknown");
      count = tag.getInt("count").orElse(0);
    }

    @Override
    public CompoundTag writeToNBT() {
      CompoundTag compound = new CompoundTag();
      compound.putString("name", name);
      compound.putInt("count", count);
      return compound;
    }
  }
}
