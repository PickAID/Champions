package top.theillusivec4.champions.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.client.util.HUDHelper;
import top.theillusivec4.champions.client.util.MouseHelper;
import top.theillusivec4.champions.common.config.ChampionsConfig;

import java.util.Objects;
import java.util.Optional;

public class ChampionsOverlay implements IGuiOverlay {

    public static boolean isRendering = false;
    public static int startX = 0;
    public static int startY = 0;

    public static boolean isBlackListEntity(LivingEntity entity) {
        return ChampionsConfig.bossBarBlackList.contains(Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType())).toString());
    }

    @Override
    public void render(ForgeGui gui, PoseStack guiGraphics, float partialTick, int width,
                       int height) {

        if (ChampionsConfig.showHud) {
            Minecraft mc = Minecraft.getInstance();
            Optional<LivingEntity> livingEntity =
                    MouseHelper.getMouseOverChampion(mc, partialTick);
            livingEntity.ifPresent(entity -> isRendering = !isBlackListEntity(entity) && HUDHelper.renderHealthBar(guiGraphics, entity));

            if (livingEntity.isEmpty()) {
                isRendering = false;
            }
        }
    }
}
