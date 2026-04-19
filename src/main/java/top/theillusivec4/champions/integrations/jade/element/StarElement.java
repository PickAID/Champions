package top.theillusivec4.champions.integrations.jade.element;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.Nullable;
import snownee.jade.api.ui.Element;
import top.theillusivec4.champions.util.Util;

public class StarElement extends Element {
	private static final Identifier TEXTURE = Util.id("textures/gui/star.png");
	private final int color;
	private final int tier;

	public StarElement(TextColor color, int tier) {
		this.color = color.getValue();
		this.tier = tier;
		this.width = 9;
		this.height = 9;
	}

	@Override
	public @Nullable Component getNarration() {
		return null;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		for (int i = 0; i < tier; i++) {
			int xOffset = this.getX() + i * 9;
			int yOffset = this.getY();
			graphics.blit(
					RenderPipelines.GUI_TEXTURED,
					TEXTURE,
					xOffset,
					yOffset,
					0.0f,
					0.0f,
					9,
					9,
					9,
					9,
					ARGB.color(1.0f, this.color)
			);
		}
	}
}
