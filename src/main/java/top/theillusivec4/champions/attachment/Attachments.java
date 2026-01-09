package top.theillusivec4.champions.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.affix.LatestDamage;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.client.gui.components.ClientChampionBossEvent;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;

import java.util.Optional;

public final class Attachments {
  private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Champions.MODID);
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<LatestDamage>> LATEST_DAMAGE = register("latest_damage", AttachmentType.builder(() -> LatestDamage.EMPTY).serialize(LatestDamage.MAP_CODEC));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Affixes>> AFFIXES = register("entity_affixes", AttachmentType.builder(() -> Affixes.EMPTY).serialize(Affixes.MAP_CODEC).sync(Affixes.STREAM_CODEC));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Optional<Holder<Rank>>>> RANK = register("rank", AttachmentType.<Optional<Holder<Rank>>>builder(Optional::empty).serialize(Rank.REFERENCE_CODEC.optionalFieldOf("rank")).sync(Rank.STREAM_CODEC.apply(ByteBufCodecs::optional)));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Component>> PREFIX_NAME = register("prefix_name", AttachmentType.<Component>builder(Component::empty).serialize(ComponentSerialization.CODEC.fieldOf("prefix_name")).sync(ComponentSerialization.STREAM_CODEC));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> LEVEL = register("level", AttachmentType.builder(() -> 1).serialize(Codec.intRange(1, 99).fieldOf("level")).sync(ByteBufCodecs.INT));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> COLOR = register("color", AttachmentType.builder(() -> -1).serialize(Codec.INT.fieldOf("color")).sync(ByteBufCodecs.INT));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> SPAWNED = register("spawned", AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("spawned")));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Optional<ServerChampionBossEvent>>> SERVER_CHAMPION_BOSS_EVENT = register("server_champion_boss_event", AttachmentType.<Optional<ServerChampionBossEvent>>builder(Optional::empty).serialize(ServerChampionBossEvent.CODEC.optionalFieldOf("champion_boss_event")));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> BOSS = register("boss", AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("boss")).sync(ByteBufCodecs.BOOL));
//  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Optional<ClientChampionBossEvent>>> CLIENT_CHAMPION_BOSS_EVENT = register("client_champion_boss_event", AttachmentType.<Optional<ServerChampionBossEvent>>builder(Optional::empty).serialize(ServerChampionBossEvent.CODEC.optionalFieldOf("champion_boss_event")));

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> register(String name, AttachmentType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  private Attachments() {
  }

}
