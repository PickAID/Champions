package top.theillusivec4.champions.core.attachment;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.server.champion.ChampionsServerBossEvent;
import top.theillusivec4.champions.world.entity.affix.EntityAffixes;
import top.theillusivec4.champions.world.entity.champion.property.ChampionProperty;
import top.theillusivec4.champions.world.entity.damagetracker.DamageTracker;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ChampionsAttachments {
  private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ChampionsMod.MOD_ID);
  public static final Supplier<AttachmentType<EntityAffixes>> ENTITY_AFFIXES = register("entity_affixes", EntityAffixes.EMPTY, builder -> builder.serialize(EntityAffixes.MAP_CODEC.codec()).sync(EntityAffixes.STREAM_CODEC));
  public static final Supplier<AttachmentType<ChampionsServerBossEvent>> BOSS_EVENT = register("boss_event", ChampionsServerBossEvent.EMPTY, builder -> builder.serialize(ChampionsServerBossEvent.MAP_CODEC.codec()));
  public static final Supplier<AttachmentType<ChampionProperty>> CHAMPION_MOB_PROPERTY = register("champion_mob_property", ChampionProperty.EMPTY, builder -> builder.serialize(ChampionProperty.MAP_CODEC.codec()).sync(ChampionProperty.STREAM_CODEC));
  public static final Supplier<AttachmentType<Boolean>> CHAMPION_EGG_SPAWNED = register("champion_egg_spawned", false, builder -> builder.serialize(Codec.BOOL));
  public static final Supplier<AttachmentType<DamageTracker>> DAMAGE_TRACKER = register("damage_tracker", DamageTracker.EMPTY, builder -> builder.serialize(DamageTracker.MAP_CODEC.codec()));

  private ChampionsAttachments() {
  }

  private static <T> Supplier<AttachmentType<T>> register(String name, T empty, UnaryOperator<AttachmentType.Builder<T>> operator) {
    return DEFERRED_REGISTER.register(name, () -> operator.apply(AttachmentType.builder(() -> empty)).build());
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
