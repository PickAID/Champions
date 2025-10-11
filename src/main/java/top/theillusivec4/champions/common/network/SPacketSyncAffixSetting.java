package top.theillusivec4.champions.common.network;


import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.data.AffixSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class SPacketSyncAffixSetting {

	private static final Codec<Map<ResourceLocation, AffixSetting>> MAPPER =
			Codec.unboundedMap(ResourceLocation.CODEC, AffixSetting.CODEC);
	private final Map<ResourceLocation, AffixSetting> map;

	public SPacketSyncAffixSetting(Map<ResourceLocation, AffixSetting> map) {
		this.map = map;
	}

	public static SPacketSyncAffixSetting decode(PacketBuffer buf) {
		return new SPacketSyncAffixSetting(MAPPER.parse(NBTDynamicOps.INSTANCE, buf.readNbt()).result().orElse(new HashMap<>()));
	}

	/**
	 * Apply setting and category map from datapack
	 */
	public static void handelSettingMainThread() {
		Champions.API.getAffixDataLoader().getLoadedData().forEach((resourceLocation, affixSetting) ->
				Champions.API.getAffix(affixSetting.type()).ifPresent(affix -> {
					affix.applySetting(affixSetting);
					Champions.API.addCategory(affix.getCategory(), affix);
				}));
	}

	public void encode(PacketBuffer buf) {
		buf.writeNbt((CompoundNBT) MAPPER.encodeStart(NBTDynamicOps.INSTANCE, this.map).result().orElse(new CompoundNBT()));
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isClient()) {
			ctx.get().enqueueWork(() -> Champions.API.getAffixDataLoader().cache(this.map));
		}
		ctx.get().enqueueWork(SPacketSyncAffixSetting::handelSettingMainThread);
		ctx.get().setPacketHandled(true);
	}

	public Map<ResourceLocation, AffixSetting> map() {
		return map;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		SPacketSyncAffixSetting that = (SPacketSyncAffixSetting) obj;
		return Objects.equals(this.map, that.map);
	}

	@Override
	public int hashCode() {
		return Objects.hash(map);
	}

	@Override
	public String toString() {
		return "SPacketSyncAffixSetting[" +
				"map=" + map + ']';
	}

}
