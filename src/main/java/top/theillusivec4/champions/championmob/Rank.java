package top.theillusivec4.champions.championmob;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import top.theillusivec4.champions.affix.AffixInstance;
import top.theillusivec4.champions.affix.provider.AffixProvider;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.ChampionsUtil;

import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public record Rank(
  Component description,
  int tier,
  TextColor color,
  int weight,
  boolean boss,
  Optional<HolderSet<EntityType<?>>> supportedEntityTypes,
  HolderSet<AffixProvider> affixes
) implements WeightedEntry {
  public static final Codec<Rank> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("description").forGetter(Rank::description),
    Codec.intRange(1, 255).fieldOf("tier").forGetter(Rank::tier),
    TextColor.CODEC.fieldOf("color").forGetter(Rank::color),
    Codec.intRange(1, 1024).fieldOf("weight").forGetter(Rank::weight),
    Codec.BOOL.fieldOf("boss").forGetter(Rank::boss),
    RegistryCodecs.homogeneousList(Registries.ENTITY_TYPE).optionalFieldOf("supported_entity_types").forGetter(Rank::supportedEntityTypes),
    AffixProvider.LIST_CODEC.optionalFieldOf("affixes", HolderSet.empty()).forGetter(Rank::affixes)
  ).apply(instance, Rank::new));
  public static final Codec<Holder<Rank>> REFERENCE_CODEC = RegistryFileCodec.create(ChampionsRegistries.RANK, DIRECT_CODEC, false);
  public static final Codec<HolderSet<Rank>> LIST_CODEC = RegistryCodecs.homogeneousList(ChampionsRegistries.RANK, DIRECT_CODEC);
  public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Rank>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ChampionsRegistries.RANK);

  public static Rank.Builder builder() {
    return new Builder();
  }

  public Stream<AffixInstance> createAffixInstances(Entity entity, RandomSource random, DifficultyInstance difficulty) {
    return createAffixInstances(entity.getType(), random, difficulty);
  }

  public Stream<AffixInstance> createAffixInstances(EntityType<?> entityType, RandomSource random, DifficultyInstance difficulty) {
    Stream.Builder<AffixInstance> builder = Stream.builder();
    this.affixes.stream().forEach(provider -> provider.value().get(entityType, random, difficulty).forEach(builder::add));
    return builder.build();
  }

  public boolean isSupported(Entity entity) {
    return this.isSupported(entity.getType());
  }

  public boolean isSupported(EntityType<?> entityType) {
    return this.supportedEntityTypes.isEmpty() || this.supportedEntityTypes.get().contains(entityType.builtInRegistryHolder());
  }

  @Override
  public Weight getWeight() {
    return Weight.of(this.weight);
  }

  public static class Builder {
    private UnaryOperator<MutableComponent> nameFactory = UnaryOperator.identity();
    private int tier = 1;
    private float growthFactor;
    private TextColor color = TextColor.fromLegacyFormat(ChatFormatting.WHITE);
    private int weight = 5;
    private boolean boss = false;
    private HolderSet<EntityType<?>> supportedEntityTypes = null;
    private HolderSet<AffixProvider> affixes = HolderSet.empty();

    private Builder() {
    }

    public Builder setGrowthFactor(float growthFactor) {
      this.growthFactor = growthFactor;
      return this;
    }

    public Builder customName(UnaryOperator<MutableComponent> nameFactory) {
      this.nameFactory = nameFactory;
      return this;
    }

    public Builder tier(int tier) {
      this.tier = tier;
      return this;
    }

    public Builder color(TextColor color) {
      this.color = color;
      return this;
    }

    public Builder weight(int weight) {
      this.weight = weight;
      return this;
    }

    public Builder boss(boolean boss) {
      this.boss = boss;
      return this;
    }

    public Builder supportedEntityTypes(HolderSet<EntityType<?>> supportedEntityTypes) {
      this.supportedEntityTypes = supportedEntityTypes;
      return this;
    }

    public Builder affixes(HolderSet<AffixProvider> affixes) {
      this.affixes = affixes;
      return this;
    }

    public Rank build(ResourceLocation id) {
      return new Rank(
        this.nameFactory.apply(Component.translatable(ChampionsUtil.makeDescriptionId("rank", id))),
        this.tier,
        this.color,
        this.weight,
        this.boss,
        Optional.ofNullable(this.supportedEntityTypes),
        this.affixes
      );
    }
  }
}
