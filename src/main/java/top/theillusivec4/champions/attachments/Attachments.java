package top.theillusivec4.champions.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.affix.Affix;
import top.theillusivec4.champions.affix.EntityAffixes;
import top.theillusivec4.champions.affix.LatestDamage;
import top.theillusivec4.champions.affix.effect.AffixLocationBasedEffect;

import java.util.Map;

public final class Attachments {
  private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Champions.MODID);
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<EntityAffixes>> ENTITY_AFFIXES = register("entity_affixes", AttachmentType.builder(() -> EntityAffixes.EMPTY).serialize(EntityAffixes.CODEC).sync(EntityAffixes.STREAM_CODEC));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<LatestDamage>> LATEST_DAMAGE = register("latest_damage", AttachmentType.builder(() -> LatestDamage.EMPTY).serialize(LatestDamage.CODEC));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> LEVEL = register("level", AttachmentType.builder(() -> 1).serialize(Codec.intRange(1, 99).fieldOf("level")).sync(ByteBufCodecs.INT));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Map<Affix, AffixLocationBasedEffect>>> ACTION_EFFECTS = register("action_effects", AttachmentType.builder(Map::of));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<ServerBossEvent>> BOSS_EVENT = register("boss_event", AttachmentType.builder(() -> Internal.SERVER_BOSS_EVENT).serialize(Internal.MapCodecs.SERVER_BOSS_EVENT));

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> register(String name, AttachmentType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  private Attachments() {
  }

  private static final class Internal {
    public static final ServerBossEvent SERVER_BOSS_EVENT = new ServerBossEvent(Component.literal(""), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.NOTCHED_10);

    private Internal() {
    }

    private static final class MapCodecs {
      public static final MapCodec<ServerBossEvent> SERVER_BOSS_EVENT = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ComponentSerialization.CODEC.fieldOf("name").forGetter(ServerBossEvent::getName),
        BossEvent.BossBarColor.CODEC.fieldOf("color").forGetter(BossEvent::getColor),
        BossEvent.BossBarOverlay.CODEC.fieldOf("overlay").forGetter(BossEvent::getOverlay),
        Codec.FLOAT.fieldOf("progress").forGetter(BossEvent::getProgress),
        Codec.BOOL.fieldOf("darken_screen").forGetter(BossEvent::shouldDarkenScreen),
        Codec.BOOL.fieldOf("play_boss_music").forGetter(BossEvent::shouldPlayBossMusic),
        Codec.BOOL.fieldOf("create_world_fog").forGetter(BossEvent::shouldCreateWorldFog)
      ).apply(instance, (component, bossBarColor, bossBarOverlay, progress, darkenScreen, playBossMusic, createWorldFog) -> {
        ServerBossEvent serverBossEvent = new ServerBossEvent(component, bossBarColor, bossBarOverlay);
        serverBossEvent.setProgress(progress);
        serverBossEvent.setDarkenScreen(darkenScreen);
        serverBossEvent.setPlayBossMusic(playBossMusic);
        serverBossEvent.setCreateWorldFog(createWorldFog);
        return serverBossEvent;
      }));

      private MapCodecs() {
      }
    }
  }
}
