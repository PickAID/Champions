package top.theillusivec4.champions.champion.value.based.difficulty;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.DifficultyInstance;

public interface DifficultyBasedValue {
  float calculate(DifficultyInstance difficulty);

  MapCodec<? extends DifficultyBasedValue> codec();
}
