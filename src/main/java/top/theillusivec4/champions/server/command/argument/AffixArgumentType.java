package top.theillusivec4.champions.server.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.server.command.argument.AffixArgumentType.IAffixProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class AffixArgumentType implements ArgumentType<IAffixProvider> {

  private static final Collection<String> EXAMPLES = Arrays.asList("molten", "reflecting");
  private static final DynamicCommandExceptionType UNKNOWN_AFFIX = new DynamicCommandExceptionType(
    type -> Component.translatable("argument.champions.affix.unknown", type));

  public static AffixArgumentType affix() {
    return new AffixArgumentType();
  }

  public static Set<IAffix> getAffixes(CommandContext<CommandSourceStack> context,
                                       String name)
    throws CommandSyntaxException {
    return context.getArgument(name, IAffixProvider.class).getAffixes(context.getSource());
  }

  @Override
  public Collection<String> getExamples() {
    return EXAMPLES;
  }

  @Override
  public IAffixProvider parse(StringReader reader) throws CommandSyntaxException {
    Set<IAffix> affixes = new HashSet<>();

    while (reader.canRead()) {
      reader.skipWhitespace();  // 跳过空白字符

      // 尝试读取Identifier类型的标识符
      Identifier affixId = Identifier.read(reader);
      String id = affixId.toString();

      // 检查并添加到affixes列表中
      affixes.add(Champions.API.getAffix(id.trim())
        .orElseThrow(() -> UNKNOWN_AFFIX.create(id)));

      // 检查是否还可以继续读取
      if (reader.canRead()) {
        if (reader.peek() == ' ') {
          reader.skip(); // 如果有逗号分隔符则跳过
        }
      }
    }

    return (source) -> affixes;
  }

  @FunctionalInterface
  public interface IAffixProvider {

    Set<IAffix> getAffixes(CommandSourceStack source) throws CommandSyntaxException;
  }
}
