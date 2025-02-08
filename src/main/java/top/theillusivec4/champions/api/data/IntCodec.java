package top.theillusivec4.champions.api.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.MinMaxBounds;

public class IntCodec {
    private static Codec<MinMaxBounds.Ints> CODEC;

    public static Codec<MinMaxBounds.Ints> codec() {
        if (CODEC == null) {
            CODEC = Codec.PASSTHROUGH.xmap(
                    // 从 JsonElement 转换到 MinMaxBounds.Ints
                    (dynamic) -> MinMaxBounds.Ints.fromJson(dynamic.convert(JsonOps.INSTANCE).getValue()),
                    // 从 MinMaxBounds.Ints 转换到 JsonElement
                    (bounds) -> new Dynamic<>(JsonOps.INSTANCE, bounds.serializeToJson())
            );
        }
        return CODEC;
    }
}
