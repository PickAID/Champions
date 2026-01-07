package top.theillusivec4.champions.champion.affix.effect.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import top.theillusivec4.champions.registries.BuiltInRegistries;

import java.util.function.Function;

public interface AffixValueEffect extends LootContextUser {
  Codec<AffixValueEffect> CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.AFFIX_VALUE_EFFECT_TYPE.byNameCodec().dispatch(AffixValueEffect::codec, Function.identity()));

  float process(LootContext context, int level, float inputValue);

  MapCodec<? extends AffixValueEffect> codec();
}
