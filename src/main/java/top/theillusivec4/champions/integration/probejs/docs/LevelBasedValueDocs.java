package top.theillusivec4.champions.integration.probejs.docs;

import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSObjectType;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.util.ChampionsUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class LevelBasedValueDocs {
  private static final Map<ResourceLocation, BaseType> TYPE_MAP = new HashMap<>();

  static {
    register("constant", builder -> builder.member("value", Types.NUMBER));
    register("exponent", builder -> builder.member("base", Types.typeOf(LevelBasedValue.class)).member("power", Types.typeOf(LevelBasedValue.class)));
    register("fraction", builder -> builder.member("numerator", Types.typeOf(LevelBasedValue.class)).member("denominator", Types.typeOf(LevelBasedValue.class)));
    register("summation", builder -> builder.member("augend", Types.typeOf(LevelBasedValue.class)).member("addend", Types.typeOf(LevelBasedValue.class)));
    register("product", builder -> builder.member("multiplicand", Types.typeOf(LevelBasedValue.class)).member("multiplier", Types.typeOf(LevelBasedValue.class)));
    register("linear", builder -> builder.member("base", Types.typeOf(LevelBasedValue.class)).member("per_level_above_first", Types.typeOf(LevelBasedValue.class)));
  }

  private LevelBasedValueDocs() {
  }

  public static void assignType(ScriptDump scriptDump) {
    scriptDump.assignType(LevelBasedValue.class, Types.NUMBER);
    for (BaseType type : TYPE_MAP.values()) {
      scriptDump.assignType(LevelBasedValue.class, type);
    }
  }

  private static void register(String name, UnaryOperator<JSObjectType.Builder> operator) {
    ResourceLocation id = ChampionsUtil.id(name);
    TYPE_MAP.put(id, operator.apply(Types.object().member("type", Types.literal(id.toString()))).build());
  }
}
