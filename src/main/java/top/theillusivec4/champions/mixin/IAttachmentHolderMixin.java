package top.theillusivec4.champions.mixin;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;
import top.theillusivec4.champions.util.IIAttachmentHolderExtension;

import java.util.Map;

@Mixin(IAttachmentHolder.class)
public interface IAttachmentHolderMixin extends IIAttachmentHolderExtension {
  @Override
  default Map<AttachmentType<?>, Object> champions$getAttachmentMap() {
    return Map.of();
  }
}
