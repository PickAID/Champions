package top.theillusivec4.champions.mixin;

import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.UUID;

@Mixin(value = BossHealthOverlay.class)
public interface BossHealthOverlayAccessor {
  @Accessor(value = "events")
  Map<UUID, LerpingBossEvent> getEvents();
}
