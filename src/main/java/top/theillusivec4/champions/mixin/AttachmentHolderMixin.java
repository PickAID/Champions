package top.theillusivec4.champions.mixin;

import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.theillusivec4.champions.util.IIAttachmentHolderExtension;

import java.util.Map;

@Mixin(AttachmentHolder.class)
public abstract class AttachmentHolderMixin implements IIAttachmentHolderExtension {
  @Shadow
  @Nullable Map<AttachmentType<?>, Object> attachments;

  @Override
  public Map<AttachmentType<?>, Object> champions$getAttachmentMap() {
    return this.attachments;
  }
}
