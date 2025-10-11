package top.theillusivec4.champions.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.client.ChampionsOverlay;
import top.theillusivec4.champions.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.common.util.Utils;

import java.util.Set;
import java.util.stream.Collectors;

public class HUDHelper {

    private static final ResourceLocation GUI_BAR_TEXTURES = new ResourceLocation("textures/gui/bars.png");
    private static final ResourceLocation GUI_STAR = Utils.getLocation("textures/gui/staricon.png");

    public static boolean renderHealthBar(MatrixStack poseStack, final LivingEntity livingEntity) {
        return ChampionCapability.getCapability(livingEntity).map(champion -> {
            IChampion.Client clientChampion = champion.getClient();
            return ChampionHelper.isValidChampion(clientChampion) && clientChampion.getRank().map(rank -> {
                int championLevel = rank.getA();
                Set<String> affixSet = clientChampion.getAffixes().stream().map(IAffix::toLanguageKey)
                        .collect(Collectors.toSet());

                if (championLevel >= 1 || !affixSet.isEmpty()) {
                    Minecraft client = Minecraft.getInstance();
                    // calculate render position
                    int i = client.getWindow().getGuiScaledWidth();
                    int k = i / 2 - 91;
                    int j = 21;
                    int xOffset = ClientChampionsConfig.hudXOffset;
                    int yOffset = ClientChampionsConfig.hudYOffset;
                    String colorCode = rank.getB();
                    int color = Rank.getColor(colorCode);

                    float r = ColorHelper.PackedColor.red(color) / 255.0F;
                    float g = ColorHelper.PackedColor.green(color) / 255.0F;
                    float b = ColorHelper.PackedColor.blue(color) / 255.0F;

                    RenderSystem.defaultBlendFunc();
                    // set shader color for render element
                    RenderSystem.color4f(r, g, b, 1.0F);
                    RenderSystem.enableBlend();
	                client.getTextureManager().bind(GUI_BAR_TEXTURES);
                    ChampionsOverlay.startX = xOffset + k;
                    ChampionsOverlay.startY = yOffset + 1;

	                AbstractGui.blit(poseStack, xOffset + k, yOffset + j, 0, 60, 182, 5, 256, 256);
                    int healthOffset =
                            (int) ((livingEntity.getHealth() / livingEntity.getMaxHealth()) * 183.0F);

                    if (healthOffset > 0) {
	                    AbstractGui.blit(poseStack, xOffset + k, yOffset + j, 0, 65, healthOffset, 5, 256,
                                256);
                    }

	                client.getTextureManager().bind(GUI_STAR);

                    if (championLevel <= 18) {
                        int startStarsX = xOffset + i / 2 - 5 - 5 * (championLevel - 1);

                        for (int tier = 0; tier < championLevel; tier++) {
	                        AbstractGui.blit(poseStack, startStarsX, yOffset + 1, 0, 0, 9, 9, 9, 9);
                            startStarsX += 10;
                        }
                    } else {
                        int startStarsX = xOffset + i / 2 - 5;
                        String count = "x" + championLevel;
	                    AbstractGui.blit(poseStack, startStarsX - client.font.width(count) / 2,
                                yOffset + 1, 0, 0, 9, 9, 9, 9);
	                    client.font.drawShadow(poseStack, count,
                                startStarsX + 10 - client.font.width(count) / 2.0F, yOffset + 2,
                                16777215, true);
                    }
                    ITextComponent customName = livingEntity.getCustomName();
                    String name;

                    if (customName == null) {
                        name = Utils.translatable("rank.champions.title." + championLevel).getString();
                        name += " " + livingEntity.getName().getString();
                    } else {
                        name = customName.getString();
                    }
	                client.font.drawShadow(poseStack, name,
                            xOffset + (float) (i / 2 - client.font.width(name) / 2),
                            yOffset + (float) (j - 9), color, true);
                    // reset shader color
                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                    StringBuilder builder = new StringBuilder();

                    for (String affix : affixSet) {
                        builder.append(
                                Utils.translatable(affix).getString());
                        builder.append(" ");
                    }
                    String affixes = builder.toString().trim();
	                client.font.drawShadow(poseStack, affixes,
                            xOffset + (float) (i / 2 - client.font.width(affixes) / 2),
                            yOffset + (float) (j + 6), 16777215, true);
                    RenderSystem.disableBlend();
                    return true;
                }
                return false;
            }).orElse(false);
        }).orElse(false);
    }

    public static ResourceLocation getGuiStar() {
        return GUI_STAR;
    }
}
