package top.theillusivec4.champions.common.registry;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.BasicAffixBuilder;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.common.affix.*;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.util.Utils;

import java.util.List;
import java.util.function.Supplier;

public class AffixTypes {
    public static final RegistryObject<AdaptableAffix> ADAPTABLE;
    public static final RegistryObject<ArcticAffix> ARCTIC;
    public static final RegistryObject<DampeningAffix> DAMPENING;
    public static final RegistryObject<DesecratingAffix> DESECRATING;
    public static final RegistryObject<EnkindlingAffix> ENKINDLING;
    public static final RegistryObject<HastyAffix> HASTY;
    public static final RegistryObject<InfestedAffix> INFESTED;
    public static final RegistryObject<KnockingAffix> KNOCKING;
    public static final RegistryObject<LivelyAffix> LIVELY;
    public static final RegistryObject<MagneticAffix> MAGNETIC;
    public static final RegistryObject<MoltenAffix> MOLTEN;
    public static final RegistryObject<ParalyzingAffix> PARALYZING;
    public static final RegistryObject<PlaguedAffix> PLAGUED;
    public static final RegistryObject<ReflectiveAffix> REFLECTIVE;
    public static final RegistryObject<ShieldingAffix> SHIELDING;
    public static final RegistryObject<WoundingAffix> WOUNDING;

    static {
        // add more config for data generate file example
        ADAPTABLE = register("adaptable", () -> new BasicAffixBuilder<>(AdaptableAffix::new)
                .setType(Utils.getLocation("adaptable"))
                .setCategory(AffixCategory.DEFENSE)
                .setHasSub(false)
                .setMobList(List.of(new ResourceLocation("minecraft:pig"), new ResourceLocation("minecraft:creeper")))
                .setMobPermission(ConfigEnums.Permission.BLACKLIST).setEnable(true)
                .setTier(MinMaxBounds.Ints.between(1, 100))
                .build());
        ARCTIC = register("arctic", () -> new BasicAffixBuilder<>(ArcticAffix::new).setType(Utils.getLocation("arctic")).setCategory(AffixCategory.CC).setEnable(true).build());
        DAMPENING = register("dampening", () -> new BasicAffixBuilder<>(DampeningAffix::new).setType(Utils.getLocation("dampening")).setCategory(AffixCategory.DEFENSE).setEnable(true).build());
        DESECRATING = register("desecrating", () -> new BasicAffixBuilder<>(DesecratingAffix::new).setType(Utils.getLocation("desecrating")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
        ENKINDLING = register("enkindling", () -> new BasicAffixBuilder<>(EnkindlingAffix::new).setType(Utils.getLocation("enkindling")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
        HASTY = register("hasty", () -> new BasicAffixBuilder<>(HastyAffix::new).setType(Utils.getLocation("hasty")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
        INFESTED = register("infested", () -> new BasicAffixBuilder<>(InfestedAffix::new).setType(Utils.getLocation("infested")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
        KNOCKING = register("knocking", () -> new BasicAffixBuilder<>(KnockingAffix::new).setType(Utils.getLocation("knocking")).setCategory(AffixCategory.CC).setEnable(true).build());
        LIVELY = register("lively", () -> new BasicAffixBuilder<>(LivelyAffix::new).setType(Utils.getLocation("lively")).setCategory(AffixCategory.DEFENSE).setEnable(true).build());
        MAGNETIC = register("magnetic", () -> new BasicAffixBuilder<>(MagneticAffix::new).setType(Utils.getLocation("magnetic")).setCategory(AffixCategory.CC).setEnable(true).build());
        MOLTEN = register("molten", () -> new BasicAffixBuilder<>(MoltenAffix::new).setType(Utils.getLocation("molten")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
        PARALYZING = register("paralyzing", () -> new BasicAffixBuilder<>(ParalyzingAffix::new).setType(Utils.getLocation("paralyzing")).setCategory(AffixCategory.CC).setEnable(true).build());
        PLAGUED = register("plagued", () -> new BasicAffixBuilder<>(PlaguedAffix::new).setType(Utils.getLocation("plagued")).setCategory(AffixCategory.OFFENSE).setEnable(true).build());
        REFLECTIVE = register("reflective", () -> new BasicAffixBuilder<>(ReflectiveAffix::new).setType(Utils.getLocation("reflective")).setHasSub().setCategory(AffixCategory.OFFENSE).setEnable(true).build());
        SHIELDING = register("shielding", () -> new BasicAffixBuilder<>(ShieldingAffix::new).setType(Utils.getLocation("shielding")).setCategory(AffixCategory.DEFENSE).setEnable(true).build());
        WOUNDING = register("wounding", () -> new BasicAffixBuilder<>(WoundingAffix::new).setType(Utils.getLocation("wounding")).setCategory(AffixCategory.OFFENSE).setHasSub().setEnable(true).build());
    }

    private static <I extends IAffix> RegistryObject<I> register(String name, Supplier<I> supplier) {
        return AffixRegistry.AFFIXES.register(name, supplier);
    }

    public static void register(IEventBus bus) {
        AffixRegistry.AFFIXES.register(bus);
    }
}
