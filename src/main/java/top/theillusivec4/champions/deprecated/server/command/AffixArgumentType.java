package top.theillusivec4.champions.deprecated.server.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.api.IAffix;
import top.theillusivec4.champions.deprecated.server.command.AffixArgumentType.IAffixProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AffixArgumentType implements ArgumentType<IAffixProvider> {

  private static final Collection<String> EXAMPLES = Arrays.asList("molten", "reflecting");
  private static final DynamicCommandExceptionType UNKNOWN_AFFIX = new DynamicCommandExceptionType(
    type -> Component.translatable("argument.champions.affix.unknown", type));

  public static AffixArgumentType affix() {
    return new AffixArgumentType();
  }

  public static Collection<IAffix> getAffixes(CommandContext<CommandSourceStack> context,
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
    List<IAffix> affixes = new ArrayList<>();

    while (reader.canRead()) {
      reader.skipWhitespace();  // 跳过空白字符
      String id = reader.readString();  // 读取一个字符串
      affixes.add(Champions.API.getAffix(id).orElseThrow(() -> UNKNOWN_AFFIX.create(id)));
    }
    return (source) -> affixes;
  }

  @FunctionalInterface
  public interface IAffixProvider {

    Collection<IAffix> getAffixes(CommandSourceStack source) throws CommandSyntaxException;
  }
}
