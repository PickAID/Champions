package top.theillusivec4.champions.util;

import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.Map;

public interface IIAttachmentHolderExtension {
  Map<AttachmentType<?>, Object> champions$getAttachmentMap();
}
