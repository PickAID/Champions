package top.theillusivec4.champions.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.callback.AddCallback;
import net.neoforged.neoforge.registries.callback.ClearCallback;
import top.theillusivec4.champions.attachments.AttachmentSyncHandler;

import java.util.HashMap;
import java.util.Map;

public final class ChampionsRegistryCallbacks {
  private ChampionsRegistryCallbacks() {
  }

  public static class Attachments implements AddCallback<AttachmentType<?>>, ClearCallback<AttachmentType<?>> {
    public static final Attachments INSTANCE = new Attachments();
    public static final Map<AttachmentType<?>, AttachmentSyncHandler<?>> SYNC_HANDLER_MAP = new HashMap<>();

    @Override
    public void onAdd(Registry<AttachmentType<?>> registry, int id, ResourceKey<AttachmentType<?>> key, AttachmentType<?> value) {
      ResourceLocation name = key.location();
      AttachmentSyncHandler<?> handler = ChampionsRegistries.ATTACHMENT_SYNC_HANDLER.get(name);
      if (handler != null) {
        SYNC_HANDLER_MAP.put(value, handler);
      }
    }

    @Override
    public void onClear(Registry<AttachmentType<?>> registry, boolean full) {
      SYNC_HANDLER_MAP.clear();
    }
  }
}
