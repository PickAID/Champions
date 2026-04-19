package top.theillusivec4.champions.integrations.probejs.util;

import com.google.common.collect.ImmutableList;
import moe.wolfgirl.probejs.lang.java.clazz.ClassPath;
import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import moe.wolfgirl.probejs.lang.typescript.code.type.js.JSObjectType;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.util.Util;

import java.util.function.UnaryOperator;

public record TypeAssigner(ClassPath path, ImmutableList<BaseType> types) {
  public static TypeAssigner.Builder builder(Class<?> aClass) {
    return new Builder(new ClassPath(aClass));
  }

  public void assignType(ScriptDump dump) {
    types.forEach(type -> dump.assignType(path, type));
  }

  public static class Builder {
    private final ClassPath path;
    private final ImmutableList.Builder<BaseType> builder = ImmutableList.builder();

    private Builder(ClassPath path) {
      this.path = path;
    }

    public void dispatch(String name, UnaryOperator<JSObjectType.Builder> operator) {
      ResourceLocation id = Util.id(name);
      BaseType type = operator.apply(Types.object().member("type", Types.literal(id.toString()))).build();
      builder.add(type);
    }

    public void direct(BaseType type) {
      builder.add(type);
    }

    public TypeAssigner build() {
      return new TypeAssigner(path, builder.build());
    }
  }
}
