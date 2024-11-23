package top.theillusivec4.champions.common.registry;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixCategory;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.BasicAffixBuilder;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.common.affix.*;
import top.theillusivec4.champions.common.config.ConfigEnums;

import java.util.List;
import java.util.function.Supplier;

public class AffixTypes {
  public static final DeferredHolder<IAffix, AdaptableAffix> ADAPTABLE;
  public static final DeferredHolder<IAffix, ArcticAffix> ARCTIC;
  public static final DeferredHolder<IAffix, DampeningAffix> DAMPENING;
  public static final DeferredHolder<IAffix, DesecratingAffix> DESECRATING;
  public static final DeferredHolder<IAffix, EnkindlingAffix> ENKINDLING;
  public static final DeferredHolder<IAffix, HastyAffix> HASTY;
  public static final DeferredHolder<IAffix, InfestedAffix> INFESTED;
  public static final DeferredHolder<IAffix, KnockingAffix> KNOCKING;
  public static final DeferredHolder<IAffix, LivelyAffix> LIVELY;
  public static final DeferredHolder<IAffix, MagneticAffix> MAGNETIC;
  public static final DeferredHolder<IAffix, MoltenAffix> MOLTEN;
  public static final DeferredHolder<IAffix, ParalyzingAffix> PARALYZING;
  public static final DeferredHolder<IAffix, PlaguedAffix> PLAGUED;
  public static final DeferredHolder<IAffix, ReflectiveAffix> REFLECTIVE;
  public static final DeferredHolder<IAffix, ShieldingAffix> SHIELDING;
  public static final DeferredHolder<IAffix, WoundingAffix> WOUNDING;
  private static final DeferredRegister<IAffix> AFFIXES = DeferredRegister.create(AffixRegistry.AFFIX_REGISTRY, Champions.MODID);

  static {
    // add more config for data generate file example
    ADAPTABLE = register("adaptable", () -> new BasicAffixBuilder<>(AdaptableAffix::new)
      .setType(Champions.getLocation("adaptable"))
      .setCategory(AffixCategory.DEFENSE)
      .setHasSub(false)
      .setMobList(List.of(ResourceLocation.parse("minecraft:pig"), ResourceLocation.parse("minecraft:creeper")))
      .setMobPermission(ConfigEnums.Permission.BLACKLIST).setEnable(true)
      .setTier(MinMaxBounds.Ints.between(1, 100))
      .build());
    ARCTIC = register("arctic", () -> new BasicAffixBuilder<>(ArcticAffix::new).setType(Champions.getLocation("arctic")).setCategory(AffixCategory.CC).setEnable(true).build());
    DAMPENING = register("dampening", () -> new BasicAffixBuilder<>(DampeningAffix::new).setType(Champions.getLocation("dampening")).setCategory(AffixCategory.DEFENSE).setEnable(true).build());
    DESECRATING = register("desecrating", () -> new BasicAffixBuilder<>(DesecratingAffix::new).setType(Champions.getLocation("desecrating")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
    ENKINDLING = register("enkindling", () -> new BasicAffixBuilder<>(EnkindlingAffix::new).setType(Champions.getLocation("enkindling")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
    HASTY = register("hasty", () -> new BasicAffixBuilder<>(HastyAffix::new).setType(Champions.getLocation("hasty")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
    INFESTED = register("infested", () -> new BasicAffixBuilder<>(InfestedAffix::new).setType(Champions.getLocation("infested")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
    KNOCKING = register("knocking", () -> new BasicAffixBuilder<>(KnockingAffix::new).setType(Champions.getLocation("knocking")).setCategory(AffixCategory.CC).setEnable(true).build());
    LIVELY = register("lively", () -> new BasicAffixBuilder<>(LivelyAffix::new).setType(Champions.getLocation("lively")).setCategory(AffixCategory.DEFENSE).setEnable(true).build());
    MAGNETIC = register("magnetic", () -> new BasicAffixBuilder<>(MagneticAffix::new).setType(Champions.getLocation("magnetic")).setCategory(AffixCategory.CC).setEnable(true).build());
    MOLTEN = register("molten", () -> new BasicAffixBuilder<>(MoltenAffix::new).setType(Champions.getLocation("molten")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
    PARALYZING = register("paralyzing", () -> new BasicAffixBuilder<>(ParalyzingAffix::new).setType(Champions.getLocation("paralyzing")).setCategory(AffixCategory.CC).setEnable(true).build());
    PLAGUED = register("plagued", () -> new BasicAffixBuilder<>(PlaguedAffix::new).setType(Champions.getLocation("plagued")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
    REFLECTIVE = register("reflective", () -> new BasicAffixBuilder<>(ReflectiveAffix::new).setType(Champions.getLocation("reflective")).setHasSub().setCategory(AffixCategory.OFFENSE).setEnable(true).build());
    SHIELDING = register("shielding", () -> new BasicAffixBuilder<>(ShieldingAffix::new).setType(Champions.getLocation("shielding")).setCategory(AffixCategory.DEFENSE).setEnable(true).build());
    WOUNDING = register("wounding", () -> new BasicAffixBuilder<>(WoundingAffix::new).setType(Champions.getLocation("wounding")).setCategory(AffixCategory.OFFENSE).setHasSub().setEnable(true).build());
  }

  private static <I extends IAffix> DeferredHolder<IAffix, I> register(String name, Supplier<I> supplier) {
    return AFFIXES.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    AFFIXES.register(bus);
  }
}
