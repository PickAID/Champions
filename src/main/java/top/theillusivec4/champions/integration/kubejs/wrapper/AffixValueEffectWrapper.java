package top.theillusivec4.champions.integration.kubejs.wrapper;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JavaOps;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.rhino.Context;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.api.affix.effect.AffixValueEffect;

public final class AffixValueEffectWrapper {
  private AffixValueEffectWrapper() {
  }

  public static AffixValueEffect wrap(Context context, @Nullable Object object) {
    return tryWrap(context, object).getOrThrow(KubeRuntimeException::new);
  }

  private static DataResult<AffixValueEffect> tryWrap(Context context, @Nullable Object object) {
    return AffixValueEffect.CODEC.decode(JavaOps.INSTANCE, object)
      .map(Pair::getFirst);
  }
}
