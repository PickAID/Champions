package top.theillusivec4.champions.common.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.EntityAffixes;
import top.theillusivec4.champions.api.affix.LatestDamage;

public final class AttachmentTypes {
  private static final DeferredRegister<AttachmentType<?>> DEFERRED_REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Champions.MODID);
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<EntityAffixes>> ENTITY_AFFIXES = register("entity_affixes", AttachmentType.builder(() -> EntityAffixes.EMPTY).serialize(EntityAffixes.CODEC).sync(EntityAffixes.STREAM_CODEC));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<LatestDamage>> LATEST_DAMAGE = register("latest_damage", AttachmentType.builder(() -> LatestDamage.EMPTY).serialize(LatestDamage.CODEC));
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> LEVEL = register("level", AttachmentType.builder(() -> 1).serialize(Codec.intRange(1, 99).fieldOf("level")).sync(ByteBufCodecs.INT));

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> register(String name, AttachmentType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  private AttachmentTypes() {
  }
}
