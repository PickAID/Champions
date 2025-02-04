package top.theillusivec4.champions.common.integration.kubejs.wrapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.MinMaxBounds;
import org.jetbrains.annotations.Nullable;

public class MinMaxBoundsIntsJS {
  public static MinMaxBounds.Ints of(@Nullable Object object) {
    switch (object) {
      case MinMaxBounds.Ints ints -> {
        return ints;
      }
      case JsonArray jsonArray -> {
        if (jsonArray.size() >= 2) {
          int min = jsonArray.get(0).getAsInt();
          int max = jsonArray.get(1).getAsInt();
          return MinMaxBounds.Ints.between(min, max);
        } else if (jsonArray.size() == 1) {
          return MinMaxBounds.Ints.exactly(jsonArray.get(0).getAsInt());
        } else {
          return MinMaxBounds.Ints.ANY; // JsonArray size == 0 应该允许这种情况吗?
        }
      }
      case JsonElement jsonElement -> {
        return MinMaxBounds.Ints.CODEC.decode(JsonOps.INSTANCE, jsonElement).result().orElseThrow().getFirst();
      }
      case null, default -> {
        return MinMaxBounds.Ints.ANY;
      }
    }
  }
}
