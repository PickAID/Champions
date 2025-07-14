package top.theillusivec4.champions.common.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.affix.*;

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
    ADAPTABLE = register("adaptable", AdaptableAffix::new);
    ARCTIC = register("arctic", ArcticAffix::new);
    DAMPENING = register("dampening", DampeningAffix::new);
    DESECRATING = register("desecrating", DesecratingAffix::new);
    ENKINDLING = register("enkindling", EnkindlingAffix::new);
    HASTY = register("hasty", HastyAffix::new);
    INFESTED = register("infested", InfestedAffix::new);
    KNOCKING = register("knocking", KnockingAffix::new);
    LIVELY = register("lively", LivelyAffix::new);
    MAGNETIC = register("magnetic", MagneticAffix::new);
    MOLTEN = register("molten", MoltenAffix::new);
    PARALYZING = register("paralyzing", ParalyzingAffix::new);
    PLAGUED = register("plagued", PlaguedAffix::new);
    REFLECTIVE = register("reflective", ReflectiveAffix::new);
    SHIELDING = register("shielding", ShieldingAffix::new);
    WOUNDING = register("wounding", WoundingAffix::new);
  }

  private static <I extends IAffix> DeferredHolder<IAffix, I> register(String name, Supplier<I> supplier) {
    return AFFIXES.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    AFFIXES.register(bus);
  }
}
