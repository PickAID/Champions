package top.theillusivec4.champions.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;

import java.util.Optional;
import java.util.function.Supplier;

public final class Attachments {
  private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Champions.MODID);
  // Champion Entity
  public static final Supplier<AttachmentType<Optional<Holder<DamageType>>>> DAMAGE_TYPE = register("damage_type", AttachmentType.<Optional<Holder<DamageType>>>builder(Optional::empty).serialize(DamageType.CODEC.optionalFieldOf("damage_type")));
  public static final Supplier<AttachmentType<Optional<Integer>>> DAMAGE_COUNT = register("damage_count", AttachmentType.<Optional<Integer>>builder(Optional::empty).serialize(Codec.INT.optionalFieldOf("damage_count")));
  public static final Supplier<AttachmentType<Optional<Integer>>> DAMAGE_TIME = register("damage_time", AttachmentType.<Optional<Integer>>builder(Optional::empty).serialize(Codec.INT.optionalFieldOf("damage_time")));
  public static final Supplier<AttachmentType<Optional<Float>>> DAMAGE_AMOUNT = register("damage_amount", AttachmentType.<Optional<Float>>builder(Optional::empty).serialize(Codec.FLOAT.optionalFieldOf("damage_amount")));
  // Champion Common
  public static final Supplier<AttachmentType<Optional<Affixes>>> AFFIXES = register("affixes", AttachmentType.<Optional<Affixes>>builder(Optional::empty).serialize(Affixes.CODEC.optionalFieldOf("affix")).sync(Affixes.STREAM_CODEC.apply(ByteBufCodecs::optional)));
  public static final Supplier<AttachmentType<Optional<Holder<Rank>>>> RANK = register("rank", AttachmentType.<Optional<Holder<Rank>>>builder(Optional::empty).serialize(Rank.REFERENCE_CODEC.optionalFieldOf("rank")).sync(Rank.STREAM_CODEC.apply(ByteBufCodecs::optional)));
  public static final Supplier<AttachmentType<Optional<Component>>> PREFIX_NAME = register("prefix_name", AttachmentType.<Optional<Component>>builder(Optional::empty).serialize(ComponentSerialization.CODEC.optionalFieldOf("prefix_name")).sync(ComponentSerialization.STREAM_CODEC.apply(ByteBufCodecs::optional)));
  public static final Supplier<AttachmentType<Integer>> LEVEL = register("level", AttachmentType.builder(() -> 1).serialize(Codec.INT.fieldOf("level")).sync(ByteBufCodecs.INT));
  public static final Supplier<AttachmentType<Integer>> COLOR = register("color", AttachmentType.builder(() -> -1).serialize(Codec.INT.fieldOf("color")).sync(ByteBufCodecs.INT));
  public static final Supplier<AttachmentType<Optional<ServerChampionBossEvent>>> SERVER_CHAMPION_BOSS_EVENT = register("server_champion_boss_event", AttachmentType.<Optional<ServerChampionBossEvent>>builder(Optional::empty).serialize(ServerChampionBossEvent.CODEC.optionalFieldOf("champion_boss_event")));
  public static final Supplier<AttachmentType<Boolean>> BOSS = register("boss", AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("boss")).sync(ByteBufCodecs.BOOL));

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T> Supplier<AttachmentType<T>> register(String name, AttachmentType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  private Attachments() {
  }

}
