package top.theillusivec4.champions.common.datagen;


import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.champions.common.integration.gateways_to_eternity.GatewaysSetting;
import top.theillusivec4.champions.common.registry.AffixTypes;
import top.theillusivec4.champions.common.util.MinMaxBoundsHelper;
import top.theillusivec4.champions.common.util.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GatewaysConfigProvider implements IDataProvider {

	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private final DataGenerator generator;
	private final List<GatewaysSetting> gatewaysSettings = new ArrayList<>();

	public GatewaysConfigProvider(DataGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void run(DirectoryCache cachedOutput) {
		gatewaysSettings.add(new GatewaysSetting(Utils.getLocation("wave_0to1"),
				MinMaxBounds.IntBound.atLeast(1), Lists.newArrayList(AffixTypes.HASTY.get().getIdentifier()),
				MinMaxBoundsHelper.between(0, 1),
				Optional.of(Lists.newArrayList(ResourceLocation.tryParse("minecraft:pig"), ResourceLocation.tryParse("minecraft:zombie"))),
				Optional.of(false)));
		gatewaysSettings.add(new GatewaysSetting(Utils.getLocation("wave_2to5"),
				MinMaxBoundsHelper.between(2, 5), Lists.newArrayList(AffixTypes.MAGNETIC.get().getIdentifier(), AffixTypes.DAMPENING.get().getIdentifier(), AffixTypes.DESECRATING.get().getIdentifier()),
				MinMaxBoundsHelper.between(2, 4),
				Optional.of(Lists.newArrayList(ResourceLocation.tryParse("minecraft:pig"), ResourceLocation.tryParse("minecraft:zombie"))),
				Optional.of(false)));
		gatewaysSettings.forEach(gatewaysSetting -> {

			Path outputPath = generator.getOutputFolder().resolve("data/" + gatewaysSetting.id().getNamespace() + "/gateway_setting/" + gatewaysSetting.id().getPath() + ".json");

			try {
				IDataProvider.save(
						GSON,
						cachedOutput,
						GatewaysSetting.CODEC
								.encodeStart(JsonOps.INSTANCE, gatewaysSetting)
								.result()
								.orElseThrow(() -> new JsonSyntaxException("Could not save gateway settings to " + outputPath)),
						outputPath
				);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

	}

	@Override
	public String getName() {
		return "Gateways_setting";
	}
}
