package top.theillusivec4.champions.attachments;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record AttachmentSyncHandler<T>(StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
  public void write(RegistryFriendlyByteBuf buf, T attachment) {
    streamCodec.encode(buf, attachment);
  }

  public T read(RegistryFriendlyByteBuf buf) {
    return streamCodec.decode(buf);
  }
}
