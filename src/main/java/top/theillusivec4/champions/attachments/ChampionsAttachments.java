package top.theillusivec4.champions.attachments;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.affix.AffixContainer;

import java.util.function.Supplier;

public final class ChampionsAttachments {
  private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ChampionsMod.MOD_ID);
  public static final Supplier<AttachmentType<AffixContainer>> AFFIXES = register("affix_container", AttachmentType.builder(() -> AffixContainer.EMPTY).serialize(AffixContainer.MAP_CODEC.codec()).sync(AffixContainer.STREAM_CODEC));

  private ChampionsAttachments() {
  }

  private static <T> Supplier<AttachmentType<T>> register(String name, AttachmentType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
