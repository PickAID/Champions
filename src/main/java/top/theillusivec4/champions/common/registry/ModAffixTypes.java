package top.theillusivec4.champions.common.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixCategory;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.BasicAffixBuilder;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.common.affix.*;

import java.util.function.Supplier;

public class ModAffixTypes {
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
    ADAPTABLE = register("adaptable", () -> new BasicAffixBuilder<>(AdaptableAffix::new).setCategory(AffixCategory.DEFENSE).enabled(true).build());
    ARCTIC = register("arctic", () -> new BasicAffixBuilder<>(ArcticAffix::new).setCategory(AffixCategory.CC).enabled(true).build());
    DAMPENING = register("dampening", () -> new BasicAffixBuilder<>(DampeningAffix::new).setCategory(AffixCategory.DEFENSE).enabled(true).build());
    DESECRATING = register("desecrating", () -> new BasicAffixBuilder<>(DesecratingAffix::new).setCategory(AffixCategory.OFFENSE).enabled(true).build());
    ENKINDLING = register("enkindling", () -> new BasicAffixBuilder<>(EnkindlingAffix::new).setCategory(AffixCategory.OFFENSE).enabled(true).build());
    HASTY = register("hasty", () -> new BasicAffixBuilder<>(HastyAffix::new).setCategory(AffixCategory.OFFENSE).enabled(true).build());
    INFESTED = register("infested", () -> new BasicAffixBuilder<>(InfestedAffix::new).setCategory(AffixCategory.OFFENSE).enabled(true).build());
    KNOCKING = register("knocking", () -> new BasicAffixBuilder<>(KnockingAffix::new).setCategory(AffixCategory.CC).enabled(true).build());
    LIVELY = register("lively", () -> new BasicAffixBuilder<>(LivelyAffix::new).setCategory(AffixCategory.DEFENSE).enabled(true).build());
    MAGNETIC = register("magnetic", () -> new BasicAffixBuilder<>(MagneticAffix::new).setCategory(AffixCategory.CC).enabled(true).build());
    MOLTEN = register("molten", () -> new BasicAffixBuilder<>(MoltenAffix::new).setCategory(AffixCategory.OFFENSE).enabled(true).build());
    PARALYZING = register("paralyzing", () -> new BasicAffixBuilder<>(ParalyzingAffix::new).setCategory(AffixCategory.CC).enabled(true).build());
    PLAGUED = register("plagued", () -> new BasicAffixBuilder<>(PlaguedAffix::new).setCategory(AffixCategory.OFFENSE).enabled(true).build());
    REFLECTIVE = register("reflective", () -> new BasicAffixBuilder<>(ReflectiveAffix::new).setHasSubscriptions().setCategory(AffixCategory.OFFENSE).enabled(true).build());
    SHIELDING = register("shielding", () -> new BasicAffixBuilder<>(ShieldingAffix::new).setCategory(AffixCategory.DEFENSE).enabled(true).build());
    WOUNDING = register("wounding", () -> new BasicAffixBuilder<>(WoundingAffix::new).setCategory(AffixCategory.OFFENSE).setHasSubscriptions().enabled(true).build());
  }

  private static <I extends IAffix> DeferredHolder<IAffix, I> register(String name, Supplier<I> supplier) {
    return AFFIXES.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    AFFIXES.register(bus);
  }
}
