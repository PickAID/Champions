package top.theillusivec4.champions.integration.probejs.util;

import moe.wolfgirl.probejs.lang.typescript.ScriptDump;

import java.util.function.Consumer;

public final class DocUtil {

  private DocUtil() {
  }

  public static void assignType(ScriptDump scriptDump, Class<?> aClass, Consumer<TypeAssigner.Builder> context) {
    TypeAssigner.Builder builder = TypeAssigner.builder(aClass);
    context.accept(builder);
    builder.build().assignType(scriptDump);
  }

}
