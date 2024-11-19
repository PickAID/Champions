package top.theillusivec4.champions.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.affix.core.BasicAffix;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.integration.kubejs.ChampionHooks;
import top.theillusivec4.champions.common.integration.kubejs.KubeJSHooks;
import top.theillusivec4.champions.common.registry.ModAffixTypes;

import java.util.List;

@EventBusSubscriber(modid = Champions.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AffixRegistry {
  public static final ResourceKey<Registry<IAffix>> AFFIX_REGISTRY_KEY = createKey("affix_registry_key");

  public static final Registry<IAffix> AFFIX_REGISTRY = new RegistryBuilder<>(AFFIX_REGISTRY_KEY)
    .sync(true)
    .maxId(2048)
    .defaultKey(Keys.ADAPTABLE)
    .onAdd((registry, id, resourceKey, affix) -> {
      Champions.LOGGER.info("Affix added to registry: {}", resourceKey);
      ChampionHooks.onCustomAffixBuild(affix);
      if (ModList.get().isLoaded("kubejs")) KubeJSHooks.onKubeJSAffixBuild(affix);
    })
    .create();

  public static final Codec<IAffix> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Codec.BOOL.fieldOf("enabled").forGetter(IAffix::isEnabled),
    MinMaxBounds.Ints.CODEC.fieldOf("tier").forGetter(IAffix::getTier),
    Codec.list(ResourceLocation.CODEC).optionalFieldOf("mobList").forGetter(IAffix::getMobList),
    StringRepresentable.fromEnum(ConfigEnums.Permission::values).fieldOf("mobPermission").forGetter(IAffix::getMobPermission),
    StringRepresentable.fromEnum(AffixCategory::values).fieldOf("category").forGetter(IAffix::getCategory),
    Codec.STRING.fieldOf("prefix").forGetter(IAffix::getPrefix),
    Codec.BOOL.fieldOf("hasSubscriptions").forGetter(IAffix::hasSubscriptions)
  ).apply(instance, (enabled, tier, mobList, permission, category, prefix, hasSubscriptions) -> {
    // 使用 BasicAffixBuilder 构建对象
    var builder = new BasicAffixBuilder<>(BasicAffix::new);
    builder.enabled(enabled)
      .setTier(tier)
      .setMobList(mobList.orElse(List.of()))
      .setMobPermission(permission)
      .setCategory(category)
      .setPrefix(prefix);
    if (hasSubscriptions) {
      builder.setHasSubscriptions();
    }
    return builder.build();
  }));

  @SubscribeEvent
  public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
    event.dataPackRegistry(
      Keys.AFFIX_TYPE,
      CODEC,
      CODEC
    );
  }

  public static ResourceKey<IAffix> create(String path) {
    return ResourceKey.create(Keys.AFFIX_TYPE, Champions.getLocation(path));
  }

  private static ResourceKey<Registry<IAffix>> createKey(String path) {
    return ResourceKey.createRegistryKey(Champions.getLocation(path));
  }

  public static class Keys {
    public static final ResourceKey<Registry<IAffix>> AFFIX_TYPE = createKey("affix_type");
    public static final ResourceKey<IAffix> ADAPTABLE = create("adaptable");
    public static final ResourceKey<IAffix> ARCTIC = create("arctic");
    public static final ResourceKey<IAffix> DAMPENING = create("dampening");
    public static final ResourceKey<IAffix> ENKINDLING = create("enkindling");
    public static final ResourceKey<IAffix> DESECRATING = create("desecrating");
    public static final ResourceKey<IAffix> HASTY = create("hasty");
    public static final ResourceKey<IAffix> INFESTED = create("infested");
    public static final ResourceKey<IAffix> KNOCKING = create("knocking");
    public static final ResourceKey<IAffix> LIVELY = create("lively");
    public static final ResourceKey<IAffix> MAGNETIC = create("magnetic");
    public static final ResourceKey<IAffix> MOLTEN = create("molten");
    public static final ResourceKey<IAffix> PARALYZING = create("paralyzing");
    public static final ResourceKey<IAffix> PLAGUED = create("plagued");
    public static final ResourceKey<IAffix> REFLECTIVE = create("reflective");
    public static final ResourceKey<IAffix> SHIELDING = create("shielding");
    public static final ResourceKey<IAffix> WOUNDING = create("wounding");

    public static void bootstrap(BootstrapContext<IAffix> context) {
      context.register(ADAPTABLE, ModAffixTypes.ADAPTABLE.get());
      context.register(ARCTIC, ModAffixTypes.ARCTIC.get());
      context.register(DAMPENING, ModAffixTypes.DAMPENING.get());
      context.register(DESECRATING, ModAffixTypes.DESECRATING.get());
      context.register(ENKINDLING, ModAffixTypes.ENKINDLING.get());
      context.register(HASTY, ModAffixTypes.HASTY.get());
      context.register(INFESTED, ModAffixTypes.INFESTED.get());
      context.register(KNOCKING, ModAffixTypes.KNOCKING.get());
      context.register(LIVELY, ModAffixTypes.LIVELY.get());
      context.register(MAGNETIC, ModAffixTypes.MAGNETIC.get());
      context.register(MOLTEN, ModAffixTypes.MOLTEN.get());
      context.register(PARALYZING, ModAffixTypes.PARALYZING.get());
      context.register(PLAGUED, ModAffixTypes.PLAGUED.get());
      context.register(REFLECTIVE, ModAffixTypes.REFLECTIVE.get());
      context.register(SHIELDING, ModAffixTypes.SHIELDING.get());
      context.register(WOUNDING, ModAffixTypes.WOUNDING.get());
    }
  }

}
