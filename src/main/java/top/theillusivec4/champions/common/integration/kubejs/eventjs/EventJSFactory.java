package top.theillusivec4.champions.common.integration.kubejs.eventjs;

import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.api.IAffixBuilder;

import java.util.Map;

public class EventJSFactory {
	
	public static Map<ResourceLocation, IAffixBuilder<?>> registerAffixTypes(){
		RegisterAffixEventJS event = new RegisterAffixEventJS();
		ChampionsEvents.REGISTER.post(ScriptType.STARTUP, event);
		return event.getBuilderMap();
	}
}
