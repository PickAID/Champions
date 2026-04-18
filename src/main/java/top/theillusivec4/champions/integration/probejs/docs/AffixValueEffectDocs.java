package top.theillusivec4.champions.integration.probejs.docs;

import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.api.affix.effect.AffixValueEffect;
import top.theillusivec4.champions.integration.probejs.util.DocUtil;

public final class AffixValueEffectDocs {
  public static void assignType(ScriptDump scriptDump) {
    DocUtil.assignType(scriptDump, AffixValueEffect.class, context -> {
      context.direct(Types.typeOf(AffixValueEffect.class));
      context.dispatch("add", builder -> builder.member("value", Types.type(LevelBasedValue.class)));
      context.dispatch("all_of", builder -> builder.member("effects", Types.type(LevelBasedValue.class).asArray()));
      context.dispatch("multiply", builder -> builder.member("factor", Types.type(LevelBasedValue.class)));
      context.dispatch("set", builder -> builder.member("value", Types.type(LevelBasedValue.class)));
      context.dispatch("remove_binomial", builder -> builder.member("chance", Types.type(LevelBasedValue.class)));
    });
  }
}
