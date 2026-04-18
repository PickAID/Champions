package top.theillusivec4.champions.integrations.jade.component;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.JadeIds;
import snownee.jade.api.config.IPluginConfig;
import top.theillusivec4.champions.integrations.jade.element.StarElement;
import top.theillusivec4.champions.api.championmob.ChampionMobPropertyHelper;

public class EntityChampionPropertyComponent implements IEntityComponentProvider {
	public static EntityChampionPropertyComponent create() {
		return new EntityChampionPropertyComponent();
	}

	@Override
	public void appendTooltip(ITooltip tip, EntityAccessor accessor, IPluginConfig config) {
		Entity entity = accessor.getEntity();
		int tier = ChampionMobPropertyHelper.getTier(entity);
		if (tier > 0) {
//			List<LayoutElement> list = new ArrayList<>();
//			for (int i = 0; i < tier; i++) {
//				list.add(new StarElement(ChampionPropertyHelper.getColor(entity)));
//			}
			tip.add(new StarElement(ChampionMobPropertyHelper.getColor(entity), ChampionMobPropertyHelper.getTier(entity)));
			tip.replace(JadeIds.CORE_OBJECT_NAME, ChampionMobPropertyHelper.getDisplayName(entity));
		}
	}

	@Override
	public Identifier getUid() {
		return ChampionsComponents.ENTITY_CHAMPION_PROPERTY;
	}
}
