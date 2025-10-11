package top.theillusivec4.champions.client.integration.jade;

import com.mojang.blaze3d.systems.RenderSystem;
import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.api.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.champions.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.client.util.HUDHelper;
import top.theillusivec4.champions.common.rank.Rank;

import java.awt.*;

public class StarElement implements IComponentProvider, ITooltipRenderer {
	private int starCount;  // 记录星星的数量
	private int spacing;    // 间距
	private float r, g, b;  // 颜色

	public static CompoundNBT of(int starCount, final String colorCode, int spacing) {
		CompoundNBT data = new CompoundNBT();
		data.putInt("starCount", starCount);
		data.putString("colorCode", colorCode);
		data.putInt("spacing", spacing);
		return data;
	}

	public static ResourceLocation getTexture() {
		return HUDHelper.getGuiStar();
	}
	@Override
	public Dimension getSize(CompoundNBT compoundNBT, ICommonAccessor iCommonAccessor){
		// 宽度 = (9px * 星星数) + (间距 * (星星数 - 1))
		starCount = compoundNBT.getInt("starCount");
		spacing = compoundNBT.getInt("spacing");
		return new Dimension(starCount * 9 + (starCount - 1) * spacing, 9 + ClientChampionsConfig.jadeStarBottomPadding);
	}

	@Override
	public void draw(CompoundNBT compoundNBT, ICommonAccessor iCommonAccessor, int x, int y) {
		starCount = compoundNBT.getInt("starCount");
		spacing = compoundNBT.getInt("spacing");
		int color = Rank.getColor(compoundNBT.getString("colorCode"));
		r = ColorHelper.PackedColor.red(color) / 255.0F;
		g = ColorHelper.PackedColor.green(color) / 255.0F;
		b = ColorHelper.PackedColor.blue(color) / 255.0F;
		// set render element color
		RenderSystem.color4f(r, g, b, 1.0F);
		Minecraft.getInstance().getTextureManager().bind(getTexture());
		for (int i = 0; i < starCount; i++) {
			AbstractGui.blit(
					RenderContext.matrixStack,
					x + i * (9 + spacing), // 计算 X 偏移量
					y, // Y 坐标不变
					0, 0,
					9, 9, 9, 9
			);
		}
		// reset color
		RenderSystem.color4f(1F, 1F, 1F, 1.0F);
	}
}