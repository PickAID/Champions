package top.theillusivec4.champions.integration.probejs.docs;

import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.integration.probejs.util.ProbeJSUtil;

public final class LevelBasedValueDocs {

  private LevelBasedValueDocs() {
  }

  public static void assignType(ScriptDump scriptDump) {
    ProbeJSUtil.assignType(scriptDump, LevelBasedValue.class, context -> {
      context.direct(Types.typeOf(LevelBasedValue.class));
      context.direct(Types.NUMBER);
      context.dispatch("constant", builder -> builder.member("value", Types.NUMBER));
      context.dispatch("fraction", builder -> builder.member("numerator", Types.type(LevelBasedValue.class)).member("denominator", Types.type(LevelBasedValue.class)));
      context.dispatch("clamped", builder -> builder.member("value", Types.type(LevelBasedValue.class)).member("min", Types.NUMBER).member("max", Types.NUMBER));
      context.dispatch("levels_squared", builder -> builder.member("added", Types.NUMBER));
      context.dispatch("linear", builder -> builder.member("base", Types.type(LevelBasedValue.class)).member("per_level_above_first", Types.type(LevelBasedValue.class)));
      context.dispatch("lookup", builder -> builder.member("values", Types.NUMBER.asArray()).member("fallback", Types.type(LevelBasedValue.class)));
    });
  }
}
