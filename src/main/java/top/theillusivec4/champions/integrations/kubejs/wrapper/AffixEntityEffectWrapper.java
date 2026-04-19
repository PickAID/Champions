package top.theillusivec4.champions.integrations.kubejs.wrapper;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JavaOps;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.rhino.Context;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.api.affix.effect.AffixEntityEffect;
import top.theillusivec4.champions.api.affix.effect.AffixValueEffect;

public final class AffixEntityEffectWrapper {
  private AffixEntityEffectWrapper() {
  }

  public static AffixEntityEffect wrap(Context context, @Nullable Object object) {
    return tryWrap(context, object).getOrThrow(KubeRuntimeException::new);
  }

  private static DataResult<AffixEntityEffect> tryWrap(Context context, @Nullable Object object) {
    return AffixEntityEffect.CODEC.decode(JavaOps.INSTANCE, object)
      .map(Pair::getFirst);
  }
}
