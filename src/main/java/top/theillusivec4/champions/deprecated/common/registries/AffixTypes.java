package top.theillusivec4.champions.deprecated.common.registries;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.api.AffixRegistry;
import top.theillusivec4.champions.deprecated.api.affix.IAffix;
import top.theillusivec4.champions.deprecated.common.affix.*;

import java.util.function.Supplier;

@Deprecated
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
    ADAPTABLE = register("adaptable", AdaptableAffix::new); // 适应 √
    ARCTIC = register("arctic", ArcticAffix::new); // 极寒 √
    DAMPENING = register("dampening", DampeningAffix::new); // 抑制 √
    DESECRATING = register("desecrating", DesecratingAffix::new); // 亵渎 Goal Affix ·
    ENKINDLING = register("enkindling", EnkindlingAffix::new); // 点燃 Goal Affi ·
    HASTY = register("hasty", HastyAffix::new); // 急速 √
    INFESTED = register("infested", InfestedAffix::new); // 寄生 Goal Affix ×
    KNOCKING = register("knocking", KnockingAffix::new); // 击退 √
    LIVELY = register("lively", LivelyAffix::new); // 活性 √
    MAGNETIC = register("magnetic", MagneticAffix::new); // 磁性 Goal Affix ×
    MOLTEN = register("molten", MoltenAffix::new); // 熔融 √
    PARALYZING = register("paralyzing", ParalyzingAffix::new); // 瘫痪 √
    PLAGUED = register("plagued", PlaguedAffix::new); // 瘟疫 √
    REFLECTIVE = register("reflective", ReflectiveAffix::new); // 反射 √
    SHIELDING = register("shielding", ShieldingAffix::new); // 保护 √
    WOUNDING = register("wounding", WoundingAffix::new); // 创伤 √
  }

  private static <I extends IAffix> DeferredHolder<IAffix, I> register(String name, Supplier<I> supplier) {
    return AFFIXES.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    AFFIXES.register(bus);
  }
}
