package top.theillusivec4.champions.common.registry;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.loot.ChampionLootModifier;

public class ModLootModifiers {
	public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS =
			DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, Champions.MODID);
	public static final RegistryObject<ChampionLootModifier.Serializer> CHAMPION_LOOT = LOOT_MODIFIER_SERIALIZERS.register("champion_loot", ChampionLootModifier.Serializer::new);

	public static void register(IEventBus bus) {
		LOOT_MODIFIER_SERIALIZERS.register(bus);
	}
}
