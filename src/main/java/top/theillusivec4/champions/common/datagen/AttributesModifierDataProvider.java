package top.theillusivec4.champions.common.datagen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.api.data.AttributesModifierDataLoader;
import top.theillusivec4.champions.api.data.ChampionModifierCondition;
import top.theillusivec4.champions.api.data.ModifierSetting;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.loot.AffixesPredicate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AttributesModifierDataProvider implements IDataProvider {

	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private final DataGenerator generator;
	private final Map<ResourceLocation, ModifierSetting> settings = new HashMap<>();

	public AttributesModifierDataProvider(DataGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void run(DirectoryCache cache) throws IOException {
		// 收集所有需要生成的数据
		ForgeRegistries.ATTRIBUTES.getEntries().forEach(entry -> {
			ResourceLocation attributeId = entry.getKey().location();
			double baseValue = 1d;
			boolean enable = false;
			AttributeModifier.Operation operation = AttributeModifier.Operation.ADDITION;

			// 应用默认设置
			if (attributeId.equals(ForgeRegistries.ATTRIBUTES.getKey(Attributes.MAX_HEALTH))) {
				baseValue = 0.35D;
				enable = true;
				operation = AttributeModifier.Operation.MULTIPLY_TOTAL;
			} else if (attributeId.equals(ForgeRegistries.ATTRIBUTES.getKey(Attributes.ATTACK_DAMAGE))) {
				baseValue = 0.5D;
				enable = true;
				operation = AttributeModifier.Operation.MULTIPLY_TOTAL;
			} else if (attributeId.equals(ForgeRegistries.ATTRIBUTES.getKey(Attributes.ARMOR))) {
				baseValue = 2.0D;
				enable = true;
			} else if (attributeId.equals(ForgeRegistries.ATTRIBUTES.getKey(Attributes.ARMOR_TOUGHNESS))) {
				baseValue = 1.0D;
				enable = true;
			} else if (attributeId.equals(ForgeRegistries.ATTRIBUTES.getKey(Attributes.KNOCKBACK_RESISTANCE))) {
				baseValue = 0.05D;
				enable = true;
			}

			ModifierSetting setting = new ModifierSetting(
					attributeId,
					enable,
					Pair.of(baseValue, operation),
					Optional.of(new ChampionModifierCondition(
							Optional.of(Sets.newHashSet(new ResourceLocation("minecraft:creeper"))),
							Optional.of(MinMaxBounds.IntBound.ANY),
							Optional.of(new AffixesPredicate(Sets.newHashSet(), MinMaxBounds.IntBound.ANY, MinMaxBounds.IntBound.ANY)),
							ConfigEnums.Permission.BLACKLIST
					))
			);

			settings.put(attributeId, setting);
		});

		// 写入所有数据
		Path outputFolder = generator.getOutputFolder();
		for (Map.Entry<ResourceLocation, ModifierSetting> entry : settings.entrySet()) {
			ResourceLocation attributeId = entry.getKey();
			Path outputPath = outputFolder
					.resolve("data")
					.resolve(attributeId.getNamespace())
					.resolve(AttributesModifierDataLoader.getFolder())
					.resolve(attributeId.getPath() + ".json");

			try {
				IDataProvider.save(
						GSON,
						cache,
						ModifierSetting.MAP_CODEC.codec()
								.encodeStart(JsonOps.INSTANCE, entry.getValue())
								.result()
								.orElseThrow(()->new RuntimeException("Failed to serialize modifiers for " + attributeId)),
						outputPath
				);
			} catch (IOException e) {
				throw new IOException("Failed to save attribute modifier " + attributeId, e);
			}
		}
	}

	@Override
	public String getName() {
		return "attribute modifiers";
	}
}
