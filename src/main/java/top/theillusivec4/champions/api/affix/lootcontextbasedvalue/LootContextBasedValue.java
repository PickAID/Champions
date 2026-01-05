package top.theillusivec4.champions.api.affix.lootcontextbasedvalue;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import top.theillusivec4.champions.common.registries.BuiltInRegistries;

import java.util.function.Function;

public interface LootContextBasedValue extends LootContextUser {
  Codec<LootContextBasedValue> CODEC = Codec.lazyInitialized(() -> {
    Codec<LootContextBasedValue> typedCodec = BuiltInRegistries.LOOT_CONTEXT_BASED_VALUE_TYPE.byNameCodec().dispatch(LootContextBasedValue::codec, Function.identity());
    return Codec.either(Constant.CODEC, typedCodec).xmap(either -> either.map(Function.identity(), Function.identity()), value -> value instanceof Constant constant ? Either.left(constant) : Either.right(value));
  });

  float calculate(LootContext context, int level);

  MapCodec<? extends LootContextBasedValue> codec();
}
