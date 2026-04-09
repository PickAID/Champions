package top.theillusivec4.champions.deprecated.common.registry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.common.capability.ChampionAttachment;
import top.theillusivec4.champions.deprecated.common.util.ChampionHelper;

public class ModAttachments {

  private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Champions.MODID);
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<ChampionAttachment.Provider>> CHAMPION_ATTACHMENT = ATTACHMENTS.register("champion_attachment", () -> AttachmentType.serializable(entity -> {
    if (ChampionHelper.isValidChampion((Entity) entity)) {
      return ChampionAttachment.createProvider((LivingEntity) entity);
    }
    return null;
  }).build());

  public static void register(IEventBus bus) {
    ATTACHMENTS.register(bus);
  }
}
