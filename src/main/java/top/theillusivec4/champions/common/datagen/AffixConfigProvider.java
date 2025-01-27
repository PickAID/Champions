package top.theillusivec4.champions.common.datagen;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixSetting;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AffixConfigProvider implements DataProvider {

    private final DataGenerator generator;
    private final Map<String, JsonElement> affixSettings = new HashMap<>();

    public AffixConfigProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(CachedOutput cache) throws IOException {
        // 收集所有需要生成的数据
        Champions.API.getAffixes().forEach(affix -> {
            var affixId = affix.getIdentifier();
            JsonElement jsonElement = AffixSetting.CODEC.encodeStart(JsonOps.INSTANCE, affix.getSetting())
                    .get()
                    .orThrow()
                    .getAsJsonObject();

            affixSettings.put(affixId.toString(), jsonElement);
        });

        // 写入所有数据
        Path outputFolder = generator.getOutputFolder();

        for (Map.Entry<String, JsonElement> entry : affixSettings.entrySet()) {
            String[] splitId = entry.getKey().split(":");
            String namespace = splitId[0];
            String path = splitId[1];

            Path outputPath = outputFolder
                    .resolve("data")
                    .resolve(namespace)
                    .resolve("affix_setting")
                    .resolve(path + ".json");

            try {
                DataProvider.saveStable(cache, entry.getValue(), outputPath);
            } catch (IOException e) {
                throw new IOException("Failed to save affix setting " + entry.getKey(), e);
            }
        }
    }

    @Override
    public String getName() {
        return "Affix_configs";
    }
}
