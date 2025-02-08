package top.theillusivec4.champions.common.datagen;

import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.common.integration.gateways_to_eternity.GatewaysSetting;
import top.theillusivec4.champions.common.registry.AffixTypes;
import top.theillusivec4.champions.common.util.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GatewaysConfigProvider implements DataProvider {

    private final DataGenerator generator;
    private final List<GatewaysSetting> gatewaysSettings = new ArrayList<>();

    public GatewaysConfigProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(CachedOutput cachedOutput) {
        gatewaysSettings.add(new GatewaysSetting(Utils.getLocation("test_1"), MinMaxBounds.Ints.atLeast(2), List.of(AffixTypes.HASTY.get().getIdentifier()), MinMaxBounds.Ints.between(0, 1), Optional.of(List.of(new ResourceLocation("minecraft:pig"), new ResourceLocation("minecraft:zombie")))));
        gatewaysSettings.add(new GatewaysSetting(Utils.getLocation("test_2"), MinMaxBounds.Ints.atLeast(2), List.of(AffixTypes.MAGNETIC.get().getIdentifier(), AffixTypes.DAMPENING.get().getIdentifier(), AffixTypes.DESECRATING.get().getIdentifier()), MinMaxBounds.Ints.between(2, 4), Optional.of(List.of(new ResourceLocation("minecraft:pig"), new ResourceLocation("minecraft:zombie")))));
        gatewaysSettings.forEach(gatewaysSetting -> {

            Path outputPath = generator.getOutputFolder().resolve("data/" + gatewaysSetting.id().getNamespace() + "/gateway_setting/" + gatewaysSetting.id().getPath() + ".json");

            try {
                DataProvider.saveStable(
                        cachedOutput,
                        GatewaysSetting.CODEC
                                .encodeStart(JsonOps.INSTANCE, gatewaysSetting)
                                .result()
                                .orElseThrow(),
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
