package top.theillusivec4.champions.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerBossEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.EntityAffixes;
import top.theillusivec4.champions.champion.affix.LatestDamage;
import top.theillusivec4.champions.champion.affix.effect.AffixLocationBasedEffect;
import top.theillusivec4.champions.champion.rank.Rank;

import java.util.Map;
import java.util.Optional;

public final class Attachments {
  private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Champions.MODID);
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<EntityAffixes>> ENTITY_AFFIXES = register("entity_affixes", AttachmentType.builder(() -> EntityAffixes.EMPTY).serialize(EntityAffixes.MAP_CODEC).sync(EntityAffixes.STREAM_CODEC));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<LatestDamage>> LATEST_DAMAGE = register("latest_damage", AttachmentType.builder(() -> LatestDamage.EMPTY).serialize(LatestDamage.MAP_CODEC));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Component>> PREFIX_NAME = register("prefix_name", AttachmentType.<Component>builder(Component::empty).serialize(ComponentSerialization.CODEC.fieldOf("prefix_name")).sync(ComponentSerialization.STREAM_CODEC));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> LEVEL = register("level", AttachmentType.builder(() -> 1).serialize(Codec.intRange(1, 99).fieldOf("level")).sync(ByteBufCodecs.INT));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> COLOR = register("color", AttachmentType.builder(() -> -1).serialize(Codec.INT.fieldOf("color")).sync(ByteBufCodecs.INT));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Optional<Holder<Rank>>>> RANK = register("rank", AttachmentType.<Optional<Holder<Rank>>>builder(Optional::empty).serialize(Rank.REFERENCE_CODEC.optionalFieldOf("rank")).sync(Rank.STREAM_CODEC.apply(ByteBufCodecs::optional)));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Map<Affix, AffixLocationBasedEffect>>> ACTION_EFFECTS = register("action_effects", AttachmentType.builder(Map::of));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<ServerBossEvent>> BOSS_EVENT = register("boss_event", AttachmentType.builder(() -> Instances.SERVER_BOSS_EVENT).serialize(MapCodecs.SERVER_BOSS_EVENT));

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> register(String name, AttachmentType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  private Attachments() {
  }

}
