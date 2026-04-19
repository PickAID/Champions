package top.theillusivec4.champions.integrations.probejs.docs;

import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import top.theillusivec4.champions.api.affix.effect.AffixLocationBasedEffect;
import top.theillusivec4.champions.integrations.probejs.util.ProbeJSUtil;

public final class AffixLocationBasedEffectDocs {
  private AffixLocationBasedEffectDocs() {
  }

  public static void assignType(ScriptDump scriptDump) {
    ProbeJSUtil.assignType(scriptDump, AffixLocationBasedEffect.class, context -> {
      context.direct(Types.typeOf(AffixLocationBasedEffect.class));
    });
  }
}
