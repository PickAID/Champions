package top.theillusivec4.champions.common.datagen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.api.AttributesModifierDataLoader;
import top.theillusivec4.champions.api.ChampionModifierCondition;
import top.theillusivec4.champions.api.ModifierSetting;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.loot.AffixesPredicate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AttributesModifierDataProvider implements DataProvider {

    private final DataGenerator generator;
    private final Map<ResourceLocation, ModifierSetting> settings = new HashMap<>();

    public AttributesModifierDataProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(CachedOutput cache) throws IOException {
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
                            Optional.of(Set.of(new ResourceLocation("minecraft:creeper"))),
                            Optional.of(MinMaxBounds.Ints.ANY),
                            Optional.of(new AffixesPredicate(Set.of(), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY)),
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
                DataProvider.saveStable(
                        cache,
                        ModifierSetting.MAP_CODEC.codec()
                                .encodeStart(JsonOps.INSTANCE, entry.getValue())
                                .result()
                                .orElseThrow(),
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
