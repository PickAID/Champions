package top.theillusivec4.champions.common.registry;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.affix.*;

import java.util.function.Supplier;

public class AffixTypes {
	public static final RegistryObject<AdaptableAffix> ADAPTABLE;
	public static final RegistryObject<ArcticAffix> ARCTIC;
	public static final RegistryObject<DampeningAffix> DAMPENING;
	public static final RegistryObject<DesecratingAffix> DESECRATING;
	public static final RegistryObject<EnkindlingAffix> ENKINDLING;
	public static final RegistryObject<HastyAffix> HASTY;
	public static final RegistryObject<InfestedAffix> INFESTED;
	public static final RegistryObject<KnockingAffix> KNOCKING;
	public static final RegistryObject<LivelyAffix> LIVELY;
	public static final RegistryObject<MagneticAffix> MAGNETIC;
	public static final RegistryObject<MoltenAffix> MOLTEN;
	public static final RegistryObject<ParalyzingAffix> PARALYZING;
	public static final RegistryObject<PlaguedAffix> PLAGUED;
	public static final RegistryObject<ReflectiveAffix> REFLECTIVE;
	public static final RegistryObject<ShieldingAffix> SHIELDING;
	public static final RegistryObject<WoundingAffix> WOUNDING;

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

	private static <I extends IAffix> RegistryObject<I> register(String name, Supplier<I> supplier) {
		return AffixRegistry.AFFIXES.register(name, supplier);
	}

	public static void register(IEventBus bus) {
		AffixRegistry.AFFIXES.register(bus);
	}
}
