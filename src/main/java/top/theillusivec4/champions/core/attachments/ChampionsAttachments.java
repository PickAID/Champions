package top.theillusivec4.champions.core.attachments;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.EntityAffixes;
import top.theillusivec4.champions.api.affix.effect.AffixLocationBasedEffect;
import top.theillusivec4.champions.api.championmob.ChampionMobProperty;
import top.theillusivec4.champions.server.champion.ChampionsServerBossEvent;
import top.theillusivec4.champions.world.entity.damagetracker.DamageTracker;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ChampionsAttachments {
	private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Champions.MOD_ID);
	public static final Supplier<AttachmentType<EntityAffixes>> ENTITY_AFFIXES = register("entity_affixes", EntityAffixes.EMPTY, builder -> builder.serialize(EntityAffixes.MAP_CODEC).sync(EntityAffixes.STREAM_CODEC));
	public static final Supplier<AttachmentType<Map<Affix, Set<AffixLocationBasedEffect>>>> ACTIVE_LOCATION_DEPENDENT_EFFECTS = register("active_location_dependent_effects", new Reference2ObjectArrayMap<>(), builder -> builder);
	public static final Supplier<AttachmentType<ChampionsServerBossEvent>> BOSS_EVENT = register("boss_event", ChampionsServerBossEvent.EMPTY, builder -> builder.serialize(ChampionsServerBossEvent.MAP_CODEC));
	public static final Supplier<AttachmentType<ChampionMobProperty>> CHAMPION_MOB_PROPERTY = register("champion_mob_property", ChampionMobProperty.EMPTY, builder -> builder.serialize(ChampionMobProperty.MAP_CODEC).sync(ChampionMobProperty.STREAM_CODEC));
	public static final Supplier<AttachmentType<Boolean>> CHAMPION_MOB_EGG_SPAWNED = register("champion_egg_spawned", false, builder -> builder.serialize(Codec.BOOL.fieldOf("champion_egg_spawned")));
	public static final Supplier<AttachmentType<DamageTracker>> DAMAGE_TRACKER = register("damage_tracker", DamageTracker.EMPTY, builder -> builder.serialize(DamageTracker.MAP_CODEC));

	private ChampionsAttachments() {
	}

	public static void register(IEventBus modEventBus) {
		DEFERRED_REGISTER.register(modEventBus);
	}

	private static <T> Supplier<AttachmentType<T>> register(String name, T empty, UnaryOperator<AttachmentType.Builder<T>> operator) {
		return DEFERRED_REGISTER.register(name, () -> operator.apply(AttachmentType.builder(() -> empty)).build());
	}

}
