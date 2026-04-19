package top.theillusivec4.champions.integrations.theoneprobe;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.integrations.theoneprobe.elements.ElementColoredText;
import top.theillusivec4.champions.integrations.theoneprobe.elements.ElementStar;
import top.theillusivec4.champions.integrations.theoneprobe.overrides.DisplayNameOverride;
import top.theillusivec4.champions.integrations.theoneprobe.providers.ChampionPropertyEntityProvider;
import top.theillusivec4.champions.integrations.theoneprobe.providers.EntityAffixesInfoEntityProvider;
import top.theillusivec4.champions.util.Util;

import java.util.Locale;
import java.util.function.Function;

public final class ChampionsTheOneProbePlugin implements Function<ITheOneProbe, Void> {
  public static final ResourceLocation ELEMENT_STAR = Util.id("element_star");
  public static final ResourceLocation ELEMENT_COLORED_TEXT = Util.id("element_colored_text");
  public static final ResourceLocation PROVIDER_CHAMPION_PROPERTY = Util.id("provider_champion_property");
  public static final ResourceLocation PROVIDER_ENTITY_AFFIXES = Util.id("provider_entity_affixes");

  private ChampionsTheOneProbePlugin() {
  }

  public static ChampionsTheOneProbePlugin create() {
    return new ChampionsTheOneProbePlugin();
  }

  @Override
  public Void apply(ITheOneProbe probe) {
    probe.registerEntityProvider(ChampionPropertyEntityProvider.create());
    probe.registerEntityProvider(EntityAffixesInfoEntityProvider.create());
    probe.registerEntityDisplayOverride(DisplayNameOverride.create());
    probe.registerElementFactory(ElementStar.factory());
    probe.registerElementFactory(ElementColoredText.factory());
    return null;
  }

  public static final class Codecs {
    public static final Codec<ElementAlignment> ELEMENT_ALIGNMENT = Codec.STRING.xmap(string -> {
      for (ElementAlignment value : ElementAlignment.values()) {
        if (value.name().toLowerCase(Locale.ROOT).equals(string)) {
          return value;
        }
      }
      return ElementAlignment.ALIGN_CENTER;
    }, alignment -> alignment.name().toLowerCase(Locale.ROOT));

    private Codecs() {
    }
  }

  public static final class StreamCodecs {
    public static final StreamCodec<ByteBuf, ElementAlignment> ELEMENT_ALIGNMENT = ByteBufCodecs.fromCodec(Codecs.ELEMENT_ALIGNMENT);

    private StreamCodecs() {
    }
  }
}
