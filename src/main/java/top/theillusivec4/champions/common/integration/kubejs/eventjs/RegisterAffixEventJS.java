package top.theillusivec4.champions.common.integration.kubejs.eventjs;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.api.AffixCategory;
import top.theillusivec4.champions.api.BasicAffixBuilder;
import top.theillusivec4.champions.api.IAffixBuilder;
import top.theillusivec4.champions.common.integration.kubejs.affixjs.CustomAffix;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RegisterAffixEventJS extends EventJS {

	/**
	 * A map to store registered affix builders, keyed by their resource location.
	 */
	private final Map<ResourceLocation, IAffixBuilder<?>> builderMap = new HashMap<>();
	
	public RegisterAffixEventJS() {
	
	}

	/**
	 * Creates a new CustomAffix builder for defining custom affixes.
	 * <p>
	 * This method allows you to create a builder for a custom affix,
	 * where you can set various properties and functions before building.
	 * <p>
	 *
	 * Example
	 * event.createCustomAffixBuilder()
	 *     .setName("my_custom_affix")
	 *     .onAttack(my_custom_attack_callback)
	 *     .build();
	 */
	@Info("""
			Creates a new CustomAffix builder for defining custom affixes.
			 This method allows you to create a builder for a custom affix,\s
			 where you can set various properties and functions before building.
			 @example
			 	 event.createAffixBuilder()
			 	     .setName("my_custom_affix")
			 	     .onAttack(my_custom_attack_callback)
			 	     .build();
			""")
	public CustomAffix.Builder createCustomAffixBuilder(){
		return new CustomAffix.Builder();
	}
	/**
	 * Creates a new affix Setting Builder with the specified name and supplier.
	 * <p>
	 * This method registers a new affix type with a default configuration:
	 * - Category set to CC (Crowd Control)
	 * - Enabled by default
	 *
	 * @param name The resource location identifying the affix type
	 * @param supplier A supplier that provides the CustomAffix instance
	 * @return The created IAffixBuilder for further customization
	 * @throws IllegalArgumentException if the name is invalid or already exists
	 */
	@Info("""
			Creates a new affix Setting Builder with the specified name and supplier.
			
			  This method registers a new affix type with a default configuration:
			  - Category set to CC (Crowd Control)
			  - Enabled by default
			
			  @param name The resource location identifying the affix type
			  @param supplier A supplier that provides the CustomAffix instance
			  @return The created IAffixBuilder for further customization
			  @throws IllegalArgumentException if the name is invalid or already exists
			""")
	public IAffixBuilder<?> createAffixSettingBuilder(ResourceLocation name, Supplier<CustomAffix> supplier){
		IAffixBuilder<?> builder = new BasicAffixBuilder<>(supplier).setType(name).setCategory(AffixCategory.CC).setEnable(true);
		builderMap.put(name, builder);
		return builder;
	}

	/**
	 * Retrieves the map of all registered affix builders.
	 *
	 * @return A map containing all registered affix builders,
	 *         with resource locations as keys and IAffixBuilder instances as values
	 */
	@Info("""
			Retrieves the map of all registered affix builders.
	 
			  @return A map containing all registered affix builders,\s
			  with resource locations as keys and IAffixBuilder instances as values
			""")
	public Map<ResourceLocation, IAffixBuilder<?>> getBuilderMap() {
		return builderMap;
	}
}
