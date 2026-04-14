package top.theillusivec4.champions.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import top.theillusivec4.champions.server.commands.AffixCommands;
import top.theillusivec4.champions.server.commands.BossCommands;

public final class ChampionsCommands {
	private ChampionsCommands() {
	}

	public static void register(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext buildContext) {
		AffixCommands.register(builder, buildContext);
		BossCommands.register(builder, buildContext);
	}
}
