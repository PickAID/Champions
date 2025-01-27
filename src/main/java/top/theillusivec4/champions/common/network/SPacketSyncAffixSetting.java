package top.theillusivec4.champions.common.network;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record SPacketSyncAffixSetting(Map<ResourceLocation, AffixSetting> map) {

    private static final Codec<Map<ResourceLocation, AffixSetting>> MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, AffixSetting.CODEC);

    public static SPacketSyncAffixSetting decode(FriendlyByteBuf buf) {
        return new SPacketSyncAffixSetting(MAPPER.parse(NbtOps.INSTANCE, buf.readNbt()).result().orElse(new HashMap<>()));
    }

    /**
     * Apply setting and category map from datapack
     */
    public static void handelSettingMainThread() {
        Champions.getDataLoader().getLoadedData().forEach((resourceLocation, affixSetting) ->
                Champions.API.getAffix(affixSetting.type()).ifPresent(affix -> {
                    affix.applySetting(affixSetting);
                    Champions.API.addCategory(affix.getCategory(), affix);
                }));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt((CompoundTag) MAPPER.encodeStart(NbtOps.INSTANCE, this.map).result().orElse(new CompoundTag()));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isClient()) {
            ctx.get().enqueueWork(() -> Champions.getDataLoader().cache(this.map));
        }
        ctx.get().enqueueWork(SPacketSyncAffixSetting::handelSettingMainThread);
        ctx.get().setPacketHandled(true);
    }
}
