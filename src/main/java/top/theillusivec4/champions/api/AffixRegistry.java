package top.theillusivec4.champions.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
	public static final ResourceKey<Registry<IAffix>> AFFIX_REGISTRY_KEY = ResourceKey.createRegistryKey(Utils.getLocation("affix"));
	public static final ResourceLocation EMPTY = Utils.getLocation("empty");

	public static final DeferredRegister<IAffix> AFFIXES = DeferredRegister.create(AFFIX_REGISTRY_KEY, Champions.MODID);
	private static final Supplier<IForgeRegistry<IAffix>> AFFIX_REGISTRY = AFFIXES.makeRegistry(IAffix.class, () ->
			new RegistryBuilder<IAffix>()
					.setName(AFFIX_REGISTRY_KEY.location())
					.setMaxID(2048)
					.setDefaultKey(EMPTY)
					.onAdd((owner, manager, id, affix, oldAffix) -> {
								if (affix instanceof BasicAffix basic &&
										basic.getSetting().equals(AffixSetting.empty())
								) {
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
