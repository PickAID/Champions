package top.theillusivec4.champions.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.affix.AffixContainer;
import top.theillusivec4.champions.championmob.property.ChampionProperty;
import top.theillusivec4.champions.extralootparam.DamageTracker;
import top.theillusivec4.champions.server.champion.ChampionsServerBossEvent;

import java.util.function.Supplier;

public final class ChampionsAttachments {
  private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ChampionsMod.MOD_ID);
  public static final Supplier<AttachmentType<AffixContainer>> AFFIXES = register("affixes", AttachmentType.builder(() -> AffixContainer.EMPTY).serialize(AffixContainer.MAP_CODEC.codec()).sync(AffixContainer.STREAM_CODEC));
  public static final Supplier<AttachmentType<ChampionsServerBossEvent>> BOSS_EVENT = register("boss_event", AttachmentType.builder(() -> ChampionsServerBossEvent.EMPTY).serialize(ChampionsServerBossEvent.MAP_CODEC.codec()));
  public static final Supplier<AttachmentType<ChampionProperty>> CHAMPION_MOB_PROPERTY = register("champion_mob_property", AttachmentType.builder(() -> ChampionProperty.EMPTY).serialize(ChampionProperty.MAP_CODEC.codec()).sync(ChampionProperty.STREAM_CODEC));
 public static final Supplier<AttachmentType<Boolean>> CHAMPION_MOB_EGG_SPAWNED = register("champion_mob_egg_spawned", AttachmentType.builder(() -> false).serialize(Codec.BOOL));
  public static final Supplier<AttachmentType<Integer>> DAMAGE_COUNT = register("damage_count", AttachmentType.builder(() -> 0).serialize(Codec.INT));
  public static final Supplier<AttachmentType<ResourceKey<DamageType>>> DAMAGE_TYPE = register("damage_type", AttachmentType.builder(() -> DamageTypes.GENERIC_KILL).serialize(ResourceKey.codec(Registries.DAMAGE_TYPE)));
  public static final Supplier<AttachmentType<DamageTracker>> DAMAGE_TRACKER = register("damage_tracker", AttachmentType.builder(() -> DamageTracker.EMPTY).serialize(DamageTracker.MAP_CODEC.codec()));

  private ChampionsAttachments() {
  }

  private static <T> Supplier<AttachmentType<T>> register(String name, AttachmentType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
