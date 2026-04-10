package top.theillusivec4.champions.attachments;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.affix.AffixContainer;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.function.Supplier;

public final class AttachmentSyncHandlers {
  private static final DeferredRegister<AttachmentSyncHandler<?>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.ATTACHMENT_SYNC_HANDLER, ChampionsMod.MOD_ID);
  public static final Supplier<AttachmentSyncHandler<AffixContainer>> AFFIXES = register("affix", AffixContainer.STREAM_CODEC);

  private AttachmentSyncHandlers() {
  }

  private static <T> Supplier<AttachmentSyncHandler<T>> register(String name, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
    return DEFERRED_REGISTER.register(name, () -> new AttachmentSyncHandler<>(streamCodec));
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
