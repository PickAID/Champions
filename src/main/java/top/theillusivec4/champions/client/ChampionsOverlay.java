package top.theillusivec4.champions.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.client.util.HUDHelper;
import top.theillusivec4.champions.client.util.MouseHelper;
import top.theillusivec4.champions.common.config.ChampionsConfig;

import java.util.Objects;
import java.util.Optional;

public class ChampionsOverlay {

	public static boolean isRendering = false;
	public static int startX = 0;
	public static int startY = 0;

	public static boolean isBlackListEntity(LivingEntity entity) {
		return ChampionsConfig.bossBarBlackList.contains(Objects.requireNonNull(ForgeRegistries.ENTITIES.getKey(entity.getType())).toString());
	}

	@SubscribeEvent
	public void renderChampionHealth(RenderGameOverlayEvent.Pre evt) {

		if (evt.getType() == RenderGameOverlayEvent.ElementType.BOSSHEALTH && ChampionsConfig.showHud) {
			Minecraft mc = Minecraft.getInstance();
			Optional<LivingEntity> livingEntity =
					MouseHelper.getMouseOverChampion(mc, evt.getPartialTicks());
			livingEntity.ifPresent(entity -> {
				MatrixStack matrixStack = evt.getMatrixStack();
				if (!isBlackListEntity(entity) && HUDHelper.renderHealthBar(matrixStack, entity)) {
					isRendering = true;
					evt.setCanceled(true);
					MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(matrixStack,
							new RenderGameOverlayEvent(matrixStack, evt.getPartialTicks(), mc.getWindow()),
							RenderGameOverlayEvent.ElementType.BOSSHEALTH));
				}

			});
		}
	}
}
