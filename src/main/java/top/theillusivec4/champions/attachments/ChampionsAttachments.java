package top.theillusivec4.champions.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.affix.AffixContainer;
import top.theillusivec4.champions.server.boss.ChampionsServerBossEvent;
import top.theillusivec4.champions.util.ChampionsEmpties;
import top.theillusivec4.champions.util.ChampionsStreamCodecs;

import java.util.function.Supplier;

public final class ChampionsAttachments {
  private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ChampionsMod.MOD_ID);
  public static final Supplier<AttachmentType<AffixContainer>> AFFIXES = register("affix_container", AttachmentType.builder(() -> AffixContainer.EMPTY).serialize(AffixContainer.MAP_CODEC.codec()).sync(AffixContainer.STREAM_CODEC));
  public static final Supplier<AttachmentType<ChampionsServerBossEvent>> BOSS_EVENT = register("boss_event", AttachmentType.builder(() -> ChampionsServerBossEvent.EMPTY).serialize(ChampionsServerBossEvent.MAP_CODEC.codec()));
  public static final Supplier<AttachmentType<Integer>> CHAMPION_LEVEL = register("champion_level", AttachmentType.builder(() -> 1).serialize(Codec.INT).sync(ByteBufCodecs.INT));
  public static final Supplier<AttachmentType<TextColor>> NAME_COLOR = register("name_color", AttachmentType.builder(() -> ChampionsEmpties.TEXT_COLOR).serialize(TextColor.CODEC).sync(ChampionsStreamCodecs.TEXT_COLOR));
  public static final Supplier<AttachmentType<Component>> NAME_PREFIX = register("name_prefix", AttachmentType.builder(() -> CommonComponents.EMPTY).serialize(ComponentSerialization.CODEC).sync(ComponentSerialization.STREAM_CODEC));

  private ChampionsAttachments() {
  }

  private static <T> Supplier<AttachmentType<T>> register(String name, AttachmentType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
