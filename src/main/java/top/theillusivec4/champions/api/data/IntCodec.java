package top.theillusivec4.champions.api.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.criterion.MinMaxBounds;

public class IntCodec {
    private static Codec<MinMaxBounds.IntBound> CODEC;

    public static Codec<MinMaxBounds.IntBound> codec() {
        if (CODEC == null) {
            CODEC = Codec.PASSTHROUGH.xmap(
                    // 从 JsonElement 转换到 MinMaxBounds.Ints
                    (dynamic) -> MinMaxBounds.IntBound.fromJson(dynamic.convert(JsonOps.INSTANCE).getValue()),
                    // 从 MinMaxBounds.Ints 转换到 JsonElement
                    (bounds) -> new Dynamic<>(JsonOps.INSTANCE, bounds.serializeToJson())
            );
        }
        return CODEC;
    }
}
