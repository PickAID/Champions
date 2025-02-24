package top.theillusivec4.champions.common.datagen;

import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.common.integration.gateways_to_eternity.Gateways;
import top.theillusivec4.champions.common.integration.gateways_to_eternity.GatewaysSetting;
import top.theillusivec4.champions.common.registry.AffixTypes;
import top.theillusivec4.champions.common.util.Utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class GatewaysConfigProvider implements DataProvider {

	private final PackOutput packOutput;
	private final CompletableFuture<HolderLookup.Provider> lookupProvider;
	private final List<GatewaysSetting> gatewaysSettings = new ArrayList<>();

	public GatewaysConfigProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		this.packOutput = packOutput;
		this.lookupProvider = lookupProvider;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cachedOutput) {
		List<CompletableFuture<?>> futures = new ArrayList<>();
		gatewaysSettings.add(new GatewaysSetting(Utils.getLocation("wave_0to1"),
				MinMaxBounds.Ints.atLeast(1), List.of(AffixTypes.HASTY.get().getIdentifier()),
				MinMaxBounds.Ints.between(0, 1), Gateways.NORMAL,
				Optional.of(List.of(new ResourceLocation("minecraft:pig"), new ResourceLocation("minecraft:zombie"))),
				Optional.of(false)));
		gatewaysSettings.add(new GatewaysSetting(Utils.getLocation("wave_2to5"),
				MinMaxBounds.Ints.between(2, 5), List.of(AffixTypes.MAGNETIC.get().getIdentifier(), AffixTypes.DAMPENING.get().getIdentifier(), AffixTypes.DESECRATING.get().getIdentifier()),
				MinMaxBounds.Ints.between(2, 4), Gateways.NORMAL,
				Optional.of(List.of(new ResourceLocation("minecraft:pig"), new ResourceLocation("minecraft:zombie"))),
				Optional.of(false)));
		gatewaysSettings.forEach(gatewaysSetting -> {

			Path outputPath = packOutput.getOutputFolder()
					.resolve("data/" + gatewaysSetting.id().getNamespace() + "/gateway_setting/" + gatewaysSetting.id().getPath() + ".json");
			futures.add(lookupProvider.thenCompose(provider ->
					DataProvider.saveStable(cachedOutput, GatewaysSetting.CODEC.encodeStart(JsonOps.INSTANCE, gatewaysSetting).get().orThrow().getAsJsonObject(), outputPath)
			));
		});


		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return "Gateways_setting";
	}
}
