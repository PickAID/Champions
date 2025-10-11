package top.theillusivec4.champions.api;


import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.BasicAffix;
import top.theillusivec4.champions.common.util.Utils;

import java.util.function.Supplier;

public class AffixRegistry {
	public static final ResourceLocation AFFIX_REGISTRY_KEY = Utils.getLocation("affix");
	public static final ResourceLocation EMPTY = Utils.getLocation("empty");

	public static final DeferredRegister<IAffix> AFFIXES = DeferredRegister.create(IAffix.class, Champions.MODID);
	private static final Supplier<IForgeRegistry<IAffix>> AFFIX_REGISTRY = AFFIXES.makeRegistry(AFFIX_REGISTRY_KEY.getPath(), () ->
			new RegistryBuilder<IAffix>()
					.setName(AFFIX_REGISTRY_KEY)
					.setMaxID(2048)
					.setDefaultKey(EMPTY)
					.onAdd((owner, manager, id, affix, oldAffix) -> {
								if (affix instanceof BasicAffix &&
										affix.getSetting().equals(AffixSetting.empty())
								) {
									BasicAffix basic = (BasicAffix) affix;
									// add default affix setting if empty
									basic.applyDefaultSettingWithId();
									basic.tryRegisterEvents();
									// add affix to category map
									Champions.API.addCategory(basic.getCategory(), basic);
								}
								Champions.LOGGER.info("Affix added to registry: {}", affix);
							}
					));

	public static IForgeRegistry<IAffix> getRegistry() {
		return AFFIX_REGISTRY.get();
	}
}
