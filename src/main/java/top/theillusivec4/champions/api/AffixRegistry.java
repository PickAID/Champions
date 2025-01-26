package top.theillusivec4.champions.api;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.registry.AffixTypes;

public class AffixRegistry {
  public static final ResourceKey<Registry<IAffix>> AFFIX_REGISTRY_KEY = createKey("affix_registry_key");

  public static final Registry<IAffix> AFFIX_REGISTRY = new RegistryBuilder<>(AFFIX_REGISTRY_KEY)
    .sync(true)
    .maxId(2048)
    .defaultKey(Keys.ADAPTABLE)
    .onAdd((registry, id, resourceKey, affix) -> {
      Champions.LOGGER.info("Affix added to registry: {}", resourceKey);
    })
    .create();

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
      context.register(ADAPTABLE, AffixTypes.ADAPTABLE.get());
      context.register(ARCTIC, AffixTypes.ARCTIC.get());
      context.register(DAMPENING, AffixTypes.DAMPENING.get());
      context.register(DESECRATING, AffixTypes.DESECRATING.get());
      context.register(ENKINDLING, AffixTypes.ENKINDLING.get());
      context.register(HASTY, AffixTypes.HASTY.get());
      context.register(INFESTED, AffixTypes.INFESTED.get());
      context.register(KNOCKING, AffixTypes.KNOCKING.get());
      context.register(LIVELY, AffixTypes.LIVELY.get());
      context.register(MAGNETIC, AffixTypes.MAGNETIC.get());
      context.register(MOLTEN, AffixTypes.MOLTEN.get());
      context.register(PARALYZING, AffixTypes.PARALYZING.get());
      context.register(PLAGUED, AffixTypes.PLAGUED.get());
      context.register(REFLECTIVE, AffixTypes.REFLECTIVE.get());
      context.register(SHIELDING, AffixTypes.SHIELDING.get());
      context.register(WOUNDING, AffixTypes.WOUNDING.get());
    }
  }

}
